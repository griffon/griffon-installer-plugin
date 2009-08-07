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
 * Gant script that creates an Jsmooth based installer
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

includeTargets << griffonScript("_GriffonInit")
installerPluginBase = getPluginDirForName('installer').file as String
installerWorkDir = "${basedir}/installer/izpack"
includeTargets << pluginScript("installer","_CreateInstaller")

installerWorkDir = "${basedir}/installer/jsmooth"
skeletonsDir = installerPluginBase + "/src/templates/jsmooth/skeletons"
binaryDir = installerWorkDir

ant.path(id : 'installerJarSet') {
    fileset(dir: "${installerPluginBase}/lib/installer", includes : "*.jar")
}
ant.taskdef(name: "jsmoothgen", classname: "net.charabia.jsmoothgen.ant.JSmoothGen", classpathref: "installerJarSet")

target(jsmoothSanityCheck:"") {
    depends(checkVersion, classpath, createStructure)
    def src = new File( installerWorkDir )
    if( src && src.list() ) {
        createJsmoothLauncher()
    } else {
        println """No JSmooth launcher sources were found.
Make sure you call 'griffon prepare-jsmooth-launcher' first
and configure the files appropriately.
"""
    }
}

target(createJsmoothLauncher: "Creates a Jsmooth launcher") {
    depends(checkVersion, packageApp, classpath)
    packageApp()

    event("CreateJsmoothLauncherStart", [])

    // clean up previous launchers
    ant.delete(dir:"${installerWorkDir}/dist", quiet: true, failOnError: false)
    ant.mkdir(dir:"${installerWorkDir}/dist")
    ant.mkdir(dir:"${installerWorkDir}/dist/lib")

    // copy our jars from staging
    ant.copy(todir:"${installerWorkDir}/dist/lib") {
        fileset(dir: "${basedir}/staging", includes: "*.jar")
    }

    // get our jars and inject into the JSmooth file
    def jarlist = []
    new File("${installerWorkDir}/dist/lib").eachFileMatch(~/.*\.jar/) { f ->
        jarlist << "<classPath>lib/${f.name}</classPath>"
    }
    ant.copy(file:"${installerWorkDir}/${griffonAppName}.jsmooth", tofile:"${installerWorkDir}/dist/${griffonAppName}.jsmooth")
    ant.copy(file:"${installerWorkDir}/${griffonAppName}-icon.png", tofile:"${installerWorkDir}/dist/${griffonAppName}-icon.png")
    ant.copy(file:"${installerWorkDir}/msvcr71.dll", tofile:"${installerWorkDir}/dist/msvcr71.dll")
    ant.replace(dir: "${installerWorkDir}/dist", includes: "*.jsmooth" ) {
        replacefilter( token: "@app.jars@", value: jarlist.join('\n') )
    }

    // generate the launcher
    ant.jsmoothgen(project: "${installerWorkDir}/dist/${griffonAppName}.jsmooth", skeletonroot: skeletonsDir)

    // cleanup
    ant.delete(file:"${installerWorkDir}/dist/${griffonAppName}.jsmooth")
    ant.delete(file:"${installerWorkDir}/dist/${griffonAppName}-icon.png")

    // generate a zip
    ant.zip(basedir: "${installerWorkDir}/dist",
            destfile: "${installerWorkDir}/dist/${griffonAppName}-windows-${griffonAppVersion}.zip")

    event("CreateJsmoothLauncherEnd", [])
}

setDefaultTarget(jsmoothSanityCheck)