/* Requires the Docker Pipeline plugin */
pipeline {
    agent { docker { image 'maven:3.9.6-eclipse-temurin-17-alpine' } }
    stages {
        stage('build') {
            steps {
                sh 'maven version $(mvn --version)'
                sh 'echo "Hello World"'
                sh 'pwd'
                sh 'ls -ltr'
                sh 'mvn clean package -Dmaven.test.skip=true'
            }
        }
        stage('test') {
            steps {
                sh 'echo "Running tests"'
            }
        }
        stage('deploy') {
            steps {
                sh 'echo "Deploying"'
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
