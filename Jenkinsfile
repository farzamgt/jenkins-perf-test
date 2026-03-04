pipeline {

    agent any

    parameters {
        string(name: 'ITERATIONS', defaultValue: '1', description: 'Number of times to run the Lighthouse flow')
        string(name: 'BASE_URL',  defaultValue: 'http://localhost:80', description: 'Target URL for Lighthouse tests')
    }

    stages {

        stage('clone git repo') {
            steps {
                git branch: 'lighthouse', url: 'https://github.com/farzamgt/jenkins-perf-test.git'
            }
        }

        stage('configure') {
            steps {
                bat """
                if not exist %WORKSPACE%\\reports\\lighthouse\\run-%BUILD_NUMBER% mkdir %WORKSPACE%\\reports\\lighthouse\\run-%BUILD_NUMBER%
                cd lighthouse
                set PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true
                npm install
                """
            }
        }

        stage('run test') {
            steps {
                dir("lighthouse") {
                    bat "set ITERATIONS=%ITERATIONS% && set BASE_URL=%BASE_URL% && node orderFlow.js"
                }
            }
        }

        stage('collect report') {
            steps {
                bat "copy lighthouse\\order-flow.html %WORKSPACE%\\reports\\lighthouse\\run-%BUILD_NUMBER%\\"
                bat "copy lighthouse\\order-flow.json %WORKSPACE%\\reports\\lighthouse\\run-%BUILD_NUMBER%\\"
                bat "copy lighthouse\\order-flow-aggregated-by-page.json %WORKSPACE%\\reports\\lighthouse\\run-%BUILD_NUMBER%\\"
            }
        }

        stage('publish results') {
            steps {
                archiveArtifacts artifacts: "reports/lighthouse/run-${BUILD_NUMBER}/*", fingerprint: true
            }
        }
    }
}