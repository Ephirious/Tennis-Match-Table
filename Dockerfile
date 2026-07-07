FROM maven:4.0.0-rc-4-eclipse-temurin-25-alpine AS builder

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn clean package

FROM tomcat:11-jre25

WORKDIR /app

RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

RUN mv /usr/local/tomcat/ /app

COPY --from=builder /app/target/*.war ./tomcat/webapps/api.war

# COPY /server.xml ./tomcat/conf/

RUN chmod +x tomcat/bin/catalina.sh

EXPOSE 8080

CMD ["./tomcat/bin/catalina.sh", "run"]
