package com.beloushkin.rsql.parser.ast

enum class LogicalOperator(private val symbol: String) {
    AND(";"),
    OR(","),
    NOT("!");

    override fun toString(): String = symbol
}