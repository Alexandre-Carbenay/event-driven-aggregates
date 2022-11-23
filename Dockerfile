FROM openjdk:17-slim as builder
ARG JAR_FILE=build/libs/library-loan.jar
COPY ${JAR_FILE} library-loan.jar
RUN java -Djarmode=layertools -jar library-loan.jar extract

FROM openjdk:17-slim
RUN apt-get update -y && apt-get install -y wget
RUN groupadd -r library && useradd -r -s /bin/false -g library library
WORKDIR /opt/library-loan
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
RUN chown -R library:library /opt/library-loan
USER library:library
ENTRYPOINT ["java", "--enable-preview", "org.springframework.boot.loader.JarLauncher"]
