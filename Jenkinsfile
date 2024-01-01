node {
	
	docker.image('maven:3.9.6-eclipse-temurin-8-alpine').inside('-v $HOME/.m2:/root/.m2') {
        stage('Build') {
            sh 'echo "Start build stage" && mvn --version'
			sh 'mvn clean package -Dmaven.test.skip=true'
			sh 'echo "Build stage complete"'
        }
		stage('Test') {
            sh 'echo "Start test stage"'
            sh 'mvn test-compile surefire:test'
            sh 'echo "Test stage complete"'
        }
    }
	
	stage('Deploy') {
		sh 'echo "Start deploy stage"'
		sh 'echo "Deploy stage complete"'
    }
	
}
