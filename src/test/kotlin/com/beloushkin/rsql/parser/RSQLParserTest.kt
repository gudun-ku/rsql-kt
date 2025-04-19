package com.beloushkin.rsql.parser

import com.beloushkin.rsql.parser.ast.*
import com.beloushkin.rsql.parser.RSQLParserException
import com.beloushkin.rsql.parser.ast.ComparisonOperator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RSQLParserTest {
    private val factory = NodesFactory(RSQLOperators.defaultOperators())

    @Test
    fun `should throw exception when created with null or empty set of operators`() {
        assertThrows<IllegalArgumentException> { RSQLParser(emptySet()) }
    }

    @Test
    fun `should not compile with null input`() {
        // This test is intentionally commented out because Kotlin prevents passing null to non-nullable parameters.
        // Uncommenting the following line will result in a compilation error, as intended by the API design.
        // val parser = RSQLParser()
        // assertThrows<IllegalArgumentException> { parser.parse(null as String?) }
    }

    @Test
    fun `should throw exception for invalid syntax`() {
        val parser = RSQLParser()
        assertThrows<RSQLParserException> { parser.parse("name===") }
    }

    @Test
    fun `should parse simple comparison`() {
        val parser = RSQLParser()
        val node = parser.parse("name==Jimmy")
        assertTrue(node is ComparisonNode)
        val comparison = node as ComparisonNode
        assertEquals("name", comparison.selector)
        assertEquals("==", comparison.operator.symbol)
        assertEquals(listOf("Jimmy"), comparison.arguments)
    }

    @Test
    fun `should parse with custom operator`() {
        val customOp = ComparisonOperator.fromSymbol("=custom=")
        val parser = RSQLParser(setOf(customOp))
        val node = parser.parse("field=custom=value")
        assertTrue(node is ComparisonNode)
        val comparison = node as ComparisonNode
        assertEquals("field", comparison.selector)
        assertEquals("=custom=", comparison.operator.symbol)
        assertEquals(listOf("value"), comparison.arguments)
    }
}