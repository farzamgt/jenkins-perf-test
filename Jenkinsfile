pipeline {

    agent any

    parameters {
        string(name: 'USERS',      defaultValue: '5',   description: 'Number of virtual users')
        string(name: 'RAMPUP',     defaultValue: '10',  description: 'Ramp-up time in seconds')
        string(name: 'DURATION',   defaultValue: '600', description: 'Test duration in seconds')
        string(name: 'BASE_URL',   defaultValue: 'http://localhost', description: 'Target URL for test')
        string(name: 'ASSERTION',  defaultValue: 'order', description: 'Assertion type')
    }

    stages {

        stage('clone git repo') {
            steps {
                git branch: 'gatling', url: 'https://github.com/farzamgt/jenkins-perf-test.git'
            }
        }

        stage('configure') {
            steps {
                bat "if not exist %WORKSPACE%\\reports\\run-%BUILD_NUMBER% mkdir %WORKSPACE%\\reports\\run-%BUILD_NUMBER%"
            }
        }

        stage('run test') {
            steps {
                dir("gatling") {
                    bat """
                    mvn -U gatling:test ^
                        -Dgatling.simulationClass=simulations.PerfTestSimulation ^
                        -Dusers=%USERS% ^
                        -DrampUp=%RAMPUP% ^
                        -Dduration=%DURATION% ^
                        -DbaseUrl=%BASE_URL% ^
                        -DassertionType=%ASSERTION%
                    """
                }
            }
        }

        stage('collect results') {
            steps {
                bat "xcopy /E /I /Y gatling\\target\\gatling %WORKSPACE%\\reports\\run-%BUILD_NUMBER%\\gatling"
            }
        }

        stage('publish results') {
            steps {
                archiveArtifacts artifacts: "reports/run-${BUILD_NUMBER}/**", fingerprint: true
            }
        }
    }
}