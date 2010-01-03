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
 * Gant script that creates a Mac application bundle.
 *
 * @author Andres Almiray
 * @author Josh Reed
 *
 * @since 0.4
 */

includeTargets << griffonScript("_GriffonInit")
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << pluginScript("installer","_CreateInstaller")

installerWorkDir = "${basedir}/installer/mac"
binaryDir = installerWorkDir

ant.path(id : 'installerJarSet') {
    fileset(dir: "${installerPluginBase}/lib/installer", includes : "*.jar")
}
ant.taskdef(name: "jarbundler", classname: "net.sourceforge.jarbundler.JarBundler", classpathref: "installerJarSet")

target(macLauncherSanityCheck:"") {
    depends(checkVersion, classpath, createStructure)
    def src = new File(installerWorkDir)
    if (src && src.list()) {
        createMacLauncher()
    } else {
        println """No Mac launcher sources were found.
Make sure you call 'griffon prepare-mac-launcher' first
and configure the files appropriately.
"""
    }
}

target(createMacLauncher: "Creates a Mac launcher") {
    depends(checkVersion, packageApp, classpath)
    packageApp()

    event("CreateMacLauncherStart", [])

    // clean up old launchers
    ant.delete(dir:"${installerWorkDir}/dist", quiet: true, failOnError: false)
    ant.mkdir(dir:"${installerWorkDir}/dist")

    // create an app bundle
    ant.jarbundler(dir: "${installerWorkDir}/dist", name:"${griffonAppName}", mainclass: appMainClass,
        stubfile:"${installerWorkDir}/${griffonAppName}", version:"${griffonAppVersion}", icon: "${installerWorkDir}/${griffonAppName}.icns") {
        jarfileset(dir: "${basedir}/staging", includes:"*.jar")
    }

    // create a DMG if on a Mac
    ant.condition(property: "os.isOSX", value: true) {
        and {
            os(family: "mac")
            and { os(family: "unix") }
        }
    }
    if (Boolean.valueOf(ant.project.properties.'os.isOSX')) {
        ant.exec(executable: "hdiutil") {
           arg(line:"create -srcfolder ${installerWorkDir}/dist ${installerWorkDir}/${griffonAppName}-${griffonAppVersion}.dmg" )
        }
        ant.move(file:"${installerWorkDir}/${griffonAppName}-${griffonAppVersion}.dmg", tofile:"${installerWorkDir}/dist/${griffonAppName}-${griffonAppVersion}.dmg")
    } else {
        ant.echo(message:"Skipping DMG file creation as it requires the build be run on Mac OS X")
    }

    event("CreateMacLauncherEnd", [])
}

setDefaultTarget(macLauncherSanityCheck)
