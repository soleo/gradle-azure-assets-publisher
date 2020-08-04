package com.xinjiangshao.azurepublish

import java.lang.IllegalArgumentException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.Project

open class AzurePublishExtension(project: Project) {
    var connectionString: String = ""

    var container: String = ""

    val inputDir: DirectoryProperty = project.objects.directoryProperty()

    var assetDir: String = ""

    fun validate() {
        if (connectionString.isEmpty()) throw IllegalArgumentException("Provide non-empty 'connectionString' property")
        if (container.isEmpty()) throw IllegalArgumentException("Provide non-empty 'container' property")
        if (assetDir.isEmpty()) throw IllegalArgumentException("Provide non-empty 'assetDir' property")
    }
}
