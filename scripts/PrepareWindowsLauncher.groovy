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
 * Gant script that prepares a Windows launcher.  This script just defers to the JSmooth launcher scripts.
 *
 * @author Andres Almiray
 * @author Josh Reed
 *
 * @since 0.4
 */

includeTargets << griffonScript("_GriffonInit")
includeTargets << pluginScript("installer", "PrepareJsmoothLauncher")

target(prepareWindowsLauncher: "Prepares a Windows launcher") {
    event("PrepareWindowsLauncherStart", [])
	prepareJsmoothLauncher()
    event("PrepareWindowsLauncherEnd", [])
}

setDefaultTarget(prepareWindowsLauncher)