package com.beloushkin.rsql.parser

import com.beloushkin.rsql.parser.ast.ComparisonOperator
import com.beloushkin.rsql.parser.ast.Node
import com.beloushkin.rsql.parser.ast.NodesFactory
import com.beloushkin.rsql.parser.ast.RSQLOperators
import com.beloushkin.rsql.parser.RSQLParserParser
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.misc.ParseCancellationException
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset

/**
 * Parser of the RSQL (RESTful Service Query Language).
 *
 * This class is designed for both Java and Kotlin interoperability.
 */
class RSQLParser {
    private val encoding = Charset.forName("UTF-8")
    private val nodesFactory: NodesFactory

    /**
     * Creates a new instance of [RSQLParser] with the default set of comparison operators.
     */
    constructor() {
        this.nodesFactory = NodesFactory(RSQLOperators.defaultOperators())
    }

    /**
     * Creates a new instance of [RSQLParser] that supports only the specified comparison
     * operators.
     *
     * @param operators A set of supported comparison operators. Must not be empty.
     */
    constructor(operators: Set<ComparisonOperator>) {
        require(operators.isNotEmpty()) { "operators must not be null or empty" }
        this.nodesFactory = NodesFactory(operators)
    }

    /**
     * Parses the RSQL expression and returns AST.
     *
     * @param query The query expression to parse.
     * @return A root of the parsed AST.
     * @throws RSQLParserException If some exception occurred during parsing, i.e. the
     *         [query] is syntactically invalid.
     */
    fun parse(query: String): Node {
        requireNotNull(query) { "query must not be null" }
        
        try {
            // Create a CharStream from the query string
            val input = CharStreams.fromString(query)
            
            // Create a lexer that feeds off of input
            val lexer = RSQLParserLexer(input)
            
            // Create a buffer of tokens pulled from the lexer
            val tokens = CommonTokenStream(lexer)
            
            // Create a parser that feeds off the tokens buffer
            val parser = RSQLParserParser(tokens).apply {
                factory = nodesFactory
                errorHandler = BailErrorStrategy()
            }
            
            // Begin parsing at input rule
            val tree = parser.input()
            
            // Return the Node result
            return tree.node
            
        } catch (ex: ParseCancellationException) {
            throw RSQLParserException(ex.cause ?: ex)
        } catch (ex: Exception) {
            throw RSQLParserException(ex)
        }
    }
}