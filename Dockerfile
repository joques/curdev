FROM amazoncorretto:11.0.26-alpine3.18

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

RUN git clone --depth 1 https://github.com/cakesolutions/scala-kafka-client.git /app/scala-kafka-client \
  && cd /app/scala-kafka-client \
  && git checkout tags/v2.3.1 \
  && sbt publishLocal

RUN sbt update

COPY src/ ./src/

RUN sbt clean compile

CMD ["/bin/bash", "-c", "nohup java -jar /app/target/scala-2.12/yester-assembly-0.2.5.jar > /dev/null 2>&1 & tail -f /dev/null"]
