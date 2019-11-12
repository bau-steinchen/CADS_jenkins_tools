pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Preparation'){
            steps{
                //sh 'ls'
                sh 'id -u jenkins'
            }
        }
        stage('Git checkout'){
            steps{
                dir('inet-private'){
                  echo 'checkout inet-private'
                  git branch: 'FEATURE/MIKKOE/Quic-test',
                    credentialsId: 'fd377909-72a2-44f5-b89e-787344533514',
                    url: 'https://github.com/Transport-Protocol/inet-private.git'
                  sh 'git submodule update --init'
                }
            }
        }
        stage('docker-omnetpp'){
            steps{
                echo 'trying docker container'
                script{
                    def image = docker.image('mikkoe/omnetpp-inet-docker')
                    image.pull()
                    image.inside{

                        // first information showing
                        sh 'ls -l'
                        sh 'which gcc'
                        sh 'which g++'


                        // installing omnetpp
                        sh 'wget https://github.com/omnetpp/omnetpp/releases/download/omnetpp-5.4.1/omnetpp-5.4.1-src-linux.tgz \
                            && tar -xzf omnetpp-5.4.1-src-linux.tgz \
                            && rm omnetpp-5.4.1-src-linux.tgz \
                            && mv omnetpp-5.4.1 omnetpp'
                        sh 'PATH=$PATH:${pwd}/omnetpp/bin'
                        sh 'cd omnetpp && ./configure WITH_TKENV=no WITH_QTENV=no WITH_OSG=no WITH_OSGEARTH=no WITH_PARSIM=no'
                        sh 'export PATH="/var/jenkins_home/workspace/TestSuite/omnetpp/bin:"$PATH'
                        sh 'echo $PATH'
                        sh 'cd omnetpp && make -j$(grep -c proc /proc/cpuinfo)'


                        // installing inet
                        sh 'cd inet-private && cat README.md'
                        sh 'cd inet-private && cat INSTALL'
                        sh 'cd inet-private && cat Makefile'
                        sh 'cd inet-private && make makefiles'
                        sh 'cd inet-private && make MODE=debug'
                    }
                }
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
