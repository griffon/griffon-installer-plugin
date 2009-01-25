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

ant.property(environment:"env")
griffonHome = ant.antProject.properties."env.GRIFFON_HOME"

includeTargets << griffonScript("_GriffonPackage")
installerPluginBase = getPluginDirForName("installer").file as String
installerWorkDir = "${basedir}/installer/jsmooth"
skeletonsDir = installerPluginBase + "/src/templates/jsmooth/skeletons"
binaryDir = installerWorkDir

ant.path( id : 'installerJarSet' ) {
    fileset( dir: "${installerPluginBase}/lib/installer", includes : "*.jar" )
}

ant.taskdef( name: "jsmoothgen",
             classname: "net.charabia.jsmoothgen.ant.JSmoothGen",
             classpathref: "installerJarSet" )

target(jsmoothSanityCheck:"") {
    depends(checkVersion, classpath, createStructure)
    def src = new File( installerWorkDir )
    if( src && src.list() ) {
        createJsmoothLauncher()
    } else {
        println """No Jsmooth launcher sources were found.
Make sure you call 'griffon prepare-jsmooth-launcher' first
and configure the files appropriately.
"""
    }
}

target(createJsmoothLauncher: "Creates a Jsmooth launcher") {
    jardir = "${basedir}/staging" // TODO replace with correct build property
    def jarlist = []
    new File(jardir).eachFileMatch(~/.*\.jar/) { f ->
        jarlist << "<classPath>lib/${f.name}</classPath>"
    }
    ant.replace( dir: binaryDir, includes: "*.jsmooth" ) {
        replacefilter( token: "@app.jars@", value: jarlist.join('\n') )
    }
    ant.jsmoothgen( project: "${installerWorkDir}/${griffonAppName}.jsmooth",
                    skeletonroot: skeletonsDir )
    def zipfile = "${installerWorkDir}/${griffonAppName}-windows-${griffonAppVersion}.zip"
    ant.delete(file: zipfile, quiet: true, failOnError: false)
    ant.zip( basedir: installerWorkDir,
             destfile: zipfile )
}

setDefaultTarget(jsmoothSanityCheck)