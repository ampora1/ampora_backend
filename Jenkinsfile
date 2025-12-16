pipeline {
    agent any

    environment {
        IMAGE_NAME = "numidu/ampora_backend"
        VM_USER = "dnumidu"
        VM_IP = "136.119.14.13"
        PRIVATE_KEY_PATH = "${HOME}/.ssh/ampora_gcp_key" // path to your private key on Jenkins agent
    }

    stages {

        stage('Checkout Source') {
            steps {
                git branch: 'numidu',
                    url: 'https://github.com/mari75a/ampora_backend.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:latest ."
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
                    sh """
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Deploy to GCP VM') {
            steps {
                withCredentials([
                    string(credentialsId: 'db-password', variable: 'DB_PASSWORD'),
                    string(credentialsId: 'google-api-key', variable: 'GOOGLE_API_KEY')
                ]) {
                    sh """
ssh -i ${PRIVATE_KEY_PATH} -o StrictHostKeyChecking=no ${VM_USER}@${VM_IP} '
cd ~/app
export SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
export GOOGLE_API_KEY=${GOOGLE_API_KEY}
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
