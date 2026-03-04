node {

    stage('clone git repo') {
        git 'https://github.com/farzamgt/jenkins-perf-test.git'
    }

    stage('debug') {
        bat """
        echo ==== DEBUG INFO ====
        echo JMETER_HOME:
        echo %JMETER_HOME%
        echo.
        echo PATH:
        echo %PATH%
        echo.
        echo Where is jmeter:
        where jmeter
        """
    }

    stage('configure') {
        bat "if not exist %WORKSPACE%\\%BUILD_NUMBER% mkdir %WORKSPACE%\\%BUILD_NUMBER%"
    }

    stage('run test') {
        bat """
        jmeter -n ^
        -t %WORKSPACE%\\jmeter\\test.jmx ^
        -l %WORKSPACE%\\%BUILD_NUMBER%\\JMeter.jtl ^
        -e -o %WORKSPACE%\\%BUILD_NUMBER%\\HtmlReport
        """
    }

    stage('publish results') {
        archiveArtifacts artifacts: "${BUILD_NUMBER}/JMeter.jtl, ${BUILD_NUMBER}/HtmlReport/**"
    }
}