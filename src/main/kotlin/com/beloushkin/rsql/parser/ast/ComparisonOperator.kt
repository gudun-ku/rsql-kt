package com.beloushkin.rsql.parser.ast

import java.util.regex.Pattern

class ComparisonOperator private constructor(
    private val _symbols: List<String>,
    val isMultiValue: Boolean = false
) {
    val symbol: String get() = _symbols[0]
    val symbols: List<String> get() = _symbols.toList()

    companion object {
        private val SYMBOL_PATTERN = Pattern.compile("=[a-zA-Z]*=|[><]=?|!=")

        @JvmStatic
        fun fromSymbol(symbol: String, isMultiValue: Boolean = false): ComparisonOperator =
            ComparisonOperator(listOf(symbol), isMultiValue)

        @JvmStatic
        fun fromSymbols(symbol: String, altSymbol: String, isMultiValue: Boolean = false): ComparisonOperator =
            ComparisonOperator(listOf(symbol, altSymbol), isMultiValue)

        // For Java compatibility: array-based constructor
        @JvmStatic
        fun fromArray(symbols: Array<String>, isMultiValue: Boolean = false): ComparisonOperator =
            ComparisonOperator(symbols.toList(), isMultiValue)
    }

    init {
        require(_symbols.isNotEmpty()) { "symbols must not be null or empty" }
        _symbols.forEach { sym ->
            require(isValidOperatorSymbol(sym)) { "symbol must match: $SYMBOL_PATTERN" }
        }
    }

    private fun isValidOperatorSymbol(str: String): Boolean =
        str.isNotBlank() && SYMBOL_PATTERN.matcher(str).matches()

    override fun toString(): String = symbol

    override fun equals(other: Any?): Boolean =
        other is ComparisonOperator && symbol == other.symbol

    override fun hashCode(): Int = symbol.hashCode()
}