package com.beloushkin.rsql.parser.ast

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ComparisonOperatorTest {
    @Test
    fun `single symbol factory sets symbol and isMultiValue`() {
        val op = ComparisonOperator.fromSymbol("=eq=")
        assertEquals("=eq=", op.symbol)
        assertEquals(listOf("=eq="), op.symbols)
        assertFalse(op.isMultiValue)
    }

    @Test
    fun `two symbol factory sets symbols and isMultiValue`() {
        val op = ComparisonOperator.fromSymbols("=eq=", "==", true)
        assertEquals("=eq=", op.symbol)
        assertEquals(listOf("=eq=", "=="), op.symbols)
        assertTrue(op.isMultiValue)
    }

    @Test
    fun `invalid symbol throws`() {
        assertThrows<IllegalArgumentException> {
            ComparisonOperator.fromSymbol("not-valid")
        }
    }

    @Test
    fun `symbols must not be empty`() {
        assertThrows<IllegalArgumentException> {
            ComparisonOperator.fromArray(arrayOf(), false)
        }
    }

    @Test
    fun `toString returns symbol`() {
        val op = ComparisonOperator.fromSymbol(">=")
        assertEquals(">=", op.toString())
    }

    @Test
    fun `equals and hashCode work`() {
        val op1 = ComparisonOperator.fromSymbol("=eq=")
        val op2 = ComparisonOperator.fromSymbol("=eq=")
        val op3 = ComparisonOperator.fromSymbol("=ne=")
        assertEquals(op1, op2)
        assertEquals(op1.hashCode(), op2.hashCode())
        assertNotEquals(op1, op3)
    }
}
