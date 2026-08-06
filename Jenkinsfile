pipeline {
    agent any

    parameters {
        booleanParam(name: 'PUBLISH_ARTIFACTS', defaultValue: true,
            description: 'Publish build artifacts to Jenkins for downstream deployment.')
    }

    tools {
        maven 'Maven-3.9'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                sh 'cmake --version'
                sh 'g++ --version | head -1'
                sh 'mvn -version | head -4'
            }
        }

        stage('Build') {
            steps {
                dir('native-hello') {
                    sh 'cmake -S . -B build -DCMAKE_BUILD_TYPE=Release'
                    sh 'cmake --build build --parallel'
                }
                dir('native-hello') {
                    sh 'ctest --test-dir build --output-on-failure'
                }
                dir('server-osgi') {
                    sh 'mvn -B -ntp clean package'
                }
            }
        }

        stage('Package') {
            steps {
                sh '''
                    mkdir -p dist
                    cp native-hello/build/hello-client dist/
                    cp server-osgi/hello-api/target/hello-api-1.0.0.jar dist/
                    cp server-osgi/hello-service/target/hello-service-1.0.0.jar dist/
                    tar -czf dist/ci-cpp-osgi-demo-${BUILD_NUMBER}.tar.gz -C dist hello-client hello-api-1.0.0.jar hello-service-1.0.0.jar
                '''
            }
        }

        stage('Mock deploy and verify') {
            steps {
                sh '''
                    EXPECTED_MESSAGE=$(tr -d '\\r\\n' < config/hello-message.txt)
                    PORT=9090 python3 mock/hello_endpoint.py > /tmp/hello-endpoint.log 2>&1 &
                    MOCK_PID=$!
                    trap 'kill $MOCK_PID 2>/dev/null || true' EXIT
                    sleep 1
                    test "$(curl --fail --silent http://127.0.0.1:9090/hello)" = "$EXPECTED_MESSAGE"
                '''
            }
        }

        stage('Publish artifacts') {
            when {
                expression { params.PUBLISH_ARTIFACTS }
            }
            steps {
                archiveArtifacts artifacts: 'dist/*', fingerprint: true
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}
