package com.xinjiangshao.azurepublish

import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.blob.BlobServiceClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

open class AzurePublishTask : DefaultTask() {

    @get:Input
    lateinit var extension: AzurePublishExtension

    @TaskAction
    fun action() {
        extension.validate()
        val blobServiceClient = BlobServiceClientBuilder()
            .endpoint(extension.connectionString)
            .buildClient()

        val blobContainerClient = blobServiceClient.getBlobContainerClient(extension.container)

        val assetDir = File(extension.assetDir)

        assetDir.walkTopDown().forEach {
            project.logger.info("Uploaded ${it.name}")
            val blobClient = blobContainerClient.getBlobClient(it.name);
            blobClient.upload(it.inputStream(), it.length());
        }
    }
}
