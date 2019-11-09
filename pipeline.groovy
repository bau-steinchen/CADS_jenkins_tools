pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('docker-1'){
            steps{
                echo 'trying docker container'
                script{
                    def image = docker.image('node:alpine')
                    image.pull()
                    image.inside() {

                        sh 'node --version'
                    }
                }
            }
        }
        stage('docker'){
            steps{
                echo 'trying docker container'
                script{
                    def image = docker.image('niessan/omnetpp-inet')
                    image.pull()
                    image.inside() {

                        sh 'cat /root/'
                    }
                }
            }
        }
        stage('Preparation'){
            steps{
                sh 'ls'
            }
        }
        stage('Git checkout'){
            steps{
                echo 'testing checkout scm'
                git branch: 'FEATURE/MIKKOE/Quic-test',
                  credentialsId: 'fd377909-72a2-44f5-b89e-787344533514',
                  url: 'https://github.com/Transport-Protocol/inet-private.git'
            }
        }
        stage('Run opp_test'){
            steps{
                echo 'testing checkout scm'
            }
        }
        stage('Collect Data'){
            steps{
                echo 'testing checkout scm'
            }
        }
        stage('Clean Up'){
            steps{
                echo 'testing checkout scm'
            }
        }
    }
    post {
        success {
            echo 'Pipe Stage successfull'
        }
        failure {
            echo 'Pipe Stage failure'
        }
        always {
            echo 'always here for you'
            cleanWs()
        }
    }
}
