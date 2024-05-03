FROM ubuntu

RUN apt update \
    && apt install -y curl \
    && curl -fsSL https://deb.nodesource.com/setup_19.x | bash - \
    && apt update \
    && apt remove -y curl \
    && apt install -y openjdk-21-jre nodejs libgtk-3-0 libdbus-glib-1-2 libgbm1 \
    && apt clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY . .

RUN ./gradlew build \
    && rm -rf ~/.gradle

CMD ["java", "-jar", "build/libs/wasp-api-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080