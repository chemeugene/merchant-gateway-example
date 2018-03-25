FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD ./build/libs/merchant-gateway-0.1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=JAVA_OPTS
ENV DB_URL=DB_URL
ENV DB_USER=DB_USER
ENV DB_PASS=DB_PASS
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar -Dspring.datasource.url=\"$DB_URL\" -Dspring.datasource.username=\"$DB_USER\" -Dspring.datasource.password=\"$DB_PASS\" /app.jar" ]
# Debug port
EXPOSE 8000