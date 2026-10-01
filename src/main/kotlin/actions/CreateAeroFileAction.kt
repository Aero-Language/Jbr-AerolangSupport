package com.aerolang.aerolangsupport.actions

import com.aerolang.aerolangsupport.AeroIcons
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class CreateAeroFileAction : CreateFileFromTemplateAction("Aero File", "Creates a new Aero source file", AeroIcons.small) {
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder
            .setTitle("New Aero File")
            .addKind("AeroFile", AeroIcons.small, "Aero File")
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return "Create Aero File: $newName"
    }
}