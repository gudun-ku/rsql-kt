package com.beloushkin.rsql.parser

import com.beloushkin.rsql.parser.ast.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RSQLComplexParserTest {
    // Use all standard operators, including '!=', for advanced tests
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

    @Test
    fun `should parse AND expression`() {
        val node = parser.parse("name==Jimmy;age==30")
        assertTrue(node is LogicalNode)
        val andNode = node as LogicalNode
        assertEquals(LogicalOperator.AND, andNode.operator)
        assertEquals(2, andNode.getChildren().size)
        val left = andNode.getChildren()[0] as ComparisonNode
        val right = andNode.getChildren()[1] as ComparisonNode
        assertEquals("name", left.selector)
        assertEquals("Jimmy", left.arguments[0])
        assertEquals("age", right.selector)
        assertEquals("30", right.arguments[0])
    }

    @Test
    fun `should parse OR expression`() {
        val node = parser.parse("name==Jimmy,age==30")
        assertTrue(node is LogicalNode)
        val orNode = node as LogicalNode
        assertEquals(LogicalOperator.OR, orNode.operator)
        assertEquals(2, orNode.getChildren().size)
    }

    @Test
    fun `should parse NOT expression`() {
        val node = parser.parse("!(name==Jimmy)")
        assertTrue(node is LogicalNode)
        val notNode = node as LogicalNode
        assertEquals(LogicalOperator.NOT, notNode.operator)
        assertEquals(1, notNode.getChildren().size)
        val comp = notNode.getChildren()[0] as ComparisonNode
        assertEquals("name", comp.selector)
        assertEquals("Jimmy", comp.arguments[0])
    }

    @Test
    fun `should parse nested AND_OR expression`() {
        val node = parser.parse("name==Jimmy;(age==30,city==London)")
        assertTrue(node is LogicalNode)
        val andNode = node as LogicalNode
        assertEquals(LogicalOperator.AND, andNode.operator)
        assertEquals(2, andNode.getChildren().size)
        val left = andNode.getChildren()[0] as ComparisonNode
        val right = andNode.getChildren()[1] as LogicalNode
        assertEquals(LogicalOperator.OR, right.operator)
        assertEquals(2, right.getChildren().size)
    }

    @Test
    fun `should parse IN operator`() {
        val node = parser.parse("name=in=(Jimmy,John,Jane)")
        assertTrue(node is ComparisonNode)
        val comp = node as ComparisonNode
        assertEquals("name", comp.selector)
        assertEquals("=in=", comp.operator.symbol)
        assertEquals(listOf("Jimmy", "John", "Jane"), comp.arguments)
    }

    @Test
    fun `should parse complex expression with all operators`() {
        val node = parser.parse("!(name==Jimmy;(age==30,city==London));status==active")
        assertTrue(node is LogicalNode)
        val andNode = node as LogicalNode
        assertEquals(LogicalOperator.AND, andNode.operator)
        assertEquals(2, andNode.getChildren().size)
        val notNode = andNode.getChildren()[0] as LogicalNode
        assertEquals(LogicalOperator.NOT, notNode.operator)
        val innerAnd = notNode.getChildren()[0] as LogicalNode
        assertEquals(LogicalOperator.AND, innerAnd.operator)
        val innerLeft = innerAnd.getChildren()[0] as ComparisonNode
        val innerRight = innerAnd.getChildren()[1] as LogicalNode
        assertEquals(LogicalOperator.OR, innerRight.operator)
        val statusComp = andNode.getChildren()[1] as ComparisonNode
        assertEquals("status", statusComp.selector)
        assertEquals("active", statusComp.arguments[0])
    }

    @Test
    fun `should parse deeply nested expression with multiple NOTs`() {
        val node = parser.parse("!(!(name==Jimmy,age!=30));city==London")
        assertTrue(node is LogicalNode)
        val andNode = node as LogicalNode
        assertEquals(LogicalOperator.AND, andNode.operator)
        // First child: NOT(NOT(OR))
        val notNode = andNode.getChildren()[0] as LogicalNode
        assertEquals(LogicalOperator.NOT, notNode.operator)
        val innerNot = notNode.getChildren()[0] as LogicalNode
        assertEquals(LogicalOperator.NOT, innerNot.operator)
        val innerOr = innerNot.getChildren()[0] as LogicalNode
        assertEquals(LogicalOperator.OR, innerOr.operator)
        val left = innerOr.getChildren()[0] as ComparisonNode
        val right = innerOr.getChildren()[1] as ComparisonNode
        assertEquals("name", left.selector)
        assertEquals("Jimmy", left.arguments[0])
        assertEquals("age", right.selector)
        assertEquals("30", right.arguments[0])
        assertEquals("!=", right.operator.symbol)
        // Second child: city==London
        val cityComp = andNode.getChildren()[1] as ComparisonNode
        assertEquals("city", cityComp.selector)
        assertEquals("London", cityComp.arguments[0])
    }

    @Test
    fun `should parse expression with multiple levels of AND and OR`() {
        val node = parser.parse("name==Jimmy,age==30;(city==London,status==active)")
        println(node)
        assertTrue(node is LogicalNode)
        val rootOr = node as LogicalNode
        assertEquals(LogicalOperator.OR, rootOr.operator)
        assertEquals(2, rootOr.getChildren().size)
        // First child: name==Jimmy
        val leftComp = rootOr.getChildren()[0] as ComparisonNode
        assertEquals("name", leftComp.selector)
        assertEquals("Jimmy", leftComp.arguments[0])
        // Second child: AND(age==30, OR(city==London, status==active))
        val rightAnd = rootOr.getChildren()[1] as LogicalNode
        assertEquals(LogicalOperator.AND, rightAnd.operator)
        assertEquals(2, rightAnd.getChildren().size)
        val ageComp = rightAnd.getChildren()[0] as ComparisonNode
        assertEquals("age", ageComp.selector)
        assertEquals("30", ageComp.arguments[0])
        val innerOr = rightAnd.getChildren()[1] as LogicalNode
        assertEquals(LogicalOperator.OR, innerOr.operator)
        assertEquals(2, innerOr.getChildren().size)
        val cityComp = innerOr.getChildren()[0] as ComparisonNode
        assertEquals("city", cityComp.selector)
        assertEquals("London", cityComp.arguments[0])
        val statusComp = innerOr.getChildren()[1] as ComparisonNode
        assertEquals("status", statusComp.selector)
        assertEquals("active", statusComp.arguments[0])
    }
}
