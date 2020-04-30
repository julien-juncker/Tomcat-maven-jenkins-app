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
    }
}
node{
    def app
    stage('Build image') { 
        app = docker.build('payara/server-full')
    }
    stage('Deliver') { 
        docker.image('payara/server-full').withRun('-p 8080:8080 -p 4848:4848 -v ~/payaradocker:/opt/payara/deployments')
    }
}