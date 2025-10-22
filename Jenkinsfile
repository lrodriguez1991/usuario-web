pipeline {
    agent any

    environment {
        //SONAR_HOST_URL = "http://sonarqube:9000"
        //SONAR_TOKEN = credentials('sonar-token')
        //ARTIFACTORY_SERVER = 'artifactory'
        TARGET_REPO = 'libs-snapshot-local'
    }

    stages {
        stage('Build') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-21'
                    args '-e HOME=/root -u 0'
                }
            }
            steps {
                echo "Compilando proyecto Java..."
                sh 'mvn clean package -DskipTests'
                stash name: 'war-file', includes: 'target/*.war'
            }
        }

		stage('Test') {
		    agent {
		        docker {
		            image 'maven:3.9.6-eclipse-temurin-21'
		            args '-v $WORKSPACE:/workspace -w /workspace -e HOME=/root -u 0'
		        }
		    }
		    steps {
		        echo "Ejecutando pruebas unitarias..."
		        sh 'mvn -f /workspace/pom.xml test jacoco:report'
		    }
		    post {
		        always {
		            junit 'target/surefire-reports/*.xml'
		        }
		    }
		}

        /*stage('SonarQube Analysis') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-21'
                    args '-e HOME=/root -u 0'
                }
            }
            steps {
                echo "🔍 Analizando código con SonarQube..."
                withSonarQubeEnv('SonarQube-Server') {
                    sh """
                        mvn sonar:sonar \
                          -Dsonar.projectKey=usuario-web \
                          -Dsonar.host.url=$SONAR_HOST_URL \
                          -Dsonar.login=$SONAR_TOKEN
                    """
                }
            }
        }

        stage('Publish to Artifactory') {
            steps {
                script {
                    echo "Subiendo WAR a Artifactory mediante File Spec..."
                    unstash 'war-file' // Recupera el WAR generado

                    def server = Artifactory.server(ARTIFACTORY_SERVER)
                    def pom = readMavenPom file: 'pom.xml'
                    def groupIdPath = pom.groupId.replaceAll("\\.", "/")

                    def uploadSpec = """
                    {
                        "files": [
                            {
                                "pattern": "target/.*\\.war",
                                "target": "${TARGET_REPO}/${groupIdPath}/${pom.artifactId}/${pom.version}/",
                                "regexp": "true",
                                "props": "build.number=${env.BUILD_NUMBER};build.url=${env.RUN_DISPLAY_URL}"
                            }
                        ]
                    }
                    """
                    server.upload spec: uploadSpec
                }
            }
        }*/
    }

    post {
        success {
            echo "Pipeline completado exitosamente: WAR subido a Artifactory"
           // unstash 'war-file' // Asegúrate de que esté presente para archivarlo
           // archiveArtifacts artifacts: 'target/*.war', fingerprint: true
        }
        failure {
            echo "Falló el pipeline. Revisa los logs."
        }
    }
}
