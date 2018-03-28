FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD ./build/libs/merchant-gateway-0.1.0-SNAPSHOT.war app.war
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=JAVA_OPTS
ENV DB_URL=DB_URL
ENV DB_USER=DB_USER
ENV DB_PASS=DB_PASS
ENV ALFA_LOGIN=ALFA_LOGIN
ENV ALFA_PASS=ALFA_PASS
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar -Dspring.datasource.url=\"$DB_URL\" -Dspring.datasource.username=\"$DB_USER\" -Dspring.datasource.password=\"$DB_PASS\" -Dalfabank.login=\"$ALFA_LOGIN\" -Dalfabank.password=\"$ALFA_PASS\" /app.war" ]
# Debug port
EXPOSE 8000