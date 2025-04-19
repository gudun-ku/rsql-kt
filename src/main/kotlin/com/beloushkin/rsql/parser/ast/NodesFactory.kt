package com.beloushkin.rsql.parser.ast

class NodesFactory(val operators: Set<ComparisonOperator>) {
    companion object {
        fun defaultOperators(): Set<ComparisonOperator> = setOf(
            ComparisonOperator.fromSymbol("=="),
            ComparisonOperator.fromSymbol("=in=", true)
        )
    }

    fun createLogicalNode(operator: LogicalOperator, nodes: List<Node>): LogicalNode {
        return when (operator) {
            LogicalOperator.AND -> AndNode(nodes)
            LogicalOperator.OR -> OrNode(nodes)
            LogicalOperator.NOT -> {
                require(nodes.size == 1) { "NOT operator requires exactly one child" }
                NotNode(nodes[0])
            }
        }
    }

    fun createComparisonNode(operator: String, selector: String, arguments: List<String>): ComparisonNode {
        val op = operators.find { it.symbol == operator }
            ?: throw IllegalArgumentException("Unknown operator: $operator")
        return ComparisonNode(op, selector, arguments)
    }
}
