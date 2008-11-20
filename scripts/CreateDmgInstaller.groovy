/*
 * Copyright 2008 the original author or authors.
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
 * Gant script that creates an DMG based installer
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

Ant.property(environment:"env")
griffonHome = Ant.antProject.properties."env.GRIFFON_HOME"

defaultTarget("Create DMG installer") {
    depends(checkVersion, classpath, text_is_osx)
    def src = new File( installerWorkDir )
    if( src && src.list() ) {
        createDMGInstaller()
    } else {
        println """No DMG installer sources were found.
Make sure you call 'griffon prepare-dmg-installer' first
and configure the files appropriately.
"""
    }
}

includeTargets << griffonScript("Init")
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << loadScript("${installerPluginBase}/scripts/_PrepareInstaller")

installerWorkDir = "${basedir}/installer/dmg"

target(createDMGInstaller: "Creates a DMG installer") {
    Ant.exec( executable: "hdiutil" ) {
       arg( line:"create -srcfolder ${installerWorkDir} ${installerWorkDir}/${griffonAppName}-${griffonAppVersion}.dmg" />
    }
}