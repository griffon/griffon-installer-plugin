/*
 * Copyright 2008-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Gant script that prepares an IzPack based installer
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

installerPluginBase = getPluginDirForName('installer').file as String
includePluginScript('installer','_Prepare')

target(name: 'preparePackageIzpack', description: '', prehook: null, posthook: null) {
    event('PreparePackageStart', ['izpack'])

    installerWorkDir = "${projectWorkDir}/installer/izpack"
    binaryDir = installerWorkDir + '/binary'
    installerResourcesDir = installerWorkDir + '/resources'

    prepareDirectories()
    ant.mkdir(dir: installerResourcesDir)

    ant.copy(todir: installerResourcesDir) {
        fileset(dir: "${installerPluginBase}/src/templates/izpack")
    }

    File applicationTemplates = new File("${basedir}/src/installer/izpack")
    if (applicationTemplates.exists()) {
        ant.copy(todir: installerWorkDir, overwrite: true) {
            fileset(dir: applicationTemplates)
        }
    }

    event('PreparePackageEnd', ['izpack'])
}
setDefaultTarget(preparePackageIzpack)
