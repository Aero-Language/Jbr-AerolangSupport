package com.aerolang.aerolangsupport

import com.aerolang.aerolangsupport.highlighting.AeroLexer
import com.aerolang.aerolangsupport.highlighting.AeroTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

// Flat tree: the file is one node holding every token; real analysis is done by the LSP. Gives the file the Aero language so editor handlers apply
class AeroParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = AeroLexer()

    override fun createParser(project: Project?): PsiParser = PsiParser { root: IElementType, builder: PsiBuilder ->
        val mark = builder.mark()
        while (!builder.eof()) builder.advanceLexer()
        mark.done(root)
        builder.treeBuilt
    }

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = TokenSet.create(AeroTokenTypes.COMMENT)

    override fun getStringLiteralElements(): TokenSet = TokenSet.create(AeroTokenTypes.STRING, AeroTokenTypes.CHARACTER)

    override fun createElement(node: ASTNode): PsiElement = throw UnsupportedOperationException(node.elementType.toString())

    override fun createFile(viewProvider: FileViewProvider): PsiFile = AeroFile(viewProvider)

    companion object {
        val FILE = IFileElementType(AeroLanguage.Instance)
    }
}
