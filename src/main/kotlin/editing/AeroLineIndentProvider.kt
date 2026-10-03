package com.aerolang.aerolangsupport.editing

import com.aerolang.aerolangsupport.AeroLanguage
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.codeStyle.lineIndent.LineIndentProvider

// Without a formatter the platform guesses indents (e.g. after typing a lone '{'); this gives it the same answer Enter uses
class AeroLineIndentProvider : LineIndentProvider {
    override fun getLineIndent(project: Project, editor: Editor, language: Language?, offset: Int): String? {
        val doc = editor.document
        val file = PsiDocumentManager.getInstance(project).getPsiFile(doc) ?: return null
        val lineStart = doc.getLineStartOffset(doc.getLineNumber(offset))
        return AeroIndent.compute(doc, lineStart, AeroIndent.unit(file))
    }

    override fun isSuitableFor(language: Language?): Boolean = language != null && language.isKindOf(AeroLanguage.Instance)
}