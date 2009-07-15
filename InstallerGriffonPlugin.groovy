class InstallerGriffonPlugin {
    def version = '0.4'
    def canBeGlobal = true
    def dependsOn = [:]

    def author = "Andres Almiray, Josh Reed"
    def authorEmail = "aalmiray@users.sourceforge.net, jareed@andrill.org"
    def title = "Creates launchers and installers for your Griffon application"
    def description = '''\
Creates launchers and installers for your Griffon application.
Supported formats are:
 - IzPack (universal)
 - RPM (Linux)
 - DMG (MacOSX)
 - JSmooth (Windows)
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Installer+Plugin"
}
