class InstallerGriffonPlugin {
    def version = '0.3'
    def canBeGlobal = true
    def dependsOn = [:]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "Creates installers for your Griffon application"
    def description = '''\
Creates installers for your Griffon application.
Supported formats are:
 - IzPack (universal)
 - RPM (Linux)
 - DMG (MacOSX)
 - JSmooth (Windows)
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Installer+Plugin"
}
