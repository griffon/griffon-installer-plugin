/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Gant script that creates a single executable JAR.
 *
 * @author Andres Almiray
 * @author Josh Reed
 *
 * @since 0.4
 */

includeTargets << griffonScript("_GriffonInit")
installerPluginBase = getPluginDirForName('installer').file as String
installerWorkDir = "${basedir}/installer/izpack"
includeTargets << pluginScript("installer","_CreateInstaller")

installerWorkDir = "${basedir}/installer/jar"
binaryDir = installerWorkDir

target(jarLauncherSanityCheck:"") {
    depends(checkVersion, classpath, createStructure)
    def src = new File(installerWorkDir)
    if (src && src.list()) {
        createJarLauncher()
    } else {
        println """No JAR launcher sources were found.
Make sure you call 'griffon prepare-jar-launcher' first
and configure the files appropriately.
"""
    }
}

target(createJarLauncher: "Creates a single executable JAR") {
    copyAllAppArtifacts()

    event("CreateJarLauncherStart", [])

    // delete
    ant.delete(dir:"${installerWorkDir}/classes", quiet: true, failOnError: false)
    ant.delete(dir:"${installerWorkDir}/dist", quiet: true, failOnError: false)
    ant.mkdir(dir:"${installerWorkDir}/classes")
    ant.mkdir(dir:"${installerWorkDir}/dist")

    // unzip our jars
    ant.unjar(dest:"${installerWorkDir}/classes", overwrite: true) {
        fileset(dir: "${basedir}/staging", includes: "**/*.jar")
    }

    // create a single jar
    ant.jar(basedir: "${installerWorkDir}/classes", destfile:"${installerWorkDir}/dist/${griffonAppName}-${griffonAppVersion}.jar", index: "true", manifest:"${installerWorkDir}/MANIFEST.MF")

    event("CreateJarLauncherEnd", [])
}

setDefaultTarget(jarLauncherSanityCheck)