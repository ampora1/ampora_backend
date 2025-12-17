pipeline {
    agent any

    environment {
        IMAGE_NAME = "numidu/ampora_backend"
        VM_USER = "ampora-jenkins"
        VM_IP   = "104.197.153.74"
    }

    stages {

        stage('Checkout Source') {
            steps {
                git branch: 'numidu',
                    url: 'https://github.com/mari75a/ampora_backend.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t numidu/ampora_backend:latest .'
            }
        }

        stage('Push Image to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push numidu/ampora_backend:latest
                    '''
                }
            }
        }

        stage('Deploy to GCP VM') {
            steps {
                withCredentials([
                    sshUserPrivateKey(
                        credentialsId: 'gcp_vm_key',
                        keyFileVariable: 'SSH_KEY',
                        usernameVariable: 'SSH_USER'
                    ),
                    string(credentialsId: 'google-api-key', variable: 'GOOGLE_API_KEY')
                ]) {
                    sh '''
                        ssh -i "$SSH_KEY" -o StrictHostKeyChecking=no "$SSH_USER@104.197.153.74" << EOF
                        set -e

                        mkdir -p ~/app
                        cd ~/app

                        export GOOGLE_API_KEY="$GOOGLE_API_KEY"

                        docker compose pull
                        docker compose down || true
                        docker compose up -d
                        EOF
                    '''
                }
            }
        }
    }
}
