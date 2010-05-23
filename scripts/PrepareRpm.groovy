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
 * Gant script that prepares an RPM based installer
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

includeTargets << griffonScript("_GriffonInit")
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << pluginScript("installer","_Prepare")

target('default': "Prepares an RPM installer") {
    prepareRpm()
}

target(prepareRpm: "Prepares an RPM installer") {
//    depends(test_is_linux)
    event("PrepareRpmStart", [])

    installerWorkDir = "${projectTargetDir}/installer/rpm"
    binaryDir = "${installerWorkDir}/${griffonAppName}-${griffonAppVersion}"

    ant.mkdir(dir: "${installerWorkDir}/BUILD")
    ant.mkdir(dir: "${installerWorkDir}/SOURCES")
    ant.mkdir(dir: "${installerWorkDir}/SPECS")
    ant.mkdir(dir: "${installerWorkDir}/SRPMS")
    ant.mkdir(dir: "${installerWorkDir}/RPMS/noarch")

    prepareDirectories()

    ant.copy(todir: "${installerWorkDir}/SPECS") {
        fileset(dir: "${installerPluginBase}/src/templates/rpm")
    }
    ant.move(file: "${installerWorkDir}/SPECS/app.spec",
              tofile: "${installerWorkDir}/SPECS/${griffonAppName}.spec")

    event("PrepareRpmEnd", [])
}
