/*
 * Copyright 2008-2010 the original author or authors.
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
 * Gant script that creates an IzPack based installer
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

includeTargets << griffonScript("_GriffonInit")
installerPluginBase = getPluginDirForName('installer').file as String
installerWorkDir = "${basedir}/installer/izpack"
includeTargets << pluginScript("installer","_CreateInstaller")

installerWorkDir = "${basedir}/installer/izpack"
binaryDir = installerWorkDir + "/binary"

ant.path(id : "installerJarSet") {
    fileset(dir: "${installerPluginBase}/lib/installer", includes : "*.jar")
}

ant.taskdef( name: "izpack",
             classname: "com.izforge.izpack.ant.IzPackTask",
             classpathref: "installerJarSet" )

target(izPackSanityCheck:"") {
    depends(checkVersion, classpath)
    def src = new File( installerWorkDir )
    if( src && src.list() ) {
        createIzPackInstaller()
    } else {
        println """No IzPack installer sources were found.
Make sure you call 'griffon prepare-izpack-installer' first
and configure the files appropriately.
"""
    }
}

target(createIzPackInstaller: "Creates an IzPack installer") {
    copyAllAppArtifacts()

    ant.izpack( basedir: installerWorkDir,
                input: "${installerWorkDir}/resources/installer.xml",
                output: "${installerWorkDir}/${griffonAppName}-${griffonAppVersion}-installer.jar",
                compression: "deflate",
                compressionLevel: "9" )
}

setDefaultTarget(izPackSanityCheck)
