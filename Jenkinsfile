pipeline {
    agent any

    environment {
        IMAGE_NAME = "numidu/ampora_backend"
        VM_USER = "dnumidu"
        VM_IP = "34.44.230.107"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'numidu',
                    url: 'https://github.com/Numidu/BackendDeploye.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t $IMAGE_NAME:latest ."
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $IMAGE_NAME:latest
                    '''
                }
            }
        }

        stage('Deploy to GCP VM') {
            steps {
                sshagent(['gcp_vm_key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no $VM_USER@$VM_IP '
                        cd ~/app || mkdir -p ~/app && cd ~/app
                        docker compose pull
                        docker compose down
                        docker compose up -d
                    '
                    """
                }
            }
        }
    }
}
