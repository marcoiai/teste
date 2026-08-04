pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('server-osgi') {
                    sh 'mvn -B -ntp clean package'
                }
            }
        }

        stage('Archive artifacts') {
            steps {
                archiveArtifacts artifacts: 'server-osgi/**/target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}
