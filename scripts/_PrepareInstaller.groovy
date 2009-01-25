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

ant.property(environment:"env")
griffonHome = ant.antProject.properties."env.GRIFFON_HOME"


includeTargets << griffonScript("_GriffonPackage")
installerPluginBase = getPluginDirForName('installer').file as String
// These will be set by caller script
// installerWorkDir = "${basedir}/installer/izpack"
// binaryDir = installerWorkDir + "/binary"

target(prepareBinary: "") {
    depends(checkVersion, packageApp, classpath)

    packageApp()

    ant.mkdir( dir: "${binaryDir}" )
    ant.mkdir( dir: "${binaryDir}/lib" )
    ant.mkdir( dir: "${binaryDir}/bin" )

    ant.copy( todir: "${binaryDir}/bin" ) {
        fileset( dir: "${installerPluginBase}/src/templates/bin" )
    }
    ant.replace( dir: "${binaryDir}/bin" ) {
        replacefilter( token: "@app.name@", value:"${griffonAppName}" )
        replacefilter( token: "@app.version@", value:"${griffonAppVersion}" )
    }
    ant.move( file: "${binaryDir}/bin/app.run", tofile: "${binaryDir}/bin/${griffonAppName}" )
    ant.move( file: "${binaryDir}/bin/app.run.bat", tofile: "${binaryDir}/bin/${griffonAppName}.bat" )

    ant.copy( todir: "${binaryDir}/lib" ) {
        fileset( dir: "${basedir}/staging", excludes: "*.jnlp,applet.html,griffon.jpeg" )
    }
}

target(test_is_linux: "" ) {
    ant.condition( property: "os.isLinux", value: true ) {
        and {
            os( family: "unix" )
            and { os( name: "Linux" ) }
        }
    }
    ant.fail( message: "You are not running on Linux", unless:"os.isLinux" )
    ant.echo( message: "You are running ${ant.properties.'os.name'} ${ant.properties.'os.arch'} ${ant.properties.'os.version'}" )
}

target(test_is_osx: "" ) {
    ant.condition( property: "os.isOSX", value: true ) {
        and {
            os( family: "mac" )
            and { os( family: "unix" ) }
        }
    }
    ant.fail( message: "You are not running on MacOSX", unless: "os.isOSX" )
    ant.echo( message: "You are running ${ant.properties.'os.name'} ${ant.properties.'os.arch'} ${ant.properties.'os.version'}" )
}

// setDefaultTarget(prepareBinary)