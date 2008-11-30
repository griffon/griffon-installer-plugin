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
 * Gant script that prepares an RPM based installer
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

Ant.property(environment:"env")
griffonHome = Ant.antProject.properties."env.GRIFFON_HOME"

defaultTarget("Prepare RPM installer") {
    depends( text_is_linux )
    prepareRPMInstaller()
}

includeTargets << griffonScript("Init")
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << loadScript("${installerPluginBase}/scripts/_PrepareInstaller")

target(prepareRPMInstaller: "Prepares an RPM installer") {
    event( "PrepareRpmInstallerStart", [] )

    installerWorkDir = "${basedir}/installer/rpm"
    Ant.mkdir( dir: "${installerWorkDir}/BUILD" )
    Ant.mkdir( dir: "${installerWorkDir}/SOURCES" )
    Ant.mkdir( dir: "${installerWorkDir}/SPECS" )
    Ant.mkdir( dir: "${installerWorkDir}/SRPMS" )
    Ant.mkdir( dir: "${installerWorkDir}/RPMS/noarch" )
    binaryDir = "${installerWorkDir}/${griffonAppName}-${griffonAppVersion}"
    Ant.mkdir( dir: binaryDir )

    prepareBinary()

    Ant.copy( todir: "${installerWorkDir}/SPECS" ) {
        fileset( dir: "${installerPluginBase}/src/templates/rpm" )
    }
    Ant.replace( dir: "${installerWorkDir}/SPECS" ) {
        replacefilter( token: "@app.name@", value:"${griffonAppName}" )
        replacefilter( token: "@app.version@", value:"${griffonAppVersion}" )
    }
    Ant.move( file: "${installerWorkDir}/SPECS/app.spec",
              tofile: "${installerWorkDir}/SPECS/${griffonAppName}.spec" )
    Ant.zip( destfile: "${installerWorkDir}/SOURCES/${griffonAppName}-${griffonAppVersion}-bin.zip",
             basedir: installerWorkDir,
             includes: "${griffonAppName}-${griffonAppVersion}/**/*" )

    event( "PrepareRpmInstallerEnd", [] )
}
