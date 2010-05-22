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
 * Gant script that creates an RPM based installer
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

includeTargets << griffonScript("_GriffonInit")
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << pluginScript("installer","_CreateLauncher")

target('default': "Creates an RPM installer") {
    createRpmLauncher()
}

target(rpmSanityCheck:"") {
    depends(checkVersion, classpath)//, test_is_linux)

	installerWorkDir = "${projectTargetDir}/installer/rpm"
	binaryDir = "${installerWorkDir}/${griffonAppName}-${griffonAppVersion}"

    def src = new File(installerWorkDir)
    if(!src.list() ) {
        println """No RPM installer sources were found.
Make sure you call 'griffon prepare-rpm-installer' first
and configure the files appropriately.
"""
        System.exit(1)
    }
}

target(createRpmLauncher: "Creates an RPM installer") {
    depends(rpmSanityCheck, copyAllAppArtifacts)

    event("CreateRpmLauncherStart", [])

    ant.zip(destfile: "${installerWorkDir}/SOURCES/${griffonAppName}-${griffonAppVersion}-bin.zip",
            basedir: installerWorkDir,
            includes: "${griffonAppName}-${griffonAppVersion}/**/*" )

    ant.rpm(specFile: "${griffonAppName}.spec",
            topDir: installerWorkDir,
            cleanBuildDir: "false",
            failOnError: "true" )

    event("CreateRpmLauncherEnd", [])
}