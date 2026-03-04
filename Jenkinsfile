pipeline {

    agent any

    parameters {
        string(name: 'USERS',    defaultValue: '5',  description: 'Number of virtual users')
        string(name: 'RAMPUP',   defaultValue: '10',  description: 'Ramp-up time (seconds)')
        string(name: 'DURATION', defaultValue: '30',  description: 'Test duration (seconds)')
    }

    stages {

        stage('clone git repo') {
            steps {
                git branch: 'jmeter', url: 'https://github.com/farzamgt/jenkins-perf-test.git'
            }
        }

        stage('configure') {
            steps {
                bat "if not exist %WORKSPACE%\\%BUILD_NUMBER% mkdir %WORKSPACE%\\%BUILD_NUMBER%"
            }
        }

        stage('run test') {
            steps {
                bat """
                jmeter -n ^
                -Jusers=${params.USERS} ^
                -Jrampup=${params.RAMPUP} ^
                -Jduration=${params.DURATION} ^
                -t %WORKSPACE%\\jmeter\\test.jmx ^
                -l %WORKSPACE%\\%BUILD_NUMBER%\\JMeter.jtl ^
                -e -o %WORKSPACE%\\%BUILD_NUMBER%\\HtmlReport
                """
            }
        }

        stage('publish results') {
            steps {
                archiveArtifacts artifacts: "${BUILD_NUMBER}/JMeter.jtl, ${BUILD_NUMBER}/HtmlReport/**"
            }
        }
    }
}