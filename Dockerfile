FROM dialogds/ddsbase:1.0
ARG JAR_FILE
COPY ./target/${JAR_FILE} /usr/src/template/
WORKDIR /usr/src/template
EXPOSE 8080
ENTRYPOINT ["java","-Dserver.port=8080","-Dreactor.netty.http.server.accessLogEnabled=true","-jar", "service.jar"]