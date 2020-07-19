package com.xinjiangshao.azurepublish

import com.microsoft.azure.storage.CloudStorageAccount
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class AzurePublishTask : DefaultTask() {
    lateinit var extension: AzurePublishExtension


    @TaskAction
    fun action() {
        extension.validate()
        val account = CloudStorageAccount.parse(extension.connectionString)
        val client = account.createCloudBlobClient()
        val container = client.getContainerReference(extension.container)
        container.createIfNotExists()

        val tarFile = File(extension.packageTarFile)

        val outputPath = File(extension.path, "dist").path
        val blob = container.getBlockBlobReference(outputPath)
        blob.upload(tarFile.inputStream(), tarFile.length())

        project.logger.info("Uploaded ${tarFile.name} to ${container.name}:$outputPath")
    }
}
