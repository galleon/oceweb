eventCreateWarStart = {warName, File stagingDir ->
    Process process = "git log -1".execute()
    File file = new File("${stagingDir.path}/currentVersion.txt")
    file.text = process.getText()
    println "Adding version info at versionInfo.txt ${stagingDir.path}"
}