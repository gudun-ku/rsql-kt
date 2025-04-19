package com.beloushkin.rsql.parser.ast


/**
 * An adapter for the [RSQLVisitor] interface with a simpler contract that omits the optional
 * second argument.
 *
 * @param R Return type of the visitor's method.
 */
abstract class NoArgRSQLVisitorAdapter<R> : RSQLVisitor<R, Nothing?> {
    abstract fun visit(node: AndNode): R
    abstract fun visit(node: OrNode): R
    abstract fun visit(node: NotNode): R
    abstract fun visit(node: ComparisonNode): R

    override fun visit(node: AndNode, param: Nothing?): R = visit(node)
    override fun visit(node: OrNode, param: Nothing?): R = visit(node)
    override fun visit(node: NotNode, param: Nothing?): R = visit(node)
    override fun visit(node: ComparisonNode, param: Nothing?): R = visit(node)
}
