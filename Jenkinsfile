pipeline {
    agent any

    environment {
        IMAGE_NAME = "numidu/ampora_backend"
        VM_USER    = "dnumidu"
        VM_IP      = "34.14.149.31"
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

        stage('Deploy to GCP VM') {
            steps {
                withCredentials([
                    sshUserPrivateKey(credentialsId: 'gcp_vm_key', keyFileVariable: 'SSH_KEY'),
                    string(credentialsId: 'google-api-key', variable: 'G_API_KEY')
                ]) {
                    sh """
                        # 1. Copy the docker-compose file to the VM
                        scp -i ${SSH_KEY} -o StrictHostKeyChecking=no docker-compose.yml ${VM_USER}@${VM_IP}:~/docker-compose.yml

                        # 2. Connect via SSH and deploy
                        ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ${VM_USER}@${VM_IP} "
                            set -e
                            
                            # Export the key so docker-compose can see it
                            export GOOGLE_API_KEY='${G_API_KEY}'
                            
                            docker compose pull
                            docker compose up -d --remove-orphans
                        "
                    """
                }
            }
        }
    }

    post {
        success { echo "✅ Deployment successful!" }
        failure { echo "❌ Deployment failed." }
    }
}
