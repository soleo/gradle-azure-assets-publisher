package com.xinjiangshao.azurepublish

import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.blob.BlobServiceClient
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option
import java.io.File

open class AzurePublishTask : DefaultTask() {

    @get:Input
    lateinit var extension: AzurePublishExtension

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    val inputDir: DirectoryProperty = project.objects.directoryProperty()

    @Option(option = "input-dir", description = "Input directory path relative to project directory")
    fun setInputDir(path: String) {
        inputDir.set(project.layout.projectDirectory.dir(path))
    }

    @TaskAction
    fun action() {
        extension.validate()
        val blobServiceClient = BlobServiceClientBuilder()
            .endpoint(extension.connectionString)
            .buildClient()

        val blobContainerClient = blobServiceClient.getBlobContainerClient(extension.container)

        val assetDir = inputDir.get().asFile

        assetDir.walkTopDown().forEach {
            project.logger.info("Uploaded ${it.name}")
            val blobClient = blobContainerClient.getBlobClient(it.name);
            blobClient.upload(it.inputStream(), it.length());
        }
    }
}
