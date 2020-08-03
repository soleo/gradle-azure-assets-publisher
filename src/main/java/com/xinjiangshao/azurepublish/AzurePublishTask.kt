package com.xinjiangshao.azurepublish

import com.microsoft.azure.storage.CloudStorageAccount
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class AzurePublishTask : DefaultTask() {

    @get:Input
    lateinit var extension: AzurePublishExtension

    @InputFile
    lateinit var assetDir: File

    @OutputDirectory
    lateinit var outputPath: File

    @TaskAction
    fun action() {
        extension.validate()
        val account = CloudStorageAccount.parse(extension.connectionString)
        val client = account.createCloudBlobClient()
        val container = client.getContainerReference(extension.container)
        container.createIfNotExists()

        assetDir = File(extension.assetDir)

        assetDir.walkTopDown().forEach {
            project.logger.info("Uploaded ${it.name} to ${container.name}")
            val blob = container.getBlockBlobReference(it.name);
            blob.upload(it.inputStream(), it.length());
        }
    }
}
