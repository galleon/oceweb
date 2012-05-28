eventCreateWarStart = {warName, File stagingDir ->
    Process process = "git log -1".execute()
    String versionNumber = ''
    process.getText().eachLine{
      if(it.startsWith('commit')){
        versionNumber = it.tokenize(' ').last()
      }
    }
    println "Version number of application is ${versionNumber}"
}