package com.beloushkin.rsql.parser.ast


/**
 * Superclass of all logical nodes that represents a logical operation that connects
 * children nodes.
 */
abstract class LogicalNode(
    val operator: LogicalOperator,
    private val children: List<Node>
) : AbstractNode(), Iterable<Node> {

    init {
        require(children.isNotEmpty()) { "children must not be empty" }
    }

    /**
     * Returns a copy of this node with the specified children nodes.
     */
    abstract fun withChildren(children: List<Node>): LogicalNode

    /**
     * Iterate over children nodes. The underlying collection is unmodifiable!
     */
    override fun iterator(): Iterator<Node> = children.iterator()

    /**
     * Returns a copy of the children nodes.
     */
    fun getChildren(): List<Node> = children.toList()

    override fun toString(): String = "(${children.joinToString("$operator")})"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LogicalNode) return false
        return children == other.children && operator == other.operator
    }

    override fun hashCode(): Int {
        var result = children.hashCode()
        result = 31 * result + operator.hashCode()
        return result
    }
}

