node {

    stage('clone git repo') {
        git 'https://github.com/farzamgt/jenkins-perf-test.git'
    }

    stage('configure') {
        bat "mkdir %WORKSPACE%\\%BUILD_NUMBER%"
        bat "mkdir %WORKSPACE%\\reports"
    }

    stage('run test') {
        bat """
        C:\\jmeter\\bin\\jmeter.bat -Jjmeter.save.saveservice.output_format=xml ^
        -n -t %WORKSPACE%\\jmeter\\test.jmx ^
        -l %WORKSPACE%\\reports\\JMeter.jtl ^
        -e -o %WORKSPACE%\\reports\\HtmlReport
        """
    }

    stage('publish results') {
        bat "move %WORKSPACE%\\reports\\* %WORKSPACE%\\%BUILD_NUMBER%\\"
        archiveArtifacts artifacts: "%BUILD_NUMBER%\\JMeter.jtl, %BUILD_NUMBER%\\HtmlReport\\**", fingerprint: true
    }
}