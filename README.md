# How to Run Tests in Jenkins

This guide explains how to set up and run JMeter, Gatling, and Lighthouse tests using Jenkins.

## 1. Install Jenkins

- Download and install Jenkins on your local machine.
- Start the Jenkins service.

## 2. Sign in

Open the initial admin password file:

```
C:\ProgramData\Jenkins\.jenkins\secrets\initialAdminPassword
```

## 3. Open Jenkins in Browser

Navigate to: http://localhost:8080

Use the password to log in as admin.

## 4. Create a Multibranch Pipeline

- Click New Item -> choose Multibranch Pipeline -> give it a name -> click OK.
- SCM: Git
- Repository URL: `https://github.com/farzamgt/jenkins-perf-test.git`
- Leave "Credentials" empty because the repo is public.
- Save

> Jenkins will automatically scan all branches and create jobs for each branch containing a `Jenkinsfile`.

## 5. Build and Run Tests

- Navigate to the branch job.
- Click Build with Parameters.
- Enter desired values (iterations, target URL, etc.) or use defaults.
- Jenkins will execute the pipeline and save artifacts automatically.

## 6. Notes

- For JMeter, make sure JMETER_HOME environment variable is set to your JMeter installation path.
- Branches: Each branch has its own Jenkinsfile, parameters, and tests.