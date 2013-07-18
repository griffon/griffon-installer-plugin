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
 * Gant script that prepares a Mac application bundle.
 *
 * @author Andres Almiray
 * @author Josh Reed
 *
 * @since 0.4
 */

installerPluginBase = getPluginDirForName('installer').file as String
includePluginScript('installer','_Prepare')

target(name: 'preparePackageMac', description: '', prehook: null, posthook: null) {
    event("PreparePackageStart", ['mac'])

    installerWorkDir = "${projectWorkDir}/installer/mac"
    binaryDir = installerWorkDir

    ant.mkdir(dir: installerWorkDir)
    //ant.copy(file:"${installerPluginBase}/src/templates/jarbundler/JavaApplicationStub", tofile: "${binaryDir}/${griffonAppName}")
    ant.exec(executable: "cp"){
        arg(value: "-p")
        arg(value: "${installerPluginBase}/src/templates/jarbundler/JavaApplicationStub")
        arg(value: "${binaryDir}/${griffonAppName}")
    }

    File applicationTemplates = new File("${basedir}/src/installer/mac")
    if (applicationTemplates.exists()) {
        ant.copy(todir: installerWorkDir, overwrite: true) {
            fileset(dir: applicationTemplates)
        }
    }

    event("PreparePackageEnd", ['mac'])
}
setDefaultTarget(preparePackageMac)
