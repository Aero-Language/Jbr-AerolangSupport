package com.aerolang.aerolangsupport.editing

import com.aerolang.aerolangsupport.AeroLanguage
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

// Typing a closer on an otherwise blank line moves it back to the indent of its opener
class AeroTypedHandler : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): TypedHandlerDelegate.Result {
        if (file.language != AeroLanguage.Instance || c !in "})]") return TypedHandlerDelegate.Result.CONTINUE

        val doc = editor.document
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(doc)

        val caret = editor.caretModel.offset
        val lineStart = doc.getLineStartOffset(doc.getLineNumber(caret))
        val text = doc.charsSequence
        val typedAt = caret - 1
        if (typedAt < lineStart) return TypedHandlerDelegate.Result.CONTINUE

        for (i in lineStart until typedAt) if (text[i] != ' ' && text[i] != '\t') return TypedHandlerDelegate.Result.CONTINUE

        val indent = AeroIndent.compute(doc, lineStart, AeroIndent.unit(file))
        if (text.subSequence(lineStart, typedAt).toString() == indent) return TypedHandlerDelegate.Result.CONTINUE

        doc.replaceString(lineStart, typedAt, indent)
        editor.caretModel.moveToOffset(lineStart + indent.length + 1)
        return TypedHandlerDelegate.Result.CONTINUE
    }
}
