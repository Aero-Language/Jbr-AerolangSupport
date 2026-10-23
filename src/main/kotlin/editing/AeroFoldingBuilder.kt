package com.aerolang.aerolangsupport.editing

import com.aerolang.aerolangsupport.highlighting.AeroLexer
import com.aerolang.aerolangsupport.highlighting.AeroTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

// Folds multi-line {} blocks and block comments, found with the lexer since there is no real tree
class AeroFoldingBuilder : FoldingBuilderEx() {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val result = ArrayList<FoldingDescriptor>()
        val node = root.node
        val text = document.charsSequence
        val opens = ArrayList<Int>()

        val lexer = AeroLexer()
        lexer.start(text, 0, text.length, 0)
        while (true) {
            val type = lexer.tokenType ?: break
            val start = lexer.tokenStart
            val end = lexer.tokenEnd
            when (type) {
                AeroTokenTypes.LBRACE -> opens.add(start)
                AeroTokenTypes.RBRACE -> if (opens.isNotEmpty()) {
                    val open = opens.removeAt(opens.size - 1)
                    if (document.getLineNumber(open) != document.getLineNumber(start)) result.add(FoldingDescriptor(node, TextRange(open, end), null, "{...}"))
                }
                AeroTokenTypes.COMMENT ->
                    if (end - start > 4 && text[start + 1] == '*' && text.endsWith("*/", start, end) && document.getLineNumber(start) != document.getLineNumber(end - 1))
                        result.add(FoldingDescriptor(node, TextRange(start, end), null, "/*...*/"))
            }
            lexer.advance()
        }
        return result.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String = "..."

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}
