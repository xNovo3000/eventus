#!/usr/bin/env groovy

pipeline {

    agent { label 'worker-medium-2' }

    tools {
        jdk '17-temurin'
        maven '3'
    }

    stages {

        stage('Clean') {
            steps {
                withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                    sh 'mvn clean'
                }
            }
        }

        stage('Build') {
            steps {
                    withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                        sh 'mvn compile'
                    }
            }
        }

        stage('Test') {
            steps {
                    withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                        sh 'mvn test'
                    }
            }
        }

        stage('Package') {
            steps {
                    withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                        sh 'mvn package -Dmaven.test.skip=true'
                    }
            }
        }

        stage('Verify') {
            steps {
                    withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                        sh 'mvn verify'
                    }
            }
        }

        stage('Install') {
            steps {
                    withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                        sh 'mvn install -Dmaven.test.skip=true'
                    }
            }
        }
        
    }
    
}
