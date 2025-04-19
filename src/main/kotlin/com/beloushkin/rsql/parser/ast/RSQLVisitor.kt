package com.beloushkin.rsql.parser.ast

/**
 * An interface for visiting AST nodes of the RSQL.
 *
 * @param R Return type of the visitor's method.
 * @param A Type of an optional parameter passed to the visitor's method.
 */
interface RSQLVisitor<R, A> {
    fun visit(node: AndNode, param: A?): R
    fun visit(node: OrNode, param: A?): R
    fun visit(node: NotNode, param: A?): R
    fun visit(node: ComparisonNode, param: A?): R
}
