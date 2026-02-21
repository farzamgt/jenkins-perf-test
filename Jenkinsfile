pipeline {
    agent any

    stages {
        stage('Check Files') {
            steps {
                echo "Listing workspace files:"
                sh "ls -R $WORKSPACE"
                echo "Checking JMeter script:"
                sh "test -f $WORKSPACE/jmeter/test.jmx && echo 'test.jmx exists' || echo 'test.jmx missing'"
            }
        }

        stage('Run JMeter') {
            steps {
                sh """
                mkdir -p $WORKSPACE/reports
                docker run --rm \
                  -v $WORKSPACE:/test \
                  -v $WORKSPACE/reports:/reports \
                  justb4/jmeter:5.5 \
                  -n \
                  -t /test/jmeter/test.jmx \
                  -l /reports/results.jtl \
                  -e -o /reports/html
                """
            }
        }

        stage('Publish Report') {
            steps {
                archiveArtifacts artifacts: 'reports/results.jtl'

                publishHTML([
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'reports/html',
                    reportFiles: 'index.html',
                    reportName: 'JMeter Report'
                ])
            }
        }
    }
}