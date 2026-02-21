node {

    stage('clone git repo') {
        git branch: 'main', url: 'https://github.com/farzamgt/jenkins-perf-test.git'
    }

    stage('configure') {
        sh "mkdir -p ${WORKSPACE}/reports/run-${BUILD_NUMBER}"
    }

    stage('run test') {
        def reportDir = "${WORKSPACE}/reports/run-${BUILD_NUMBER}"
        sh """
        jmeter -Jjmeter.save.saveservice.output_format=csv \
               -n -t ${WORKSPACE}/jmeter/test.jmx \
               -l ${reportDir}/JMeter.jtl \
               -e -o ${reportDir}/HtmlReport
        """
    }

    stage('publish results') {
        archiveArtifacts artifacts: "reports/run-${BUILD_NUMBER}/JMeter.jtl, reports/run-${BUILD_NUMBER}/HtmlReport/**", fingerprint: true
    }
}