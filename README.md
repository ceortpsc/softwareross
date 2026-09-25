# softwareross

This repository contains a Java application (Maven) and is configured with a Dockerfile and a GitHub Actions CI workflow that builds the JAR, builds a Docker image, and pushes it to GitHub Container Registry (GHCR).

Quick overview
- Build: Maven (package)
- JDK: Eclipse Temurin 21
- Docker base runtime: eclipse-temurin:21-jre
- Container registry: ghcr.io (GitHub Container Registry)
- Local runtime port: 8080

Getting started locally
1. Build the app with Maven:

   mvn -B package

2. Build the Docker image locally:

   docker build -t my-app:local .

3. Run with Docker:

   docker run --rm -p 8080:8080 my-app:local

Or use docker-compose:

   docker-compose up --build

CI / CD (GitHub Actions)
- The workflow in .github/workflows/ci.yml runs on pushes and PRs to main.
- It builds the Maven artifact, builds a Docker image, and pushes to GHCR using the repository's GITHUB_TOKEN (permissions: packages: write).

Publishing to GHCR
1. No manual secret is required for automated pushes from GitHub Actions when using the provided GITHUB_TOKEN. Ensure repository settings allow GitHub Actions to create and publish packages.
2. The image name is derived as: ghcr.io/<OWNER>/<REPO>:<TAG>

Customizing
- To change the exposed port, update Dockerfile and docker-compose.yml.
- To push to Docker Hub instead, update the workflow to log in to Docker Hub using a DOCKERHUB_USERNAME and DOCKERHUB_TOKEN secrets and change the image name accordingly.

If you want, next steps I can perform:
- Add a minimal sample Maven pom.xml and sample Hello World app (Recommended)
- Add repository-level docs and CONTRIBUTING.md
- Configure branch protection and release tagging

Tell me which of the above to create next, or say "apply everything" to scaffold a minimal runnable app and commit files.
