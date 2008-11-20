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
 * Base script for installer scripts
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

Ant.property(environment:"env")
griffonHome = Ant.antProject.properties."env.GRIFFON_HOME"

defaultTarget("Prepare Binary") {
    depends(checkVersion, packageApp, classpath)
    prepareBinary()
}

includeTargets << griffonScript("Package")

installerPluginBase = getPluginDirForName('installer').file as String
// These will be set by caller script
// installerWorkDir = "${basedir}/installer/izpack"
// binaryDir = installerWorkDir + "/binary"

target(prepareBinary: "") {
    packageApp()

    Ant.mkdir( dir: "${binaryDir}" )
    Ant.mkdir( dir: "${binaryDir}/lib" )
    Ant.mkdir( dir: "${binaryDir}/bin" )

    Ant.copy( todir: "${binaryDir}/bin" ) {
        fileset( dir: "${installerPluginBase}/src/templates/bin" )
    }
    Ant.replace( dir: "${binaryDir}/bin" ) {
        replacefilter( token: "@app.name@", value:"${griffonAppName}" )
        replacefilter( token: "@app.version@", value:"${griffonAppVersion}" )
    }
    Ant.move( file: "${binaryDir}/bin/app.run", tofile: "${binaryDir}/bin/${griffonAppName}" )
    Ant.move( file: "${binaryDir}/bin/app.run.bat", tofile: "${binaryDir}/bin/${griffonAppName}.bat" )

    Ant.copy( todir: "${binaryDir}/lib" ) {
        fileset( dir: "${basedir}/staging", includes : "*.jar" )
    }
}

target(test_is_linux: "" ) {
    Ant.condition( property: "os.isLinux", value: true ) {
        and {
            os( family: "unix" )
            and { os( name: "Linux" ) }
        }
    }
    Ant.fail( message: "You are not running on Linux", unless:"os.isLinux" )
    Ant.echo( message: "You are running ${os.name} ${os.arch} ${os.version}" )
}

target(test_is_osx: "" ) {
    Ant.condition( property: "os.isOSX", value: true ) {
        and {
            os( family: "mac" )
            and { os( family: "unix" ) }
        }
    }
    Ant.fail( message: "You are not running on MacOSX", unless: "os.isOSX" )
    Ant.echo( message: "You are running ${os.name} ${os.arch} ${os.version}" )
}