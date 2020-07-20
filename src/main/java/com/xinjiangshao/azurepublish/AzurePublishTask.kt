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
    lateinit var tarFile: File

    @OutputDirectory
    lateinit var outputPath: File

    @TaskAction
    fun action() {
        extension.validate()
        val account = CloudStorageAccount.parse(extension.connectionString)
        val client = account.createCloudBlobClient()
        val container = client.getContainerReference(extension.container)
        container.createIfNotExists()

        tarFile = File(extension.packageTarFile)

        outputPath = File(extension.path, "dist")
        val blob = container.getBlockBlobReference(outputPath.path)
        blob.upload(tarFile.inputStream(), tarFile.length())

        project.logger.info("Uploaded ${tarFile.name} to ${container.name}:$outputPath")
    }
}
