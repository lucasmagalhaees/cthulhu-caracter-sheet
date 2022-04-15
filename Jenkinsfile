pipeline {

agent any
stages {
    stage("Compile"){
      steps {
        sh "chmod +x ./gradlew clean build"
      }
    }
}
}