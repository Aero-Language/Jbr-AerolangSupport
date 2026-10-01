package com.aerolang.aerolangsupport

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.toNioPathOrNull
import com.intellij.util.system.CpuArch
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object BinarySearcher {
    fun findLsp(): String {
        return find("Lsp")
    }

    private fun find(binName: String): String {
        val os = when {
            SystemInfo.isLinux -> "linux"
            SystemInfo.isMac -> "osx"
            SystemInfo.isWindows -> "win"
            else -> "unknown"
        }

        val arch = when {
            CpuArch.isIntel64() -> "x64"
            CpuArch.isIntel32() -> "x32"
            CpuArch.isArm32() -> "arm32"
            CpuArch.isArm64() -> "arm64"
            else -> "unknown"
        }

        val extension = if (SystemInfo.isWindows) ".exe" else ""

        val resourcePath = "/bin/$os-$arch/$binName$extension"

        val resourceStream = BinarySearcher::class.java.getResourceAsStream(resourcePath)
            ?: throw FileNotFoundException("Resource not found in classpath: $resourcePath")

        val tempDir: Path = Paths.get(System.getProperty("user.home"), ".aerolang-lsp", "$os-$arch")
        Files.createDirectories(tempDir)

        val targetFile: Path = tempDir.resolve("$binName$extension")

        resourceStream.use { input ->
            Files.copy(input, targetFile, StandardCopyOption.REPLACE_EXISTING)
        }

        // Make it executable on Unix systems
        if (!SystemInfo.isWindows) {
            targetFile.toFile().setExecutable(true)
        }

        return targetFile.toAbsolutePath().toString()
    }
}