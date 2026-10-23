package com.aerolang.aerolangsupport

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class AeroFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, AeroLanguage.Instance) {
    override fun getFileType(): FileType = AeroFileType.Instance
}
