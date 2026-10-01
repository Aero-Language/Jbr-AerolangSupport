package com.aerolang.aerolangsupport

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.extensions.ExtensionDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.toNioPathOrNull
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerDescriptor
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import okio.Path.Companion.toPath
import java.io.FileNotFoundException

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


}