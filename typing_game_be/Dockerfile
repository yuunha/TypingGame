FROM eclipse-temurin:17-jre
WORKDIR /app
COPY typing_game_be-0.0.1-SNAPSHOT.jar app.jar
CMD ["/bin/sh", "-c", "java -Dserver.port=${PORT:-10000} -jar app.jar"]
#ENTRYPOINT ["/bin/sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
