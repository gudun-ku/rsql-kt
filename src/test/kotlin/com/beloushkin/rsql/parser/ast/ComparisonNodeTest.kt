package com.beloushkin.rsql.parser.ast

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ComparisonNodeTest {
    private val singleValueOp = ComparisonOperator.fromSymbol("=eq=")
    private val multiValueOp = ComparisonOperator.fromSymbol("=in=", true)

    @Test
    fun `valid single value node`() {
        val node = ComparisonNode(singleValueOp, "name", listOf("Jimmy"))
        assertEquals(singleValueOp, node.operator)
        assertEquals("name", node.selector)
        assertEquals(listOf("Jimmy"), node.arguments)
    }

    @Test
    fun `valid multi value node`() {
        val node = ComparisonNode(multiValueOp, "name", listOf("Jimmy", "James"))
        assertEquals(multiValueOp, node.operator)
        assertEquals("name", node.selector)
        assertEquals(listOf("Jimmy", "James"), node.arguments)
    }

    @Test
    fun `blank selector throws`() {
        assertThrows<IllegalArgumentException> {
            ComparisonNode(singleValueOp, "", listOf("Jimmy"))
        }
    }

    @Test
    fun `empty arguments throws`() {
        assertThrows<IllegalArgumentException> {
            ComparisonNode(singleValueOp, "name", emptyList())
        }
    }

    @Test
    fun `single value op with multiple arguments throws`() {
        assertThrows<IllegalArgumentException> {
            ComparisonNode(singleValueOp, "name", listOf("Jimmy", "James"))
        }
    }

    @Test
    fun `withOperator returns modified copy`() {
        val node = ComparisonNode(singleValueOp, "name", listOf("Jimmy"))
        val newNode = node.withOperator(multiValueOp)
        assertEquals(multiValueOp, newNode.operator)
        assertEquals(node.selector, newNode.selector)
        assertEquals(node.arguments, newNode.arguments)
        assertNotEquals(node, newNode)
    }

    @Test
    fun `multi value op allows single argument`() {
        val node = ComparisonNode(multiValueOp, "name", listOf("Jimmy"))
        assertEquals(listOf("Jimmy"), node.arguments)
    }

    @Test
    fun `toString, equals, hashCode reflect data`() {
        val node1 = ComparisonNode(singleValueOp, "name", listOf("Jimmy"))
        val node2 = ComparisonNode(singleValueOp, "name", listOf("Jimmy"))
        val node3 = ComparisonNode(multiValueOp, "name", listOf("Jimmy", "James"))
        assertEquals(node1, node2)
        assertEquals(node1.hashCode(), node2.hashCode())
        assertNotEquals(node1, node3)
        assertTrue(node1.toString().contains("name"))
    }

    @Test
    fun `arguments list is immutable after construction`() {
        val args = mutableListOf("Jimmy")
        val node = ComparisonNode(singleValueOp, "name", args)
        args.add("James")
        assertEquals(listOf("Jimmy"), node.arguments)
    }

    @Test
    fun `withOperator returns new instance and does not mutate original`() {
        val node = ComparisonNode(singleValueOp, "name", listOf("Jimmy"))
        val newNode = node.withOperator(multiValueOp)
        assertNotSame(node, newNode)
        assertEquals(multiValueOp, newNode.operator)
        assertEquals(singleValueOp, node.operator)
    }

    @Test
    fun `accept calls visitor`() {
        val node = ComparisonNode(singleValueOp, "name", listOf("Jimmy"))
        var called = false
        val visitor = object : RSQLVisitor<String, Unit> {
            override fun visit(node: ComparisonNode, param: Unit?): String {
                called = true
                return "visited"
            }
            override fun visit(node: AndNode, param: Unit?): String = ""
            override fun visit(node: OrNode, param: Unit?): String = ""
            override fun visit(node: NotNode, param: Unit?): String = ""
        }
        val result = node.accept(visitor, null)
        assertTrue(called)
        assertEquals("visited", result)
    }
}
