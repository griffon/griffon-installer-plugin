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
 * @author Andres Almiray
 * @author Josh Reed
 */
class InstallerGriffonPlugin {
    def version = '0.5.1'
    def dependsOn = [:]
    def griffonVersion = '0.9 > *'
    def license = 'Apache Software License 2.0'

    def author = "Andres Almiray, Josh Reed"
    def authorEmail = "aalmiray@users.sourceforge.net, jareed@andrill.org"
    def title = "Creates launchers and installers for your Griffon application"
    def description = '''
Creates launchers and installers for your Griffon application.
Supported formats are:
 - IzPack (universal)
 - RPM (Linux)
 - AppBundle (MacOSX)
 - JSmooth (Windows)
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Installer+Plugin"
}
