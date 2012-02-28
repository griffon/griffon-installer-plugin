/*
 * Copyright 2009-2012 the original author or authors.
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
    // the plugin version
    String version = '0.6'
    // the version or versions of Griffon the plugin is designed for
    String griffonVersion = '0.9.5 > *'
    // the other plugins this plugin depends on
    Map dependsOn = [:]
    // resources that are included in plugin packaging
    List pluginIncludes = []
    // the plugin license
    String license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    List toolkits = []
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    List platforms = []
    // URL where documentation can be found
    String documentation = ''
    // URL where source can be found
    String source = 'https://github.com/griffon/griffon-installer-plugin'

    List authors = [
        [
            name: 'Andres Almiray',
            email: 'aalmiray@yahoo.com'
        ],
        [
            name: 'Josh Reed',
            email: 'jareed@andrill.org'
        ]
    ]
    String title = 'Creates launchers and installers for your Griffon application'
    // accepts Markdown syntax. See http://daringfireball.net/projects/markdown/ for details
    String description = '''
The Installer plugin provides useful packaging and installation solutions for your Griffon application.
The plugin creates two types of artifacts, launchers and installers. Launchers package your application
in platform-specific ways, such as an .exe file for Windows and a .app bundle for Mac. 
Installers install your packaged app onto the user's computer. The current supported options are:

 **Launchers**

 * Windows (JSmooth) - creates a .exe for launching your application on Windows
 * Mac - creates a Mac application bundle (.app) for launching your application on Mac OS X

 **Installers**

 * IzPack - platform independent installer, highly configurable.
 * RPM - RedHat Package Manager based installer, works on RedHat, Fedora, CentOS, Mandriva.
 * DEB - Debian based package installer, works on Debian, Ubuntu.
 * DMG - MacOSX disk image

Usage
-----

Creating a launcher or installer is a two step process. First you prepare the configuration files for that
launcher/installer by running the prepare-XXX script. This will copy the standard configuration files into
the installer/XXX directory so you can tweak them as needed. This step only needs to be performed once for
the application. The second step is to actually create the launcher/installer by running the create-XXX script.
This will create the actual launcher or installer in the installer/XXX/dist directory. This step should be
performed every time you want to release a new version of your application.

The provided scripts are:

 * **prepare-deb** / **create-deb**
 * **prepare-rpm** / **create-rpm**
 * **prepare-izpack** / **create-izpack**
 * **prepare-mac** / **create-mac**
 * **prepare-jsmooth** / **create-jsmooth**
 * **prepare-windows** / **create-windows **// delegates to jsmooth

All these targets can be called using the standard package command too, for example

    griffon package izpack

produces the same result as

    griffon prepare-izpack
    griffon create-izpack

Notes
-----

Preparing and creating the launchers is cross-platform, e.g. you can 'create-windows' even if you are developing
on a Linux or Mac machine (and vice-versa). Building an DMG requires running MacOSX.
'''
}
