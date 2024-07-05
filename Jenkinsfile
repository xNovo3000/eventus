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
                configFileProvider([configFile(fileId: 'soldo-maven-settings', variable: 'MVN_SETTINGS')]) {
                    sh 'mvn compile -s $MVN_SETTINGS'
                }
            }
        }

        stage('Maven: Test') {
            steps {
                configFileProvider([configFile(fileId: 'soldo-maven-settings', variable: 'MVN_SETTINGS')]) {
                    sh 'mvn test -s $MVN_SETTINGS'
                }
            }
        }

        stage('Maven: Package') {
            steps {
                configFileProvider([configFile(fileId: 'soldo-maven-settings', variable: 'MVN_SETTINGS')]) {
                    sh 'mvn package -Dmaven.test.skip=true -s $MVN_SETTINGS'
                }
                stash name: 'target', includes: '**/target/**'
            }
        }

        stage('Maven: Verify') {
            steps {
                configFileProvider([configFile(fileId: 'soldo-maven-settings', variable: 'MVN_SETTINGS')]) {
                    sh 'mvn verify -s $MVN_SETTINGS'
                }
            }
        }

        stage('Build and push artifacts') {
            parallel {
                stage('Maven: Install') {
                    steps {
                        unstash name: 'target'
                        configFileProvider([configFile(fileId: 'soldo-maven-settings', variable: 'MVN_SETTINGS')]) {
                            sh 'mvn install -Dmaven.test.skip=true -s $MVN_SETTINGS'
                        }
                    }
                }
                stage('Docker: Build') {
                    agent { label 'worker-medium && docker' }
                    steps {
                        unstash name: 'target'
                        sh 'docker build . -t eventus:1.3.1'
                    }
                }
            }
        }
        
    }
    
}
