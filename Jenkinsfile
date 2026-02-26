pipeline {
    agent any

    environment {
        REPORT_DIR = "${WORKSPACE}/reports/run-${BUILD_NUMBER}"
    }

    stages {
        stage('Clone Git repo') {
            steps {
                git branch: 'gatling-test', url: 'https://github.com/farzamgt/jenkins-perf-test.git'
            }
        }

        stage('Prepare report folder') {
            steps {
                sh "mkdir -p ${REPORT_DIR}"
            }
        }

        stage('Run Gatling tests') {
            steps {
                dir("${WORKSPACE}/gatling") {
                    sh """
                    docker run --rm \
                        --network pte-network \
                        -v ${WORKSPACE}/gatling:/tests \
                        -v ${REPORT_DIR}:/tests/target/gatling \
                        gatling/gatling:latest \
                        mvn clean install -U gatling:test \
                            -Dusers=3 \
                            -DrampUp=10 \
                            -DbaseUrl=http://wp:80 \
                            -DassertionType=order
                    """
                }
            }
        }

        stage('Publish results') {
            steps {
                archiveArtifacts artifacts: "reports/run-${BUILD_NUMBER}/**", fingerprint: true
            }
        }
    }
}