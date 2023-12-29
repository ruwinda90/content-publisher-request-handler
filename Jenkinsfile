/* Requires the Docker Pipeline plugin */
pipeline {
    agent { docker { image 'maven:3.9.6-eclipse-temurin-17-alpine' } }

    environment {
        RED = '\033[0;31m'
        GREEN = '\033[0;32m'
        NC = '\033[0m'
    }
    
    stages {
        stage('build') {
            steps {
                sh 'echo -e "Start build stage"'
                sh 'mvn --version'
                // sh 'mvn clean package -Dmaven.test.skip=true'
                sh 'echo -e "${GREEN}Build stage complete${NC}"'
            }
        }
        stage('test') {
            steps {
                sh 'echo "Start test stage"'
                sh 'echo -e "${GREEN}Test stage complete${NC}"'
            }
        }
        stage('deploy') {
            steps {
                sh 'echo "Start deploy stage"'
                sh 'echo -e "${GREEN}Deploy stage complete${NC}"'
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
