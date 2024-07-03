#!/usr/bin/env groovy

pipeline {

    agent { label 'worker-medium' }

    tools {
        jdk '17-temurin'
        maven '3'
    }

    stages {

        stage('Clean') {
            steps {
                cache(
                    defaultBranch: 'develop',
                    caches: [arbitraryFileCache(path: '/home/jenkins/.m2/repository')]
                ) {
                    withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                        sh 'mvn clean'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                cache(
                    defaultBranch: 'develop',
                    caches: [arbitraryFileCache(path: '/home/jenkins/.m2/repository')]
                ) {
                    withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                        sh 'mvn compile'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                cache(
                    defaultBranch: 'develop',
                    caches: [arbitraryFileCache(path: '/home/jenkins/.m2/repository')]
                ) {
                    withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                        sh 'mvn test'
                    }
                }
            }
        }

        stage('Package') {
            steps {
                cache(
                    defaultBranch: 'develop',
                    caches: [arbitraryFileCache(path: '/home/jenkins/.m2/repository')]
                ) {
                    withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                        sh 'mvn package -Dmaven.test.skip=true'
                    }
                }
            }
        }

        stage('Verify') {
            steps {
                cache(
                    defaultBranch: 'develop',
                    caches: [arbitraryFileCache(path: '/home/jenkins/.m2/repository')]
                ) {
                    withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                        sh 'mvn verify'
                    }
                }
            }
        }

        stage('Install') {
            steps {
                cache(
                    defaultBranch: 'develop',
                    caches: [arbitraryFileCache(path: '/home/jenkins/.m2/repository')]
                ) {
                    withMaven(mavenSettingsConfig: 'soldo-maven-settings') {
                        sh 'mvn install -Dmaven.test.skip=true'
                    }
                }
            }
        }
        
    }
    
}
