pipeline {
    agent {
        docker {
            image 'maven:3-alpine' 
            args '-v /root/.m2:/root/.m2' 
        }
    }
    stages {
        stage('Build') { 
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
    }
}
node{
    def app
    def NAME
    def VERSION
    stage('Build image') { 
        app = docker.build('payara/server-full')
    }
    stage('Payara run') { 
        docker.image('payara/server-full').run('-p 8080:8080 -p 4848:4848 -v ~/payaradocker:/opt/payara/deployments') {c -> sh "echo test"}
    }
}