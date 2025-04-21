package com.beloushkin.rsql.parser

import com.beloushkin.rsql.parser.ast.ComparisonOperator
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.system.measureNanoTime
import kotlin.test.assertTrue

class RSQLParserPerfTest {
    private val parser = RSQLParser(
        setOf(
            ComparisonOperator.fromSymbol("=="),
            ComparisonOperator.fromSymbol("=in=", true),
            ComparisonOperator.fromSymbol("!="),
            ComparisonOperator.fromSymbol("=out=", true),
            ComparisonOperator.fromSymbol(">"),
            ComparisonOperator.fromSymbol("<"),
            ComparisonOperator.fromSymbol(">="),
            ComparisonOperator.fromSymbol("<=")
        )
    )

    private val expressions = listOf(
        // Simple
        "name==Jimmy",
        "age!=30",
        // Medium
        "name==Jimmy;age>25",
        "name==Jimmy,age==30",
        "!(name==Jimmy)",
        // Complex
        "(name==Jimmy;age>25),city==London",
        "name==Jimmy;age>25;(city==London,city==Paris)",
        "((name==Jimmy;age>25),city==London);country==UK",
        // Very complex
        (1..10).joinToString(",") { "field$it==value$it" },
        (1..20).joinToString(";") { "field$it==value$it" },
        (1..5).joinToString(",") { "(" + (1..5).joinToString(";") { i -> "f${'$'}it$i==v${'$'}it$i" } + ")" }
    )

    @Test
    fun `performance of parsing various expressions`() {
        val csvFile = File("build/reports/rsql_perf_report.csv")
        csvFile.printWriter().use { out ->
            out.println("expr_length,parse_time_ms,expr")
            expressions.forEach { expr ->
                val minTime = (1..5).map {
                    measureNanoTime {
                        val node = parser.parse(expr)
                        assertTrue(node != null)
                    }
                }.minOrNull() ?: 0L
                val exprShort = expr.replace("\"", "\"\"").take(80)
                out.println("${expr.length},${minTime / 1_000_000.0},\"$exprShort\"")
            }
        }
        println("CSV report written to build/reports/rsql_perf_report.csv")
    }

    @Test
    fun `parsing 1000 simple expressions`() {
        val simpleExpr = "name==Jimmy"
        val count = 1000
        val totalTime = measureNanoTime {
            repeat(count) {
                val node = parser.parse(simpleExpr)
                assertTrue(node != null)
            }
        }
        println("\nParsing $count simple expressions took ${totalTime / 1_000_000.0} ms")
    }
}
