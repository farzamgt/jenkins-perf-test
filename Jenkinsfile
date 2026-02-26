node {
    stage('Clone git repo') {
        git branch: 'gatling-test', url: 'https://github.com/farzamgt/jenkins-perf-test.git'
    }

    stage('Prepare report folder') {
        sh "mkdir -p ${WORKSPACE}/reports/run-${BUILD_NUMBER}"
    }

    stage('Run Gatling tests') {
        sh """
        docker exec gatling mvn clean install gatling:test \
          -Dusers=3 \
          -DrampUp=10 \
          -DbaseUrl=http://wp:80 \
          -DassertionType=order
        """
    }

    stage('Publish results') {
        archiveArtifacts artifacts: "reports/run-${BUILD_NUMBER}/**", fingerprint: true
    }
}