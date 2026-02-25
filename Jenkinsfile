node {

    stage('clone git repo') {
        git branch: 'lighthouse', url: 'https://github.com/farzamgt/jenkins-perf-test.git'
    }

    stage('configure') {
        sh """
        mkdir -p ${WORKSPACE}/reports/lighthouse/run-${BUILD_NUMBER}
        cd lighthouse
        npm install
        """
    }

    stage('run test') {
        sh """
        cd lighthouse
        node orderFlow.js
        """
    }

    stage('collect report') {
        sh """
        mv lighthouse/order-flow.html ${WORKSPACE}/reports/lighthouse/run-${BUILD_NUMBER}
        """
    }

    stage('publish results') {
        archiveArtifacts artifacts: "reports/lighthouse/run-${BUILD_NUMBER}/*", fingerprint: true
    }
}