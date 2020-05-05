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
        stage('Build image') {
            agent { dockerfile true }
            steps {
                sh 'echo build Payara'
            }
        }
    }
}


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

node {
    docker.image('payara/server-full').run()
}

cp target/$NAME-$VERSION.jar /var/jenkins_home/payaradocker


set -x
mkdir target/payara
set +x

set -x
cp target/$NAME-$VERSION.jar /target/payara
set +x