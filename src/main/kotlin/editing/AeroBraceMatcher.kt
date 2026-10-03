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

    // Always close; the platform itself skips it when an unmatched closer of the same type follows
    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
}