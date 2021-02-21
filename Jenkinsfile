pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
            post {
                success {
                    echo "Build Successfull!!!"
                }
                failure {
                    emailext (
                        subject: "Build Failed: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                        body: """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                                 <p>Building stage was failed!</p>
                                 <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                        recipientProviders: [[$class: 'CulpritsRecipientProvider']]
                    )
                }
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
                success {
                    echo "Test Successfull!!!"
                }
                failure {
                    emailext (
                        subject: "Testing Failed: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                        body: """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                                 <p>However some tests were failed!</p>
                                 <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                        recipientProviders: [[$class: 'CulpritsRecipientProvider']]
                    )
                }
            }
        }
        stage('Coverage Report') {
            steps {
                sh 'mvn jacoco:report'
                jacoco classPattern: '**/target', exclusionPattern: '**/*Test.class, **/digitization/*/application/**', inclusionPattern: '**/*.class'
            }
            post {
                success {
                    echo "Coverage Reporting Successfull!!!"
                    emailext (
                        subject: "Coverage Reporting Successful: Feature Branch - '${env.BRANCH_NAME}' - Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                        body: """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                                 <p>Covering report stage was successful!</p>
                                 <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                        presendScript: '''import jenkins.*
                                          import jenkins.model.*
                                          import hudson.model.*
                                          def branchName = run.getEnvVars()["BRANCH_NAME"]
                                          cancel =!("${branchName}".substring(0,7).equals("feature"));''',
                        recipientProviders: [developers()]
                    )
                }
                failure {
                    emailext (
                        subject: "Coverage Reporting Failed: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                        body: """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                                 <p>Covering report stage was failed!</p>
                                 <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                        recipientProviders: [[$class: 'CulpritsRecipientProvider']]
                    )
                }
            }
        }
        stage('Sonarqube analysis') {
            when {
               branch 'develop'
            }
            steps {
                sh 'mvn sonar:sonar -Dsonar.projectKey=ddsarchetype -Dsonar.host.url=http://172.29.64.232:8000 -Dsonar.login=eaee37da8315a830634695f8d96919e5f45a8974'
            }
            post {
                success {
                    echo "Sonarqube analysis successful"
                }
                failure {
                    emailext (
                        subject: "Sonarqube analysis failed: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                        body: """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                                 <p>Sonarqube analysis stage was failed!</p>
                                 <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                        recipientProviders: [[$class: 'CulpritsRecipientProvider']]
                    )
                }
            }
        }
        stage('Deploy') {
            when {
               branch 'develop'
            }
            steps {
                sh 'mvn -DskipTests deploy'
            }
            post {
                success {
                    echo "Deploying Successfull!!!"
                }
                failure {
                    emailext (
                        subject: "Deploying Failed: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                        body: """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                                 <p>Deploying stage was failed!</p>
                                 <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                        recipientProviders: [[$class: 'CulpritsRecipientProvider']]
                    )
                }
            }
        }
        stage('Deliver') {
            when {
               branch 'develop'
            }
            steps {
                script{

                    def remote = [:]
                    remote.name = "qa1"
                    remote.host = "172.29.64.230"
                    remote.allowAnyHosts = true

                    withCredentials([sshUserPrivateKey(credentialsId: 'docker-deploy', keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'userName')]) {

                        remote.user = userName
                        remote.identityFile = identity

                        def pom = readMavenPom()
                        PROJECT_NAME = pom.name
                        PROJECT_VERSION = pom.version

                        sshCommand remote: remote, command: 'docker pull ddsdockerregistry:7990/' + "${PROJECT_NAME}" + ':' + "${PROJECT_VERSION}"
                        sshCommand remote: remote, command: 'docker rm --force ' + "${PROJECT_NAME}", failOnError: false
                        sshCommand remote: remote, command: 'docker run --dns=172.26.9.50 --dns=172.26.34.24 --env PROFILE=qa -p 8001:8080 --name ' + "${PROJECT_NAME}" + ' -d ddsdockerregistry:7990/' + "${PROJECT_NAME}" + ':' + "${PROJECT_VERSION}"
                    }
                }
            }
            post {
                success {
                    echo "Delivering Successfull!!!"
                    emailext (
                        subject: "Pipeline job Completed: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                        body: """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                                 <p>Total pipeline job ran successfully!</p>
                                 <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                        recipientProviders: [developers()]
                    )
                }
                failure {
                    emailext (
                        subject: "Delivering Failed: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                        body: """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                                 <p>Delivery stage was failed!</p>
                                 <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                        recipientProviders: [[$class: 'CulpritsRecipientProvider']]
                    )
                }
            }
        }
    }
}