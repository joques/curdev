FROM amazoncorretto:11.0.26-alpine3.18

WORKDIR /app

COPY project/ ./project/
COPY build.sbt ./ 
COPY src/ ./src/

RUN apk add --no-cache bash curl git zip

SHELL ["/bin/bash", "-c"]

RUN curl -s "https://get.sdkman.io" | bash \
    && source "$HOME/.sdkman/bin/sdkman-init.sh" \
    && sdk install sbt \
    && ln -s $HOME/.sdkman/candidates/sbt/current/bin/sbt /usr/local/bin/sbt


RUN sbt sbtVersion

RUN git clone https://github.com/cakesolutions/scala-kafka-client.git /app/scala-kafka-client \
  && cd /app/scala-kafka-client \
  && git checkout tags/v2.3.1 \
  && sbt publishLocal


RUN sbt clean compile

CMD ["sbt run"]