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
 * Gant script that prepares a .deb package
 *
 * @author Andres Almiray
 *
 * @since 0.5
 */

includeTargets << griffonScript('_GriffonInit')
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << pluginScript('installer','_Prepare')

target('default': 'Prepares a .deb package') {
    prepareDeb()
}
    
target('prepareDeb': 'Prepares a .deb package') {
    event('PreparePackageStart', ['deb'])

    installerWorkDir = "${projectTargetDir}/installer/deb"
    binaryDir = installerWorkDir + '/binary'
    installerResourcesDir = installerWorkDir + '/resources'

    ant.copy(todir: installerResourcesDir) {
        fileset(dir: "${installerPluginBase}/src/templates/deb")
    }

    prepareDirectories()
    event('PreparePackageEnd', ['deb'])
}
