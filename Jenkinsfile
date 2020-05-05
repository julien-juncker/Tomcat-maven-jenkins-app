def pipelineContext = [:]

pipeline {
    agent none

    environment {
        DOCKER_IMAGE_TAG = "my-app:build-${env.BUILD_ID}"
    }

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
                // sh 'mvn jar:jar install:install help:evaluate -Dexpression=project.name'
                sh './jenkins/scripts/deliver.sh'
                sh 'cp target/$NAME-$VERSION.jar /run'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Build Payara') {
            steps {
                echo "build Payara"
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
                    pipelineContext.dockerContainer = pipelineContext.dockerImage.run('-p 80:80 -p 4848:4848 -v ~/payaradocker:/opt/payara/deployments')
                }
            }
        }
    }
}