eventCleanEnd = {
	// jar
	ant.delete(dir:"${basedir}/installer/jar/dist", failonerror:false)
	ant.delete(dir:"${basedir}/installer/jar/classes", failonerror:false)
	
	// jsmooth
	ant.delete(dir:"${basedir}/installer/jsmooth/dist", failonerror:false)
	
	// linux
	ant.delete(dir:"${basedir}/installer/linux/dist", failonerror:false)
	
	// mac
	ant.delete(dir:"${basedir}/installer/mac/dist", failonerror:false)
	
	// windows
	ant.delete(dir:"${basedir}/installer/windows/dist", failonerror:false)
	
	// TODO handle izpack and rpm
}
