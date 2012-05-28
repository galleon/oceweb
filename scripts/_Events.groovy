eventCreateWarStart = {warName, File stagingDir ->
    Process process = "git log -1".execute()
    File file = new File("${stagingDir}/versionInfo.txt")
    file.text = process.getText()
    println "Adding version info at ${stagingDir}/versionInfo.txt"
}