FROM jenkins/jenkins:lts

USER root

# Встановлюємо docker CLI всередині контейнера
RUN apt-get update && apt-get install -y docker.io && rm -rf /var/lib/apt/lists/*

USER jenkins