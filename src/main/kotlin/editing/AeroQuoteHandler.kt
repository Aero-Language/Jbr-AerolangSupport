package com.aerolang.aerolangsupport.editing

import com.aerolang.aerolangsupport.highlighting.AeroTokenTypes
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.highlighter.HighlighterIterator

class AeroQuoteHandler : SimpleTokenSetQuoteHandler(AeroTokenTypes.STRING, AeroTokenTypes.CHARACTER) {
    // The string token of $"..." starts at the '$', so its quote sits one character in
    override fun isOpeningQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        if (super.isOpeningQuote(iterator, offset)) return true
        if (!myLiteralTokenSet.contains(iterator.tokenType)) return false

        val text = iterator.document.charsSequence
        return offset > 0 && iterator.start == offset - 1 && text[offset - 1] == '$'
    }
}
