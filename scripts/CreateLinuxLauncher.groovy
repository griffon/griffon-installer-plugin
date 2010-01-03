/*
 * Copyright 2009-2010 the original author or authors.
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
 * Gant script that creates a Linux launcher.
 *
 * @author Andres Almiray
 * @author Josh Reed
 *
 * @since 0.4
 */

includeTargets << griffonScript("_GriffonInit")
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << pluginScript("installer","_CreateInstaller")

installerWorkDir = "${basedir}/installer/linux"
binaryDir = installerWorkDir

target(linuxLauncherSanityCheck:"") {
    depends(checkVersion, classpath, createStructure)
    def src = new File(installerWorkDir)
    if (src ) {
        createLinuxLauncher()
    } else {
        println """No Linux launcher sources were found.
Make sure you call 'griffon prepare-linux-launcher' first
and configure the files appropriately.
"""
    }
}

target(createLinuxLauncher: "Creates a Linux launcher") {
    depends(checkVersion, packageApp, classpath)
    packageApp()

    event("CreateLinuxLauncherStart", [])

    // clean up old launchers
    ant.delete(dir:"${installerWorkDir}/dist", quiet: true, failOnError: false)
    ant.mkdir(dir:"${installerWorkDir}/dist")

    copyLaunchScripts()
    copyAppLibs()
    ant.delete {
        fileset(dir: "${binaryDir}/bin", includes: "*.bat")
    }

    // copy files around
    ant.mkdir(dir:"${installerWorkDir}/dist/bin")
    ant.copy(todir: "${installerWorkDir}/dist/bin") {
        fileset(dir: "${binaryDir}/bin")
    }

    // copy our jars
    ant.mkdir(dir:"${installerWorkDir}/dist/lib")
    ant.copy(todir: "${installerWorkDir}/dist/lib") {
        fileset(dir: "${binaryDir}/lib")
    }

    // create a zip
    ant.zip(basedir: "${installerWorkDir}/dist", destfile: "${installerWorkDir}/dist/${griffonAppName}-linux-${griffonAppVersion}.zip")

    event("CreateLinuxLauncherEnd", [])
}

setDefaultTarget(linuxLauncherSanityCheck)
