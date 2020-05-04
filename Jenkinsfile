pipeline {
    agent any

    stages {
        stage('Build') {
            agent {
                docker {
                    image 'maven:3-alpine' 
                    args '-v /root/.m2:/root/.m2' 
                }
            }
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Deliver') { 
            steps {
                sh './jenkins/scripts/deliver.sh' 
            }
        }
        stage('Build image') {
            agent {
                docker {
                    image 'payara/server-full' 
                    args '-p 8080:8080 -p 4848:4848 -v ~/payaradocker:/opt/payara/deployments' 
                }
            }
            steps {
                sh 'echo build Payara'
            }
        }
    }
}