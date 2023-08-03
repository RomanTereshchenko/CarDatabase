pipeline {
    agent any
    tools {
        maven 'MAVEN'
    }

    stages {
        stage('Build') {
            steps {
                dir ("/git/cardatabase/car-db") {
                sh "mvn -dmaven.test.falure.ignore=true mvn clean"
            }
        }
    }
    }
    post {
        always {
            junit (
                allowEmptyResults:true,
                testResults: '*test-reports/.xml'
                )
        }
    }
}
