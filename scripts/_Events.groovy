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
 * @author Josh Reed
 * @author Andres Almiray
 */

if(!isPluginProject) {    
    includeTargets << griffonScript('_PluginDependencies')
    ['Izpack', 'Rpm', 'Deb', 'Mac', 'Windows'] .each { type ->
        includePluginScript('installer', 'Prepare'+ type)
        includePluginScript('installer', 'Create'+ type)
    }
}

eventCleanEnd = {
    ant.delete(dir: "${projectTargetDir}/installer/")
}

eventMakePackage = { type ->
    switch(type.toUpperCase()) {
        case 'IZPACK':
        case 'UNIVERSAL':
            buildPackage('Izpack')
            break
        case 'MAC':
        case 'DMG':
            buildPackage('Mac')
            break
        case 'RPM':
            buildPackage('Rpm')
            break
        case 'DEB':
            buildPackage('Deb')
            break
        case 'WINDOWS':
        case 'JSMOOTH':
            buildPackage('Windows')
            break
    }
}

buildPackage = { type ->
    includeTargets.binding.with {
        getVariable("prepare${type}")()
        getVariable("create${type}")()
    }
}
