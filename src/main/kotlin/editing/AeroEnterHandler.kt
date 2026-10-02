package com.aerolang.aerolangsupport.editing

import com.aerolang.aerolangsupport.AeroLanguage
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

class AeroEnterHandler : EnterHandlerDelegateAdapter() {
    override fun postProcessEnter(file: PsiFile, editor: Editor, dataContext: DataContext): EnterHandlerDelegate.Result {
        if (file.language != AeroLanguage.Instance) return EnterHandlerDelegate.Result.Continue

        val doc = editor.document
        PsiDocumentManager.getInstance(file.project).doPostponedOperationsAndUnblockDocument(doc)

        val line = doc.getLineNumber(editor.caretModel.offset)
        if (line == 0) return EnterHandlerDelegate.Result.Continue

        val unit = AeroIndent.unit(file)
        val lineStart = doc.getLineStartOffset(line)
        val lineEnd = doc.getLineEndOffset(line)
        val text = doc.charsSequence
        val blanksEnd = AeroIndent.blanksEnd(text, lineStart, lineEnd)
        val indent = AeroIndent.compute(doc, lineStart, unit)

        // Enter between a pair, `{|}`, puts the closer on its own line
        val closer = if (blanksEnd < lineEnd) text[blanksEnd] else ' '
        val opener = AeroIndent.charBefore(text, lineStart)
        val splitsPair = (closer == '}' && opener == '{') || (closer == ')' && opener == '(') || (closer == ']' && opener == '[')

        if (splitsPair) {
            val body = indent + unit
            doc.replaceString(lineStart, blanksEnd, body + "\n" + indent)
            editor.caretModel.moveToOffset(lineStart + body.length)
            return EnterHandlerDelegate.Result.Continue
        }

        doc.replaceString(lineStart, blanksEnd, indent)
        editor.caretModel.moveToOffset(lineStart + indent.length)

        // The platform may already have split a pair; make sure the closer below lines up too
        val next = line + 1
        if (next < doc.lineCount && doc.getLineEndOffset(line) == lineStart + indent.length) {
            val nextStart = doc.getLineStartOffset(next)
            val nextEnd = doc.getLineEndOffset(next)
            val nextBlanksEnd = AeroIndent.blanksEnd(doc.charsSequence, nextStart, nextEnd)
            if (nextBlanksEnd < nextEnd && doc.charsSequence[nextBlanksEnd] in "})]") {
                doc.replaceString(nextStart, nextBlanksEnd, AeroIndent.compute(doc, nextStart, unit))
            }
        }

        return EnterHandlerDelegate.Result.Continue
    }
}
