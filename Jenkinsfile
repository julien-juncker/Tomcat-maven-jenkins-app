pipeline {
    agent none

    stages {
        stage('Test Maven') {
            agent {         
                docker {
                    image 'maven:3-alpine' 
                    args '-v /root/.m2:/root/.m2' 
                } 
            }
            steps {
                sh 'mvn -B -DskipTests clean package' 
                sh 'mvn test'
                sh 'mvn jar:jar install:install help:evaluate -Dexpression=project.name'
                sh './jenkins/scripts/deliver.sh' 
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Build Payara') {
            agent { dockerfile true }
            steps {
                sh 'echo build Payara'
            }
        }
    }
}