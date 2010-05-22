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
	['Izpack', 'Rpm', 'Mac', 'Windows'] .each { type ->
	    includePluginScript('installer', 'Prepare'+ type +'Launcher')
	    includePluginScript('installer', 'Create'+ type +'Launcher')
	}
}

eventCleanEnd = {
    ant.delete(dir: "${projectTargetDir}/installer/")
}

eventMakePackage = { type ->
    switch(type.toUpperCase()) {
	    case 'IZPACK':
	    case 'UNIVERSAL':
	        packageLauncher('Izpack')
	        break
	    case 'MAC':
		case 'DMG':
		    packageLauncher('Mac')
			break
	    case 'RPM':
		    packageLauncher('Rpm')
			break
	    case 'WINDOWS':
		case 'JSMOOTH':
		    packageLauncher('Windows')
			break
    }
}

packageLauncher = { type ->
	includeTargets.binding.with {
        getVariable("prepare${type}Launcher")()
        getVariable("create${type}Launcher")()
    }
}