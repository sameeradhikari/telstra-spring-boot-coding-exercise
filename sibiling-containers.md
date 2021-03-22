# The problem

How to start a dependent container with a process already in a container?

- Install docker or some container application in the container. Commenly called Docker in Docker.
- Get the container to start another container outside of the current container. Commenly called Docker out of Docker or sibling containers.

Why sibling containers?

- The fabric8io docker-maven-plugin works with sibling containers.
- Not installing docker in a docker container makes live alot easier, smaller container and simpler networking.

# Docker and sibling containers

## Docker server

![Docker Server](/home/brianh/Images/docker-server.png)

## Docker server and client

![Docker Server and Client](/home/brianh/Images/docker-server-and-client.png)

## Docker and sibling mount

![Docker and sibling mount](/home/brianh/Images/docker-sibling-mount.png)

# How?

***Example docker run command for primary container***

This uses the container from https://git02.ae.sda.corp.telstra.com/projects/FEAPI/repos/docker-maven/browse that has been pushed to `docker-registry-v2.ae.sda.corp.telstra.com/feapi/maven:3-jdk-11`.

```
docker run \
    --rm \
    -u $(id -u):$(stat -c '%g' /var/run/docker.sock) \
    --name="sibling-build" \
    --network=host \
    -v "$PWD":/work \
    -v ~/.m2:/m2 \
    docker-registry-v2.ae.sda.corp.telstra.com/feapi/maven:3-jdk-11 \
    mvn clean install
```

* Need to ensure the container has permissions to access `/var/run/docker.sock`. Not all hosts use the same group id.

```
-u $(id -u):$(stat -c '%g' /var/run/docker.sock)
```

* Need to mount the host `/var/run/docker.sock` in the container. 

```
-v /var/run/docker.sock:/var/run/docker.sock
```

***Note:*** Host networking is used to make things easier. The maven reserve ports functionality is used to prevent port conflicts.

# Spring Boot Coding Exercise example.

* Needs a container based on `gturnquist-quoters` to provide endpoints for functional tests.
* Uses fabric8io docker-maven-plugin to create, start and stop the dependent container.

Run normally with

```
mvn clean install
```

Run using the container with above example run command.

## Why this works

- The `/var/run/docker.sock` is just a connection point to the dockerd rest api. https://docs.docker.com/engine/api/

- The fabric8io docker-maven-plugin uses the dockerd rest api directly.

## Demo of creating containers from sibling container.

Start interactive container
```
docker run \
    --rm \
    -u $(id -u):$(stat -c '%g' /var/run/docker.sock) \
    --name="sibling-build" \
    --network=host \
    -v "$PWD":/work \
    -v ~/.m2:/m2 \
    -it \
    -v /var/run/docker.sock:/var/run/docker.sock \
    docker-registry-v2.ae.sda.corp.telstra.com/feapi/maven:3-jdk-11 \
    /bin/bash
```

List containers with curl
```
curl --unix-socket /var/run/docker.sock http:/v.1.41/containers/json
```

List images with curl
```
curl --unix-socket /var/run/docker.sock http:/v.1.41/images/json
```

create a container with curl
```
curl \
  --silent \
  --unix-socket /var/run/docker.sock \
  http:/v.1.41/containers/create?name=quoters-curl \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{ "Image": "gturnquist-quoters:latest", 
        "ExposedPorts": {
            "8080/tcp": {}
        },
        "HostConfig": {
            "PortBindings": { "8080/tcp": [{ "HostPort": "8090" }] }
        }
    }'
```

start a container with curl
```
curl --unix-socket /var/run/docker.sock -X POST "http:/v.1.41/containers/quoters-curl/start"
```

Call quoters end point.
```
curl http://localhost:8090/api/random
```

stop a container curl
```
curl --unix-socket /var/run/docker.sock -X POST http:/v.1.41/containers/quoters-curl/stop
```

remove a container curl
```
curl --unix-socket /var/run/docker.sock -X DELETE http:/v.1.41/containers/quoters-curl
```

# Bonus adding ssh keys

**Build with ssh key on bamboo**

In `bamboo.ssh_key_password` is used to mask the contents.

This relies on a special entrypoint [entrypoint.sh](https://git02.ae.sda.corp.telstra.com/projects/FEAPI/repos/docker-maven/browse/entrypoint.sh) being used in the container.

```
docker run \
--rm \
-e WORKER_UID=$(id -u) \
-e SSH_KEY="${bamboo.ssh_key_password}" \
--name="feapi-build" \
--network=host \
-v "$PWD":/work \
-v ~/.m2:/m2 \
-v /var/run/docker.sock:/var/run/docker.sock \
docker-registry-v2.ae.sda.corp.telstra.com/feapi/maven:3-jdk-11 \
mvn clean install
```