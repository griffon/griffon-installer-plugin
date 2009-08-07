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
 * Gant script that prepares a Linux launcher.
 *
 * @author Josh Reed
 *
 * @since 0.4
 */

includeTargets << griffonScript("_GriffonInit")
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << pluginScript("installer","_PrepareInstaller")

installerWorkDir = "${basedir}/installer/linux"
binaryDir = installerWorkDir

target(prepareLinuxLauncher: "Prepares a Linux launcher") {
    event("PrepareLinuxLauncherStart", [])

    ant.mkdir(dir: installerWorkDir)

//     ant.copy(file:"${installerPluginBase}/src/templates/bin/app.run", tofile: "${binaryDir}/${griffonAppName}")
//     ant.copy(file:"${installerPluginBase}/src/templates/bin/startApp", tofile: "${binaryDir}/startApp")
//     ant.replace(dir: "${binaryDir}") {
//         replacefilter( token: "@app.name@", value:"${griffonAppName}" )
//         replacefilter( token: "@app.version@", value:"${griffonAppVersion}" )
//     }

    event("PrepareLinuxLauncherEnd", [])
}

setDefaultTarget(prepareLinuxLauncher)