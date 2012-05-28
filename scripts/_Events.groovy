eventCreateWarStart = {warName, File stagingDir ->
    Process process = "git log -1".execute()
    File file = new File("versionInfo.txt")
    file.text = process.getText()
    println "Adding version info at versionInfo.txt"
}