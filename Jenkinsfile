node {

    stage('clone git repo') {
        git branch: 'gatling', url: 'https://github.com/farzamgt/jenkins-perf-test.git'
    }

    stage('configure') {
        sh "mkdir -p ${WORKSPACE}/reports/run-${BUILD_NUMBER}"
    }

    stage('run test') {
        dir("${WORKSPACE}/gatling") {
            sh """
            mvn clean install -U gatling:test \
            -Dusers=3 \
            -DrampUp=10 \
            -DbaseUrl=http://wp:80 \
            -DassertionType=order
            """
        }
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