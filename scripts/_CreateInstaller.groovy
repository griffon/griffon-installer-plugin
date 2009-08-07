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
 * Base script for installer scripts
 *
 * @author Andres Almiray
 *
 * @since 0.4
 */

import static griffon.util.GriffonApplicationUtils.isMacOSX

includeTargets << griffonScript("_GriffonPackage")
includeTargets << pluginScript("installer","_PrepareInstaller")

target(copyAllAppArtifacts: "") {
   depends(checkVersion, packageApp)
   copyLaunchScripts()
   copyAppLibs()
   copyAppResources()
}

target(copyAppLibs: "") {
    ant.copy( todir: "${binaryDir}/lib" ) {
        fileset( dir: "${basedir}/staging", includes: "*.jar" )
    }
}

target(copyAppResources: "") {
    ant.copy( todir: "${binaryDir}/icons" ) {
        fileset( dir: "${basedir}/griffon-app/resources/", includes: "griffon-icon*" )
    }
}

target(copyLaunchScripts: "") {
    def javaOpts = []
    if (config.griffon.memory?.min) {
        javaOpts << "-Xms$config.griffon.memory.min"
    }
    if (config.griffon.memory?.max) {
        javaOpts << "-Xmx$config.griffon.memory.max"
    }
    if (config.griffon.memory?.maxPermSize) {
        javaOpts << "-XX:maxPermSize=$config.griffon.memory.maxPermSize"
    }
    if (isMacOSX) {
        javaOpts << "-Xdock:name=$griffonAppName"
        javaOpts << "-Xdock:icon=${griffonHome}/bin/griffon.icns"
    }
    if (config.griffon.app?.javaOpts) {
      config.griffon.app?.javaOpts.each { javaOpts << it }
    }
    javaOpts = javaOpts ? javaOpts.join(' ') : ""

    ant.copy( todir: "${binaryDir}/bin" ) {
        fileset( dir: "${installerPluginBase}/src/templates/bin" )
    }
    ant.replace( dir: "${binaryDir}/bin" ) {
        replacefilter(token: "@app.name@", value: griffonAppName)
        replacefilter(token: "@app.version@", value: griffonAppVersion)
        replacefilter(token: "@app.java.opts@", value: javaOpts)
        replacefilter(token: "@app.main.class@", value: appMainClass)
    }
    ant.move( file: "${binaryDir}/bin/app.run", tofile: "${binaryDir}/bin/${griffonAppName}" )
    ant.move( file: "${binaryDir}/bin/app.run.bat", tofile: "${binaryDir}/bin/${griffonAppName}.bat" )
}