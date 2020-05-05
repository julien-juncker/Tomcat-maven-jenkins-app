def pipelineContext = [:]

pipeline {
    agent {
        docker {
            image 'maven:3-alpine' 
            args '-v /root/.m2:/root/.m2' 
        }
    }

    environment {
        DOCKER_IMAGE_TAG = "payara/server-full:build-${env.BUILD_ID}"
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

FROM payara/server-full

COPY target/my-app-1.0-SNAPSHOT.jar /opt/payara/deployments/my-app-1.0-SNAPSHOT.jar




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
        docker.image('payara/server-full').withRun('-p 8080:8080 -p 4848:4848 -v ~/payaradocker:/opt/payara/deployments') {c -> sh "echo test"}
    }
}


set -x
pwd
set +x

set -x
java -jar target/${NAME}-${VERSION}.jar
set +x

set -x
cp target/$NAME-$VERSION.jar /var/Jenkins
set +x

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