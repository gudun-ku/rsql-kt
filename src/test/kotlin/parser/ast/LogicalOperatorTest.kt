package com.beloushkin.rsql.parser.ast

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LogicalOperatorTest {
    @Test
    fun `test toString returns correct symbol`() {
        assertEquals(";", LogicalOperator.AND.toString())
        assertEquals(",", LogicalOperator.OR.toString())
        assertEquals("!", LogicalOperator.NOT.toString())
    }
}
