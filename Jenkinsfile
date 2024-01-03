node {
	
	try {
		// deleteDir()
		checkout scmGit(branches: [[name: '*/fb_jenkins']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/ruwinda90/content-publisher-request-handler.git']])
	
		docker.image('maven:3.9.6-eclipse-temurin-8-alpine').inside('-v $HOME/.m2:/root/.m2') {
			stage('Build') {
				sh 'echo "Start build stage" && mvn --version'
				sh 'mvn clean package -Dmaven.test.skip=true'
				echo 'Build stage complete'
			}
			stage('Test') {
				echo 'Start test stage'
				sh 'mvn test-compile surefire:test'
				echo 'Test stage complete'
			}
		}

		stage('Image build') {
            echo 'Start image build stage'
            def currentBranch = env.BRANCH_NAME;

            def buildArgs = """--build-arg CONFIG_FILE=deployment/application-${currentBranch}.yml ."""
            def applicationImage = docker.build("request-handler:${currentBranch}-${env.BUILD_ID}", buildArgs)

            echo 'Image build stage complete'
        }

		stage('Deploy') {
			echo 'Start deploy stage'
			echo 'Deploy stage complete'
		}
		
	} catch (e) {
		echo 'This will run only if failed'
		throw e
	} finally {
		def currentResult = currentBuild.result ?: 'SUCCESS'
		if (currentResult == 'UNSTABLE') {
			echo 'This will run only if the run was marked as unstable'
		}
		
		def previousResult = currentBuild.previousBuild?.result
		if (previousResult != null && previousResult != currentResult) {
			echo 'This will run only if the state of the Pipeline has changed'
		}
		
		echo 'This will always run'
	}
	
}
