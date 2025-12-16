pipeline {
    agent any

    environment {
        IMAGE_NAME = "numidu/ampora_backend"
        VM_USER = "dnumidu"
        VM_IP = "136.119.14.13"
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
                sh 'docker build -t $IMAGE_NAME:latest .'
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
                        docker push $IMAGE_NAME:latest
                    '''
                }
            }
        }

        stage('Deploy to GCP VM') {
            steps {
                withCredentials([
                    string(credentialsId: 'db-password', variable: 'DB_PASSWORD'),
                    string(credentialsId: 'google-api-key', variable: 'GOOGLE_API_KEY')
                ]) {
                    sshagent(['gcp_vm_key']) {
                        sh """
                        ssh -o StrictHostKeyChecking=no $VM_USER@$VM_IP '
                            mkdir -p ~/app
                            cd ~/app
                            echo "DB_PASSWORD=$DB_PASSWORD" > .env
                            echo "GOOGLE_API_KEY=$GOOGLE_API_KEY" >> .env
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
}
