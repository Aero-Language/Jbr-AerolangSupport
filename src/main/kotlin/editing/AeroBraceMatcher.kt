package com.aerolang.aerolangsupport.editing

import com.aerolang.aerolangsupport.highlighting.AeroTokenTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

class AeroBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> = arrayOf(
        BracePair(AeroTokenTypes.LPAREN, AeroTokenTypes.RPAREN, false),
        BracePair(AeroTokenTypes.LBRACE, AeroTokenTypes.RBRACE, true),
        BracePair(AeroTokenTypes.LBRACKET, AeroTokenTypes.RBRACKET, false)
    )

    // Only close a pair when what follows can't be the start of something it would wrap
    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean =
        contextType == null || when (contextType) {
            AeroTokenTypes.WHITE_SPACE, AeroTokenTypes.COMMENT,
            AeroTokenTypes.RPAREN, AeroTokenTypes.RBRACE, AeroTokenTypes.RBRACKET,
            AeroTokenTypes.COMMA, AeroTokenTypes.SEMICOLON -> true
            else -> false
        }

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
}
