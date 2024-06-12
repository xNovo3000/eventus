pipeline {

    agent { label 'agent' }

    tools {
        jdk '17'
        maven '3'
    }

    stages {
        stage('Clean') {
            steps {
                withMaven {
                    sh 'mvn clean'
                }
            }
        }
        stage('Build') {
            steps {
                withMaven {
                    sh 'mvn compile'
                }
            }
        }
        stage('Test') {
            steps {
                withMaven {
                    sh 'mvn test'
                }
            }
        }
        stage('Package') {
            steps {
                withMaven {
                    sh 'mvn package'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Fake deploying...'
            }
        }
    }
    
}
