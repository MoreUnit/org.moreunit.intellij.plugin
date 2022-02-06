package com.github.gianasista.sampleplugin.services

import com.intellij.openapi.project.Project
import com.github.gianasista.sampleplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
