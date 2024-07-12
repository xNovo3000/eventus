#!/usr/bin/env groovy

pipeline {

    agent { label 'worker-medium' }

    tools {
        jdk '17-temurin'
        maven '3'
    }

    stages {

        stage('Maven: Build') {
            steps {
                withMaven {
                    sh 'mvn compile'
                }
            }
        }

        stage('Maven: Package') {
            steps {
                withMaven {
                    sh 'mvn package -Dmaven.test.skip=true'
                }
                stash name: 'target', includes: '**/target/**'
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                unstash name: 'target'
                dependencyCheck odcInstallation: '8'
                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
            }
        }

        stage('Build and push artifacts') {
            parallel {
                stage('Maven: Install') {
                    steps {
                        unstash name: 'target'
                        withMaven {
                            sh 'mvn install -Dmaven.test.skip=true'
                        }
                    }
                }
                stage('Docker: Build') {
                    agent { label 'worker-medium-docker' }
                    steps {
                        unstash name: 'target'
                        sh 'docker build . -t eventus:1.3.1'
                    }
                }
            }
        }
        
    }
    
}
