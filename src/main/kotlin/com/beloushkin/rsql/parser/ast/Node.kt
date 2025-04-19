package com.beloushkin.rsql.parser.ast

/**
 * Common interface of the AST nodes. Implementations must be immutable.
 */
interface Node {
    /**
     * Accepts the visitor, calls its `visit()` method and returns a result.
     *
     * @param visitor The visitor whose appropriate method will be called.
     * @param param An optional parameter to pass to the visitor.
     * @return An object returned by the visitor (may be `null`).
     */
    fun <R, A> accept(visitor: RSQLVisitor<R, A>, param: A?): R
    
    /**
     * Accepts the visitor, calls its `visit()` method and returns the result.
     * This method just calls [accept] with `null` as the second argument.
     */
    fun <R, A> accept(visitor: RSQLVisitor<R, A>): R = accept(visitor, null)
}