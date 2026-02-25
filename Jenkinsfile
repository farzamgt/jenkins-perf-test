node {

    stage('clone git repo') {
        git branch: 'gatling', url: 'https://github.com/farzamgt/jenkins-perf-test.git'
    }

    stage('configure') {
        sh "mkdir -p ${WORKSPACE}/reports/run-${BUILD_NUMBER}"
    }

    stage('run test') {
        sh """
        mvn gatling:test \
        -Dusers=60 \
        -DrampUp=180 \
        -DbaseUrl=http://localhost \
        -DassertionType=order
        """
    }

    stage('collect results') {
        sh """
        cp -r ${WORKSPACE}/target/gatling ${WORKSPACE}/reports/run-${BUILD_NUMBER}/
        """
    }

    stage('publish results') {
        archiveArtifacts artifacts: "reports/run-${BUILD_NUMBER}/**", fingerprint: true
    }
}