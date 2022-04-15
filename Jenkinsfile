pipeline {

agent {dockerfile true}
stages {
    stage("Compile"){
      steps {
        echo 'Compile project'
        sh "chmod +x gradlew"
        sh "./gradlew clean build"
      }
    }
    stage("Docker Build"){
      steps {
        echo 'Init docker build'
        sh "docker build -t cthulhu-docker ."
          }
        }
       stage("Docker Run"){
      steps {
        echo 'Starting docker run'
        sh "docker run --name cthulhuContainer -p 9400:9200 -d cthulhu-docker"
          }
        }
}
}