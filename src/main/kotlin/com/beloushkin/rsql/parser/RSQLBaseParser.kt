package com.beloushkin.rsql.parser

import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.TokenStream
import org.antlr.v4.runtime.Vocabulary
import org.antlr.v4.runtime.atn.ATN

/**
 * Base parser class for the ANTLR4-generated parser.
 */
abstract class RSQLBaseParser(input: TokenStream) : Parser(input) {
    // This class is no longer needed for ANTLR parser inheritance, as @members is used in the grammar file.
    // You may keep it for Kotlin-side helpers, but remove factory and unescapeQuoted from here if not used elsewhere.

    override fun getTokenNames(): Array<String>? = null
    
    override fun getGrammarFileName(): String = "RSQLParser.g4"
    
    override fun getRuleNames(): Array<String> = emptyArray()

    override fun getATN(): ATN {
        throw NotImplementedError("getATN() must be implemented by the generated parser.")
    }
    
    override fun getSerializedATN(): String = ""
    
    override fun getVocabulary(): Vocabulary = EMPTY_VOCABULARY
    
    companion object {
        private val EMPTY_VOCABULARY = object : Vocabulary {
            override fun getDisplayName(tokenType: Int): String = "$tokenType"
            override fun getSymbolicName(tokenType: Int): String? = null
            override fun getLiteralName(tokenType: Int): String? = null
            override fun getMaxTokenType(): Int = 0
        }
    }
}