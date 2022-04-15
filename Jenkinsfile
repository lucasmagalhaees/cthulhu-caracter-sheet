pipeline {

agent any
stages {
    stage("Compile"){
      steps {
        echo 'Compile project'
        sh "chmod +x gradlew"
        sh "./gradlew clean build"
      }
    }
}
}