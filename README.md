# How to Run Tests in Jenkins

This guide explains how to set up and run JMeter, Gatling, and Lighthouse tests using Jenkins.

1. Install Jenkins

Download and install Jenkins on your local machine.

Start the Jenkins service.

2. Sign in

Open the initial admin password file:

C:\ProgramData\Jenkins\.jenkins\secrets\initialAdminPassword

Use the password to log in as admin.

3. Open Jenkins in Browser

Navigate to:

http://localhost:8080

4. Create a New Job

Click New Item → choose Pipeline → give it a name → click OK.

5. Configure Pipeline

- Definition: Pipeline script from SCM

- SCM: Git

- Repository URL: https://github.com/farzamgt/jenkins-perf-test.git

- Branch Specifier: */jmeter → JMeter test, */gatling → Gatling test, */lighthouse → Lighthouse test

- Click Save.

6. Build with Parameters

7. Notes

For JMeter, make sure JMETER_HOME environment variable is set to your JMeter installation path.