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

includeTargets << griffonScript("Init")
installerPluginBase = getPluginDirForName('installer').file as String
includePluginScript("installer","_Create")

ant.path(id : "installerJarSet") {
    fileset(dir: "${installerPluginBase}/lib/installer", includes : "*.jar")
}

ant.taskdef(name: "izpack",
            classname: "com.izforge.izpack.ant.IzPackTask",
            classpathref: "installerJarSet")

target('izpackSanityCheck':'') {    
    depends(checkVersion, classpath)

    packageType = 'izpack'
    installerWorkDir = "${projectWorkDir}/installer/izpack"
    binaryDir = installerWorkDir + '/binary'
    installerResourcesDir = installerWorkDir + '/resources'

    def src = new File(installerWorkDir)
    if(!src.list()) {
        println """No IzPack installer sources were found.
Make sure you call 'griffon prepare-izpack-installer' first
and configure the files appropriately.
"""
        System.exit(1)
    }
}

target(createPackageIzpack: "Creates an IzPack installer") {
    depends(izpackSanityCheck, copyAllAppArtifacts)

    event("CreatePackageStart", ['izpack'])

    ant.replace(dir: installerResourcesDir, includes: '*.xml,*.html,*.txt,*properties') {
        replacefilter(token: '@app.name@', value: griffonAppName)
        replacefilter(token: '@app.version@', value: griffonAppVersion)
        replacefilter(token: '@app.author@', value: 'Griffon')
        replacefilter(token: '@app.author.email@', value: 'user@griffon.codehaus.org')
        replacefilter(token: '@app.url@', value: 'http://griffon.codehaus.org')
    }

    ant.mkdir(dir: distDir + "/izpack")
    ant.izpack(basedir: installerWorkDir,
               input: "${installerWorkDir}/resources/installer.xml",
               output: "${distDir}/izpack/${griffonAppName}-${griffonAppVersion}-installer.jar",
               compression: "deflate",
               compressionLevel: "9")
            
    event("CreatePackageEnd", ['izpack'])
}
setDefaultTarget(createPackageIzpack)
