node {
	
	try {
		// deleteDir()
		def applicationName = 'request-handler'
		def imageTag
		checkout scmGit(branches: [[name: '*/fb_jenkins']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/ruwinda90/content-publisher-request-handler.git']]) // todo
	
		docker.image('maven:3.9.6-eclipse-temurin-8-alpine').inside('-v $HOME/.m2:/root/.m2') {
			stage('Build') {
				sh 'echo "Start build stage" && mvn --version'
				sh 'mvn clean package -Dmaven.test.skip=true'
				echo 'Build stage complete'
			}
			stage('Test') {
				try {
				    echo 'Start test stage'
				    sh 'mvn test-compile surefire:test'
				    echo 'Test stage complete'
				} catch (ex) {
				    currentBuild.result = 'UNSTABLE'
				    throw ex
				}
			}
		}

		stage('Image build') {
            echo 'Start image build stage'
            def currentBranch = env.BRANCH_NAME;
            imageTag = "${currentBranch}-${env.BUILD_ID}";
            def buildArgs = """--build-arg CONFIG_FILE=deployment/application-${currentBranch}.yml ."""
            def applicationImage = docker.build("${applicationName}:${imageTag}", buildArgs)
            echo 'Image build stage complete'
        }

		stage('Deploy') {
			echo 'Start deploy stage'
			// todo - move to script
			sh "if [ \$(docker ps -a | grep ${applicationName} | wc -l) -ge 1 ]; then docker stop ${applicationName} && docker rm ${applicationName}; fi"
			sh "docker run -d -p 8081:8080 --name ${applicationName} ${applicationName}:${imageTag}" // todo - rm hardcoded port into params maybe
			echo 'Deploy stage complete'
		}
		currentBuild.result = 'SUCCESS'
	} catch (e) {
		echo 'This will run only if failed'
		throw e
	} finally {
// 	    def currentResult = currentBuild.result ?: 'FAILURE'
	    if (currentBuild.result == null) {
            currentBuild.result = 'FAILURE'
        }
		if (currentBuild.result == 'UNSTABLE') {
			echo 'This will run only if the run was marked as unstable'
		}
		
		def previousResult = currentBuild.previousBuild?.result
		if (previousResult != null && previousResult != currentBuild.result) {
			echo 'This will run only if the state of the Pipeline has changed'
		}
		
		echo "Pipeline complete! Status - ${currentBuild.result}"
	}
	
}
