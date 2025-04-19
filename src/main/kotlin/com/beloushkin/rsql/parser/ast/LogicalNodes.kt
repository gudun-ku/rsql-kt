package com.beloushkin.rsql.parser.ast


// AndNode.kt
class AndNode(children: List<Node>) : LogicalNode(LogicalOperator.AND, children) {
    
    override fun withChildren(children: List<Node>): AndNode = AndNode(children)
    
    override fun <R, A> accept(visitor: RSQLVisitor<R, A>, param: A?): R = visitor.visit(this, param)
}

// OrNode.kt
class OrNode(children: List<Node>) : LogicalNode(LogicalOperator.OR, children) {
    
    override fun withChildren(children: List<Node>): OrNode = OrNode(children)
    
    override fun <R, A> accept(visitor: RSQLVisitor<R, A>, param: A?): R = visitor.visit(this, param)
}

// NotNode.kt
class NotNode(child: Node) : LogicalNode(LogicalOperator.NOT, listOf(child)) {
    
    val child: Node get() = getChildren().first()
    
    override fun withChildren(children: List<Node>): NotNode {
        require(children.size == 1) { "NOT operator requires exactly one child" }
        return NotNode(children.first())
    }
    
    override fun <R, A> accept(visitor: RSQLVisitor<R, A>, param: A?): R = visitor.visit(this, param)
    
    override fun toString(): String = "$operator${super.toString()}"
}