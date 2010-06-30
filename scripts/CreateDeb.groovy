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
 * Gant script that creates a .deb package
 *
 * @author Andres Almiray
 *
 * @since 0.5
 */

includeTargets << griffonScript("Init")
installerPluginBase = getPluginDirForName('installer').file as String
includePluginScript("installer","_Create")

ant.path(id : "installerJarSet") {
    fileset(dir: "${installerPluginBase}/lib/installer", includes : "*.jar")
}

ant.taskdef(resource: "ant_deb_task.properties",
            classpathref: "installerJarSet")

target('debSanityCheck':'') {    
    depends(checkVersion, classpath)

    packageType = 'deb'
    installerWorkDir = "${projectTargetDir}/installer/deb"
    binaryDir = installerWorkDir + '/binary'
    installerResourcesdir = installerWorkDir + '/resources'

    def src = new File(installerWorkDir)
    if(!src.list()) {
        println """No .deb package sources were found.
Make sure you call 'griffon prepare-deb-installer' first
and configure the files appropriately.
"""
        System.exit(1)
    }
}

target(createPackageDeb: "Creates a .deb package") {
    depends(debSanityCheck, copyAllAppArtifacts)

    event("CreatePackageStart", ['deb'])

    ant.replace(dir: installerResourcesdir, includes: '*.properties') {
        replacefilter(token: '@app.name@', value: griffonAppName)
        replacefilter(token: '@app.version@', value: griffonAppVersion)
        replacefilter(token: '@app.author@', value: 'Griffon')
        replacefilter(token: '@app.author.email@', value: 'user@griffon.codehaus.org')
        replacefilter(token: '@app.synopsis@', value: "${griffonAppName}-${griffonAppVersion}")
        replacefilter(token: '@app.description@', value: 'Unknown')
        replacefilter(token: '@app.depends@', value: 'sun-java5-jre | sun-java6-jre')
    }
    ant.property(file: "${installerResourcesdir}/deb_settings.properties")

    def packageName = ant.antProject.properties."deb.package.name".toLowerCase()
    if(packageName.size() < 2 || !(packageName ==~ /^[a-z0-9][a-z0-9\.\-\+]*$/)) {
        println """Illegal package name: $packageName
According to http://www.debian.org/doc/debian-policy/ch-controlfields.html#s-f-Source
'Package names (both source and binary, see Package, Section 5.6.7) must consist only of lower case letters (a-z), digits (0-9), plus (+) and minus (-) signs, and periods (.). They must be at least two characters long and must start with an alphanumeric character.'

Please update the name of your application with the aforementioned guidelines by editting application.properties and changing the value of 'app.name'. As an alternative edit the file ${installerResourcesDir}/deb_settings.properties only.
"""
    }

    ant.deb(todir: installerWorkDir,
            'package': packageName,
            section: ant.antProject.properties."deb.section",
            depends: ant.antProject.properties."deb.depends") {
        version(upstream: ant.antProject.properties."deb.version")
        maintainer(name: ant.antProject.properties."deb.maintainer.name",
                   email: ant.antProject.properties."deb.maintainer.email")
        description(ant.antProject.properties."deb.description",
                    synopsis: ant.antProject.properties."deb.synopsis")
        tarfileset(dir: binaryDir, prefix: "usr/share/${packageName}") {
            exclude(name: 'bin/**')
            exclude(name: '*.icns')
        }
        tarfileset(dir: "${binaryDir}/bin", prefix: "usr/share/${packageName}/bin", filemode: '755') {
            exclude(name: '*.bat')
        }
    }

    def debDistDir = distDir + '/deb'
    ant.mkdir(dir: debDistDir)
    ant.copy(todir: debDistDir) {
       fileset(dir: installerWorkDir, includes: '*.deb')
    }
            
    event("CreatePackageEnd", ['deb'])
}
setDefaultTarget(createPackageDeb)
