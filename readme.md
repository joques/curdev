# Introduction

_yester_ is part of a bigger application which aims at automating the curriculum development. In the overall implementation we adopted the **micro service** [architectural style](http://microservices.io/patterns/microservices.html). _yester_ is the **micro service** that manages all resources accessed during the curriculum development.

# Key Functionality

The key functionalities offered by _yester_ include:
* user management

# Early Design

The early design of the **micro service** should include a communication management component (message consumer and message producer); a database management component (offering an API to manipulate RethinkDB)

# Build and Run Image

```powershell
brew install direnv

echo 'eval "$(direnv hook zsh)"' >> ~/.zshrc   

source ~/.zshrc

direnv allow .
```

```powershell
docker build -f Dockerfile -t pdqa-microservices .

docker run -d \   
  -e KAFKA_BROKER_HOST="host.docker.internal:9092" \
  -e ZOOKEEPER_HOST_URL="host.docker.internal:2181" \
  -e DB_URL="host.docker.internal" \
  -e DB_USER="" \
  -e DB_PASSWORD="" \
  pdqa-microservices
  ```

# Design Notes
Before going any further one needs to take a closer look at the following points

Note1: one needs to be careful how the concurrency is managed here.
Note2: I need to design more carefully how many actors should be created for the management of the communicators (consumer and producer) and the db management API implementation
Note3: Need to identify what are the different types of messages that one should expect during the message consumption and production. Also the same need might apply for the DB API.
