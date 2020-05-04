def pipelineContext = [:]

pipeline {
    agent {
        docker {
            image 'maven:3-alpine' 
            args '-v /root/.m2:/root/.m2' 
        }
    }

    environment {
        DOCKER_IMAGE_TAG = "payara/server-full"
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
        stage('Build image') {
            steps {
                script {
                    dockerImage = docker.build("${env.DOCKER_IMAGE_TAG}")
                    pipelineContext.dockerImage = dockerImage
                }
            }
        }
        stage('Run') {
            steps {
                echo "Run docker image"
                script {
                    pipelineContext.dockerContainer = pipelineContext.dockerImage.run('-p 8080:8080 -p 4848:4848 -v ~/payaradocker:/opt/payara/deployments')
                }
            }
        }
    }
}