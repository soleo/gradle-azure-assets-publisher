package com.xinjiangshao.azurepublish

import org.gradle.api.Plugin
import org.gradle.api.Project

open class AzurePublishPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("azurePublishConfig", AzurePublishExtension::class.java, project)

        project.tasks.create("azurePublishAssets", AzurePublishTask::class.java) { task ->
            task.description = "Upload Static Assets to Azure Storage"
            task.extension = extension
            task.inputDir.set(extension.inputDir)
        }
    }
}
