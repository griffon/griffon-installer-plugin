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
 * Gant script that creates an Jsmooth based installer
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

includeTargets << griffonScript("Init")
installerPluginBase = getPluginDirForName('installer').file as String
includePluginScript("installer","_Create")

ant.path(id : 'installerJarSet') {
    fileset(dir: "${installerPluginBase}/lib/installer", includes : "*.jar")
}
ant.taskdef(name: "jsmoothgen",
            classname: "net.charabia.jsmoothgen.ant.JSmoothGen",
            classpathref: "installerJarSet")

target(jsmoothSanityCheck:"") {
    depends(checkVersion, classpath)

    packageType = 'jsmooth'
    installerDir = "${projectTargetDir}/installer/jsmooth"
    installerWorkDir = "${installerDir}/dist"
    skeletonsDir = installerPluginBase + "/src/templates/jsmooth/skeletons"
    binaryDir = installerWorkDir
    ant.mkdir(dir: installerWorkDir)
    
    def src = new File(installerDir)
    if(!src.list()) {
        println """No JSmooth launcher sources were found.
Make sure you call 'griffon prepare-jsmooth-launcher' first
and configure the files appropriately.
"""
        System.exit(1)
    }
}

target(createPackageJsmooth: "Creates a Jsmooth launcher") {
    depends(jsmoothSanityCheck)

    event("CreatePackageStart", ['jsmooth'])

    // clean up old launchers
    ant.delete(dir: installerWorkDir, quiet: true, failOnError: false)
    ant.mkdir(dir: installerWorkDir)
    copyAllAppArtifacts()

    ant.replace(dir: installerDir, includes: "*.jsmooth") {
        replacefilter(token: "@app.name@", value: griffonAppName)
        replacefilter(token: "@app.version@", value: griffonAppVersion)
        replacefilter(token: "@app.main.class@", value: griffonApplicationClass)
    }
    // get our jars and inject into the JSmooth file

    def jarlist = []
    new File("${installerWorkDir}/lib").eachFileMatch(~/.*\.jar/) { f ->
        jarlist << "<classPath>lib/${f.name}</classPath>"
    }
    ant.copy(file:"${installerDir}/${griffonAppName}.jsmooth",
             tofile:"${installerWorkDir}/${griffonAppName}.jsmooth")
    ant.copy(file:"${installerDir}/${griffonAppName}-icon.png",
             tofile:"${installerWorkDir}/${griffonAppName}-icon.png")
    ant.copy(file:"${installerDir}/msvcr71.dll",
             tofile:"${installerWorkDir}/msvcr71.dll")
    ant.replace(dir: "${installerWorkDir}", includes: "*.jsmooth" ) {
        replacefilter( token: "@app.jars@", value: jarlist.join('\n') )
    }

    // generate the launcher
    ant.jsmoothgen(project: "${installerWorkDir}/${griffonAppName}.jsmooth",
                   skeletonroot: skeletonsDir)

    // cleanup
    ant.delete(file:"${installerWorkDir}/${griffonAppName}.jsmooth")
    ant.delete(file:"${installerWorkDir}/${griffonAppName}-icon.png")

    def jsmoothDistDir = distDir + '/jsmooth'
    ant.delete(dir: jsmoothDistDir, quiet: true, failonerror: false)
    ant.mkdir(dir: jsmoothDistDir)
    ant.copy(todir: jsmoothDistDir) {
        fileset(dir: installerWorkDir)
    }

    // generate a zip
    ant.zip(basedir: jsmoothDistDir,
            destfile: "${jsmoothDistDir}/${griffonAppName}-windows-${griffonAppVersion}.zip")

    event("CreatePackageEnd", ['jsmooth'])
}
setDefaultTarget(createPackageJsmooth)
