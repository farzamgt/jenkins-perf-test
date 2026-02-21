node {

    stage('clone git repo') {
        git branch: 'main', url: 'https://github.com/farzamgt/jenkins-perf-test.git'
    }

    stage('configure') {
        sh "mkdir -p ${WORKSPACE}/${BUILD_NUMBER}"
        sh "mkdir -p ${WORKSPACE}/reports"
    }

    stage('run test') {
        sh """
        jmeter -Jjmeter.save.saveservice.output_format=csv \
        -n -t ${WORKSPACE}/jmeter/test.jmx \
        -l ${WORKSPACE}/reports/JMeter.jtl \
        -e -o ${WORKSPACE}/reports/HtmlReport
        """
    }

    stage('publish results') {
        sh "mv ${WORKSPACE}/reports/* ${WORKSPACE}/${BUILD_NUMBER}/"
        archiveArtifacts artifacts: "${BUILD_NUMBER}/JMeter.jtl, ${BUILD_NUMBER}/HtmlReport/**", fingerprint: true
    }
}