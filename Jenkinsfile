pipeline {
    agent any

    environment {
        IMAGE_NAME = "numidu/ampora_backend"
        VM_USER = "ampora-jenkins"   // ⚠️ use correct SSH username
        VM_IP   = "104.197.153.74"   // ⚠️ new VM IP
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
                sshagent(['gcp_vm_key']) {
                    withCredentials([
                        string(credentialsId: 'db-password', variable: 'DB_PASSWORD'),
                        string(credentialsId: 'google-api-key', variable: 'GOOGLE_API_KEY')
                    ]) {
                        sh '''
                            ssh -o StrictHostKeyChecking=no ampora-jenkins@104.197.153.74 << EOF
                            set -e

                            cd ~/app

                            export SPRING_DATASOURCE_PASSWORD="$DB_PASSWORD"
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
}
