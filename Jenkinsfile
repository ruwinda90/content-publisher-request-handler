/* Requires the Docker Pipeline plugin */
pipeline {
    agent { docker { image 'maven:3.9.6-eclipse-temurin-17-alpine' } }
    
    stages {
        stage('build') {
            steps {
                sh 'echo "Start build stage"'
                sh 'mvn --version'
                // sh 'mvn clean package -Dmaven.test.skip=true'
                sh 'echo "Build stage complete"'
            }
        }
        stage('test') {
            steps {
                sh 'echo "Start test stage"'
                sh 'echo "Test stage complete"'
            }
        }
        stage('deploy') {
            steps {
                sh 'echo "Start deploy stage"'
                sh 'echo "Deploy stage complete"'
            }
        }
    }
    post {
        always {
            echo 'This will always run'
        }
        success {
            echo 'This will run only if successful'
        }
        failure {
            echo 'This will run only if failed'
        }
        unstable {
            echo 'This will run only if the run was marked as unstable'
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }
    }
}
