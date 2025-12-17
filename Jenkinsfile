pipeline {
    agent any

    environment {
        DOCKERHUB_USER = 'numidu'
    }

    stages {

        stage('Clone Repository') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Numidu/BackendDeploye.git'
            }
        }

        stage('Build Backend Image') {
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
                        cd userservice
                        docker build -t $DOCKER_USER/backend:latest .
                    '''
                }
            }
        }

        stage('Push Image to Docker Hub') {
            steps {
                sh '''
                    docker push numidu/backend:latest
                '''
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

                            mkdir -p ~/app
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
