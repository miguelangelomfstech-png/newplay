pipeline {
  // Use a plain agent and run Playwright inside a Docker container via the Docker CLI.
  // This avoids the Jenkins "agent docker" requirement (Docker Pipeline plugin).
  agent any
  environment {
    CI = 'true'
    NODE_ENV = 'test'
    // Use workspace-local npm cache to speed up installs between builds
    NPM_CONFIG_CACHE = "${WORKSPACE}/.npm-cache"
    // Image to use for running tests
    PLAYWRIGHT_IMAGE = 'mcr.microsoft.com/playwright:v1'
  }
  options {
    // keep build logs for a week
    buildDiscarder(logRotator(numToKeepStr: '10'))
    timestamps()
  }
  stages {
    stage('Checkout') {
      steps {
        // `checkout scm` is only available when the job is configured as "Pipeline script from SCM"
        // or Multibranch Pipeline. To support both cases (job-from-SCM and job-created-from-XML),
        // try `checkout scm` and fall back to a Git checkout using environment variables.
        script {
          try {
            checkout scm
          } catch (err) {
            echo "checkout scm not available in this job type: ${err.toString()}"
            if (!env.GIT_URL) {
              error('GIT_URL not set. Configure the job as "Pipeline script from SCM" or set GIT_URL and optionally GIT_CREDENTIALS_ID as job environment variables.')
            }
            // Perform a git checkout using provided environment variables
            def branchSpec = env.BRANCH_NAME ? "*/${env.BRANCH_NAME}" : '*/main'
            checkout([$class: 'GitSCM', branches: [[name: branchSpec]], doGenerateSubmoduleConfigurations: false,
                      extensions: [[$class: 'CleanBeforeCheckout']], submoduleCfg: [],
                      userRemoteConfigs: [[url: env.GIT_URL, credentialsId: env.GIT_CREDENTIALS_ID ?: '']]])
          }
        }
      }
    }
    stage('Install dependencies (in container)') {
      steps {
        // Run npm ci inside the Playwright container. Mount workspace so artifacts remain.
        sh "docker run --rm -v \"${WORKSPACE}\":/work -w /work --shm-size=1g ${PLAYWRIGHT_IMAGE} bash -lc \"npm ci --cache '$NPM_CONFIG_CACHE' --prefer-offline\""
      }
    }
    stage('Install Playwright browsers (in container)') {
      steps {
        sh "docker run --rm -v \"${WORKSPACE}\":/work -w /work --shm-size=1g ${PLAYWRIGHT_IMAGE} bash -lc \"npx playwright install --with-deps\""
      }
    }
    stage('Run tests (in container)') {
      steps {
        // Run Playwright Test inside container and generate HTML report and JUnit XML
        sh "docker run --rm -v \"${WORKSPACE}\":/work -w /work --shm-size=1g ${PLAYWRIGHT_IMAGE} bash -lc \"npx playwright test --reporter=list,junit\""
      }
    }
  }
  post {
    always {
      // Archive Playwright report and raw test-results
      archiveArtifacts artifacts: 'playwright-report/**, test-results/**, artifacts.zip', fingerprint: true
      // Rename junit file to include build number (if present) to avoid overwrite between concurrent builds
      sh '''
        mkdir -p test-results/junit || true
        for f in test-results/junit/*.xml; do
          if [ -f "$f" ]; then
            mv "$f" "test-results/junit/results-${BUILD_NUMBER}.xml" || true
            break
          fi
        done
      '''
      // Publish JUnit test results so Jenkins can show them in the UI
      junit allowEmptyResults: true, testResults: 'test-results/junit/*.xml'
      // optionally publish junit if you configure the reporter
      // junit 'test-results/**/*.xml'
    }
    success {
      echo 'Tests passed.'
    }
    failure {
      echo 'There are test failures. Check the archived artifacts/playwright-report.'
    }
  }
}
