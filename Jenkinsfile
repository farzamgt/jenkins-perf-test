node {

    stage('clone git repo') {
        git branch: 'main', url: 'https://github.com/farzamgt/jenkins-perf-test.git'
    }

    stage('configure') {
        sh "mkdir -p ${WORKSPACE}/reports/run-${BUILD_NUMBER}"
    }

    stage('run test') {
        sh """
        jmeter \
        -Jjmeter.save.saveservice.output_format=csv \
        -Jjmeter.save.saveservice.default_connect_timeout=10000 \
        -Jjmeter.save.saveservice.default_response_timeout=20000 \
        -n -t ${WORKSPACE}/jmeter/test.jmx \
        -l ${WORKSPACE}/reports/run-${BUILD_NUMBER}/JMeter.jtl \
        -e -o ${WORKSPACE}/reports/run-${BUILD_NUMBER}/HtmlReport
        """
    }

    stage('publish results') {
        archiveArtifacts artifacts: "reports/run-${BUILD_NUMBER}/JMeter.jtl, reports/run-${BUILD_NUMBER}/HtmlReport/**", fingerprint: true
    }
}