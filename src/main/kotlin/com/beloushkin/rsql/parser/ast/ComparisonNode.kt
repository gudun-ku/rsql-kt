package com.beloushkin.rsql.parser.ast

/**
 * This node represents a comparison with operator, selector and arguments,
 * e.g. `name=in=(Jimmy,James)`.
 */
class ComparisonNode(
    val operator: ComparisonOperator,
    val selector: String,
    arguments: List<String>
) : AbstractNode() {
    private val _arguments: List<String> = arguments.toList()
    val arguments: List<String> get() = _arguments

    init {
        require(selector.isNotBlank()) { "selector must not be blank" }
        require(arguments.isNotEmpty()) { "arguments list must not be empty" }
        require(operator.isMultiValue || arguments.size == 1) {
            "operator $operator expects single argument, but multiple values given"
        }
    }

    override fun <R, A> accept(visitor: RSQLVisitor<R, A>, param: A?): R = visitor.visit(this, param)

    fun withOperator(newOperator: ComparisonOperator): ComparisonNode =
        ComparisonNode(newOperator, selector, arguments)

    fun withArguments(newArguments: List<String>): ComparisonNode =
        ComparisonNode(operator, selector, newArguments)

    override fun equals(other: Any?): Boolean =
        other is ComparisonNode &&
        operator == other.operator &&
        selector == other.selector &&
        arguments == other.arguments

    override fun hashCode(): Int =
        31 * operator.hashCode() + 31 * selector.hashCode() + arguments.hashCode()

    override fun toString(): String {
        val args = if (arguments.size > 1)
            arguments.joinToString(prefix = "(", postfix = ")")
        else
            arguments.joinToString()
        return "$selector$operator=$args"
    }
}