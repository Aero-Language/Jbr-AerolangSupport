package com.aerolang.aerolangsupport

import com.aerolang.aerolangsupport.highlighting.AeroSemanticColors
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.toNioPathOrNull
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.intellij.platform.lsp.api.customization.LspCustomization
import com.intellij.platform.lsp.api.customization.LspSemanticTokensSupport

class AeroLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        // Start the LSP server when an .aero file is opened
        if (file.fileType == AeroFileType.Instance) {
            serverStarter.ensureServerStarted(AeroLspServerDescriptor(project))
        }
    }
}

class AeroLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Aero") {

    override fun isSupportedFile(file: VirtualFile): Boolean {
        return file.fileType == AeroFileType.Instance
    }

    override fun createCommandLine(): GeneralCommandLine {
        val lspExecutablePath = BinarySearcher.findLsp()

        return GeneralCommandLine().apply {
            withExePath(lspExecutablePath)
            withWorkingDirectory(project.basePath?.toNioPathOrNull())
        }
    }

    // Colors the type and variable tokens the server sends; tokens without a key keep the lexer's colors
    override val lspCustomization: LspCustomization = object : LspCustomization() {
        override val semanticTokensCustomizer = object : LspSemanticTokensSupport() {
            override fun getTextAttributesKey(tokenType: String, modifiers: List<String>): TextAttributesKey? =
                AeroSemanticColors.keyFor(tokenType)
        }
    }
}