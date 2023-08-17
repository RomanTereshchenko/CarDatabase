pipeline {
    agent any
    tools {
        maven 'MAVEN'
    }

    stages {
        stage('Build') {
            steps {
            checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/dev']], extensions: [], userRemoteConfigs: [[credentialsId: '73d3b5a7-494f-4254-890e-dba9d9233f20', url: 'https://git.foxminded.ua/foxstudent102570/cardatabase.git']])
            sh "mvn clean install"
            }
        }
        stage('Test') {
            steps {
            sh "mvn test"
            }
        }
        stage('Build Docker Image') {
            steps {
            sh "docker system prune -f"
            sh "docker build - < Dockerfile"
            }
        }
        stage('Build and run Docker Containers') {
            steps {
            sh "docker compose up -d"
            }
        }
        
    }
    post {
        always {
            sh "docker compose down"
        }
    }
}
