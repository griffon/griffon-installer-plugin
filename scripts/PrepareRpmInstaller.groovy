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

ant.property(environment:"env")
griffonHome = ant.antProject.properties."env.GRIFFON_HOME"

includeTargets << griffonScript("_GriffonInit")
installerPluginBase = getPluginDirForName('installer').file as String
includeTargets << pluginScript("installer","_PrepareInstaller")

target(prepareRPMInstaller: "Prepares an RPM installer") {
    depends( test_is_linux )
    event( "PrepareRpmInstallerStart", [] )

    installerWorkDir = "${basedir}/installer/rpm"
    ant.mkdir( dir: "${installerWorkDir}/BUILD" )
    ant.mkdir( dir: "${installerWorkDir}/SOURCES" )
    ant.mkdir( dir: "${installerWorkDir}/SPECS" )
    ant.mkdir( dir: "${installerWorkDir}/SRPMS" )
    ant.mkdir( dir: "${installerWorkDir}/RPMS/noarch" )
    binaryDir = "${installerWorkDir}/${griffonAppName}-${griffonAppVersion}"
    ant.mkdir( dir: binaryDir )

    prepareBinary()

    ant.copy( todir: "${installerWorkDir}/SPECS" ) {
        fileset( dir: "${installerPluginBase}/src/templates/rpm" )
    }
    ant.replace( dir: "${installerWorkDir}/SPECS" ) {
        replacefilter( token: "@app.name@", value:"${griffonAppName}" )
        replacefilter( token: "@app.version@", value:"${griffonAppVersion}" )
    }
    ant.move( file: "${installerWorkDir}/SPECS/app.spec",
              tofile: "${installerWorkDir}/SPECS/${griffonAppName}.spec" )
    ant.zip( destfile: "${installerWorkDir}/SOURCES/${griffonAppName}-${griffonAppVersion}-bin.zip",
             basedir: installerWorkDir,
             includes: "${griffonAppName}-${griffonAppVersion}/**/*" )

    event( "PrepareRpmInstallerEnd", [] )
}

setDefaultTarget(prepareRPMInstaller)