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
                sh "mkdir -p ${WORKSPACE}/reports/run-${BUILD_NUMBER}"
            }
        }

        stage('run test') {
            steps {
                dir("${WORKSPACE}/gatling") {
                    sh """
                    mvn clean install -U gatling:test \
                        -Dusers=${params.USERS} \
                        -DrampUp=${params.RAMPUP} \
                        -Dduration=${params.DURATION} \
                        -DbaseUrl=${params.BASE_URL} \
                        -DassertionType=${params.ASSERTION}
                    """
                }
            }
        }

        stage('collect results') {
            steps {
                sh "cp -r ${WORKSPACE}/gatling/target/gatling ${WORKSPACE}/reports/run-${BUILD_NUMBER}/"
            }
        }

        stage('publish results') {
            steps {
                archiveArtifacts artifacts: "reports/run-${BUILD_NUMBER}/**", fingerprint: true
            }
        }
    }
}