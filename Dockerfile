FROM amazoncorretto:11.0.26-alpine3.18

ARG KAFKA_BROKER_HOST
ENV KAFKA_BROKER_HOST=${KAFKA_BROKER_HOST}

ARG ZOOKEEPER_HOST_URL
ENV ZOOKEEPER_HOST_URL=${ZOOKEEPER_HOST_URL}

ARG DB_URL
ENV DB_URL=${DB_URL}

ARG DB_USER
ENV DB_USER=${DB_USER}

ARG DB_PASSWORD
ENV DB_PASSWORD=${DB_PASSWORD}


WORKDIR /app

COPY project/ ./project/
COPY build.sbt ./ 

RUN apk add --no-cache bash curl git zip

SHELL ["/bin/bash", "-c"]

RUN curl -s "https://get.sdkman.io" | bash \
    && source "$HOME/.sdkman/bin/sdkman-init.sh" \
    && sdk install sbt \
    && ln -s $HOME/.sdkman/candidates/sbt/current/bin/sbt /usr/local/bin/sbt


RUN sbt sbtVersion

RUN git clone --branch v2.3.1 --single-branch https://github.com/cakesolutions/scala-kafka-client.git /app/scala-kafka-client \
  && cd /app/scala-kafka-client \
  && sbt publishLocal

RUN sbt update

COPY src/ ./src/

RUN sbt clean assembly

CMD ["java", "-jar", "/app/target/scala-2.12/yester-assembly-0.2.5.jar"]