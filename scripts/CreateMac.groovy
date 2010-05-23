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
includeTargets << pluginScript("installer","_Create")

ant.path(id : 'installerJarSet') {
    fileset(dir: "${installerPluginBase}/lib/installer", includes : "*.jar")
}
ant.taskdef(name: "jarbundler",
            classname: "net.sourceforge.jarbundler.JarBundler",
            classpathref: "installerJarSet")

target('default': "Creates a Mac launcher") {
    createMac()
}

target(macSanityCheck:"") {
    depends(checkVersion, classpath)

    installerWorkDir = "${projectTargetDir}/installer/mac/dist"
    binaryDir = installerWorkDir
    ant.mkdir(dir: installerWorkDir)
    
    def src = new File("${installerWorkDir}/../")
    if (!src.list()) {
        println """No Mac launcher sources were found.
Make sure you call 'griffon prepare-mac-launcher' first
and configure the files appropriately.
"""
        System.exit(1)
    }
}

target(createMac: "Creates a Mac launcher") {
    depends(macSanityCheck)

    event("CreateMacStart", [])

    // clean up old launchers
    ant.delete(dir: installerWorkDir, quiet: true, failOnError: false)
    ant.mkdir(dir: installerWorkDir)
    copyAllAppArtifacts()

    def iconFile = new File("${installerWorkDir}/${griffonAppName}.icns")
    if(!iconFile.exists()) iconFile = new File("${installerWorkDir}/griffon.icns")

    def bundleDir = installerWorkDir + '/bundle'
    ant.mkdir(dir: bundleDir)

    // create an app bundle
    ant.jarbundler(dir: bundleDir,
                   name: griffonAppName,
                   mainclass: griffonApplicationClass,
                   stubfile: "${installerWorkDir}/../${griffonAppName}",
                   version: griffonAppVersion,
                   icon: iconFile.absolutePath) {
        jarfileset(dir: "${installerWorkDir}/lib", includes: "*.jar")
    }

    def macDistDir = distDir + '/mac'
    ant.delete(dir: macDistDir, quiet: true, failonerror: false)
    ant.mkdir(dir: macDistDir)
    ant.copy(todir: macDistDir) {
        fileset(dir: bundleDir)
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
           arg(line:"create -srcfolder ${bundleDir} ${installerWorkDir}/${griffonAppName}-${griffonAppVersion}.dmg" )
        }
        ant.copy(todir: macDistDir, file: "${installerWorkDir}/${griffonAppName}-${griffonAppVersion}.dmg")
    } else {
        ant.echo(message:"Skipping DMG file creation as it requires the build be run on Mac OS X")
    }

    event("CreateMacEnd", [])
}
