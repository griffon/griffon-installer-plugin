class InstallerGriffonPlugin {
    def version = 'trunk'
    def canBeGlobal = true
    def dependsOn = [:]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "Allows creating installers for your Griffon application"
    def description = '''\
Allows creating installers for your Griffon application.
Supported formats are:
 - IzPack (universal)
 - RPM (Linux)
 - DEB (Linux)
 - DMG (MacOSX)
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Installer+Plugin"
}