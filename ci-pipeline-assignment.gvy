pipeline {
    agent any
    stages {
        stage('Compile') {
	         steps {
                // step1 
                echo 'compiling..'
		            git url: 'https://github.com/streamingsan/PetClinic.git'
		            sh script: '/opt/apache-maven-3.8.5/bin/mvn compile'
           }
        }
        stage('Code Review') {
	         steps {
                // step2
                echo 'codereview..'
		            sh script: '/opt/apache-maven-3.8.5/bin/mvn -P metrics pmd:pmd'
           }
	         post {
               success {
		             recordIssues enabledForFailure: true, tool: pmdParser(pattern: '**/target/pmd.xml')
               }
           }		
        }
        stage('Unit Test') {
	          steps {
                // step3
                echo 'unittest..'
	               sh script: '/opt/apache-maven-3.8.5/bin/mvn test'
            }
	          post {
               success {
                   junit 'target/surefire-reports/*.xml'
               }
            }			
        }
        /*stage('codecoverage') {

           tools {
              jdk 'java1.8'
           }
	         steps {
                // step4
                echo 'codecoverage..'
		            sh script: '/opt/apache-maven-3.8.5/bin/mvn cobertura:cobertura -Dcobertura.report.format=xml'
           }
	         post {
               success {
	               cobertura autoUpdateHealth: false, autoUpdateStability: false, coberturaReportFile: 'target/site/cobertura/coverage.xml', conditionalCoverageTargets: '70, 0, 0', failUnhealthy: false, failUnstable: false, lineCoverageTargets: '80, 0, 0', maxNumberOfBuilds: 0, methodCoverageTargets: '80, 0, 0', onlyStable: false, sourceEncoding: 'ASCII', zoomCoverageChart: false                  
               }
           }		
        }*/
        stage('Package') {
	         steps {
                // step5
                echo 'package......'
		            sh script: '/opt/apache-maven-3.8.5/bin/mvn package'	
           }		
        }
        stage('Deploy') {
	         steps {
                // step6
                echo 'deploy......'
		            sh script: 'cp target/*.war /opt/apache-tomcat-8.5.78/webapps/'	
           }		
        }
    }
}
