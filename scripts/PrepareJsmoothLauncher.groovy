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
 * Gant script that prepares an JSmooth-based installer
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

includeTargets << griffonScript("_GriffonInit")
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << pluginScript("installer","_PrepareLauncher")

target('default': "Prepares a JSmooth-based launcher") {
    prepareJsmoothLauncher()
}

target(prepareJsmoothLauncher: "Prepares a JSmooth-based launcher") {
    event("PrepareJsmoothLauncherStart", [])

	installerWorkDir = "${projectTargetDir}/installer/jsmooth"
	binaryDir = installerWorkDir
	installerPluginBase = getPluginDirForName('installer').file as String
	
    ant.copy(file:"${installerPluginBase}/src/templates/jsmooth/app.jsmooth", tofile: "${binaryDir}/${griffonAppName}.jsmooth")
    ant.copy(file:"${installerPluginBase}/src/templates/jsmooth/griffon-icon-32x32.png", tofile: "${binaryDir}/${griffonAppName}-icon.png")
    ant.copy(file:"${installerPluginBase}/src/templates/jsmooth/msvcr71.dll", tofile: "${binaryDir}/msvcr71.dll")

    ant.replace(dir: binaryDir, includes: "*.jsmooth") {
        replacefilter(token: "@app.name@", value:"${griffonAppName}")
        replacefilter(token: "@app.version@", value:"${griffonAppVersion}")
    }

    event("PrepareJsmoothLauncherEnd", [])
}