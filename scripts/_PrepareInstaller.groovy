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
 * Base script for installer scripts
 *
 * @author Andres Almiray
 *
 * @since 0.1
 */

ant.property(environment:"env")
griffonHome = ant.antProject.properties."env.GRIFFON_HOME"

installerPluginBase = getPluginDirForName('installer').file as String
// These will be set by caller script
// installerWorkDir = "${basedir}/installer/izpack"
// binaryDir = installerWorkDir + "/binary"
appMainClass = "griffon.application.SingleFrameApplication"

target(prepareDirectories: "") {
    depends(checkVersion)

    ant.mkdir(dir: installerWorkDir)
    ant.mkdir(dir: binaryDir)
    ant.mkdir(dir: "${binaryDir}/lib")
    ant.mkdir(dir: "${binaryDir}/bin")
    ant.mkdir(dir: "${binaryDir}/icons")
}

target(test_is_linux: "") {
    ant.condition(property: "os.isLinux", value: true) {
        and {
            os( family: "unix" )
            and { os( name: "Linux" ) }
        }
    }
    ant.fail(message: "You are not running on Linux", unless: "os.isLinux")
    ant.echo(message: "You are running ${ant.properties.'os.name'} ${ant.properties.'os.arch'} ${ant.properties.'os.version'}")
}

target(test_is_osx: "") {
    ant.condition(property: "os.isOSX", value: true) {
        and {
            os( family: "mac" )
            and { os( family: "unix" ) }
        }
    }
    ant.fail(message: "You are not running on MacOSX", unless: "os.isOSX")
    ant.echo(message: "You are running ${ant.properties.'os.name'} ${ant.properties.'os.arch'} ${ant.properties.'os.version'}")
}
