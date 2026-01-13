pipeline {
    agent any

    environment {
        IMAGE_NAME = "numidu/ampora_backend"
        VM_USER    = "ec2-user"                 // AWS default user
        VM_IP      = "3.25.85.147"             // YOUR EC2 PUBLIC IP
    }

    stages {
        stage('Checkout Source') {
            steps {
                git branch: 'numidu', url: 'https://github.com/mari75a/ampora_backend.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        docker build -t ${IMAGE_NAME}:latest .
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Deploy to AWS EC2') {
            steps {
                withCredentials([
                    sshUserPrivateKey(credentialsId: 'ec2_key', keyFileVariable: 'SSH_KEY_PATH')
                ]) {
                    sh """
                        echo "Deploying to AWS EC2..."

                        scp -i \$SSH_KEY_PATH -o StrictHostKeyChecking=no docker-compose.yml ${VM_USER}@${VM_IP}:~/docker-compose.yml

                        ssh -i \$SSH_KEY_PATH -o StrictHostKeyChecking=no ${VM_USER}@${VM_IP} '
                            docker pull ${IMAGE_NAME}:latest
                            docker compose up -d --remove-orphans
                        '

                        echo "Deployment complete"
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ AWS Deployment successful!"
        }
        failure {
            echo "❌ Deployment failed. Check Jenkins logs."
        }
    }
}
