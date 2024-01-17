# Labo5 HTTP Web infrastructure

## Project description

The aim of this project, as seen in the [instructions](LABO_INSTRUCTIONS.md) document, is to establish a web
infrastructure containing a
static website, a dynamic HTTP API, and a reverse proxy. To achieve this, we use Java for API implementation,
employing the `Javalin` framework. The web server was constructed using `nginx`, and for the reverse proxy
functionality, we opted for `traefik`. The entire infrastructure is deployed seamlessly through the assistance
of `docker`
and  `docker-compose`.Additionally, the capability to add or remove server instances is facilitated by the use
of `portainer`.

## Instruction to set up the infrastructure

In order to set up the infrastructure, the following steps must be followed:

1. Open a terminal and navigate to the root directory of the project.
2. If you are on Windows, make sure to open Docker Desktop.
3. Go to the ```docker``` directory and run the command
   ```shell
    docker compose up
   ```

or

   ```shell 
    docker compose up -d --scale <instance_name>=<count>
 ```

4. Docker should download all the necessary images and build the containers the first time you run it.
5. Once the containers are up and running, you can access the :
    - Static website: http://localhost:8000/ or https://localhost:443
    - Dynamic API: http://localhost:8000/api/songs or https://localhost/api/songs
    - Traefik dashboard: http://localhost:8080/
    - Portainer dashboard: http://localhost:9000/ or https://localhost:9443/

## Step 1 : Static Web site

In this step, we created a dockerfil to start the nginx server. We used the official `nginx:latest` image from
dockerhub.
We downladed a html template to reproduce the website added the file to the nginx folder. Then we created a Dockerfile
and
a [nginx.conf](docker/nginx/nginx.conf) file to configure the server listening on port 80 and giving the location files
for the website.
In the [dockerfile](docker/nginx/Dockerfile), we used the `COPY` command to copy the `nginx.conf` file to the container
as well as the website files,
and finally we tell it to use the port 80.
When all of this is done, we can build the image and run the container with the following commands:

TODO: pas sure que ce soit juste et ça marche pas chez moi

```shell
docker build -t dockerfile .
docker run -d -p 8080:80 site-trololo
```

Le site devrait pouvoir être accédé à l'adresse http://localhost:8080/

## Step 2 : Docker Compose

Once the static website is working, we can create a docker-compose file to run the website and the other containers
needed for the rest of the project. We created a [docker-compose.yml](docker/docker-compose.yml) file in the docker
folder.
We added the nginx service and we connected the `build`and `context` command to be able to rebuild the containers by
connecting
the Dockerfile to the service. We also linked the port 80 of the container to the port 8080 of the host. Here's
the code we added to the first docker-compose file:

```dockerfile
services:

  site-trololo:
    # When a build subsection is present for a service, Compose ignores the image attribute for the corresponding
    # service, as Compose can build an image from source
    build:
      # context specifies where to find the necessary files to build the image
      context: nginx
      # dockerfile specifies the name of the Dockerfile that contains the steps to build the image within
      # the defined context
      dockerfile: Dockerfile
    ports:
      - "8181:80"
    # volumes allows you to mount a directory from the host into the container
    volumes:
      - ./docker/nginx/website:/var/www/html
```

## Step 3 : HTTP API server

During this phase, we developed the API using the Javalin framework in Java. With Javalin, a server was can be created,
enabling the different endpoint routes for the API we made. The API is able to handle GET, POST, PUT and DELETE requests.
Our basic API concept gives different songs and there author as a json format and all the songs are stored in a `ConcurrentHashMap`
which are identified by an id (int). The different routes are:
- GET /api/songs -> returns all the songs
- GET /api/songs/{id}
- POST /api/songs
- PUT /api/songs/{id}
- DELETE /api/songs/{id}

When all the routes were implemented, we built the project with `maven` and created a [Dockerfile](docker/api/Dockerfile) to build the image of the API. 
We used the ` eclipse-temurin:17-jdk-focal` image from dockerhub to build the API with docker. 
We copied the jar file from the target folder to the container exposed the port 7000 and run the jar file with the command `java -jar api.jar`.
Finally, in the docker-compose file, we added the api service by giving the `context` and `dockerfile` command to find the dockerfile/build it
and linked the port 7000 of the container to the port 7000 of the host. Here how the docker-compose file looked like:

```dockerfile
services:

  site-trololo:
    # When a build subsection is present for a service, Compose ignores the image attribute for the corresponding
    # service, as Compose can build an image from source
    build:
      # context specifies where to find the necessary files to build the image
      context: nginx
      # dockerfile specifies the name of the Dockerfile that contains the steps to build the image within
      # the defined context
      dockerfile: Dockerfile
    ports:
      - "8181:80"
    # volumes allows you to mount a directory from the host into the container
    volumes:
      - ./docker/nginx/website:/var/www/html

  api-songs:
    build:
      context: api
      dockerfile: Dockerfile
    ports:
      - "7000:7000"
```

## Step 4 : Reverse proxy with Traefik

We used port 8000 because port 80 was not available on our machines.

```dockerfile
services:

  reverse-proxy:
    image: traefik:v2.10
    # Enables the web UI and tells Traefik to listen to docker
    command: --api.insecure=true --providers.docker --log.level=INFO
    volumes:
      # So that Traefik can listen to the Docker events
      - /var/run/docker.sock:/var/run/docker.sock
    ports:
      # the http port
      - "8000:80"
      # Traefik dashboard
      - "8080:8080"

  web:
    # When a build subsection is present for a service, Compose ignores the image attribute for the corresponding
    # service, as Compose can build an image from source
    build:
      # context specifies where to find the necessary files to build the image
      context: nginx
      # dockerfile specifies the name of the Dockerfile that contains the steps to build the image within
      # the defined context
      dockerfile: Dockerfile
    deploy:
      replicas: 3
    labels:
        - "traefik.enable=true"
        - "traefik.http.routers.web.rule=Host(`localhost`) && !PathPrefix(`/api`)"
        - "traefik.http.services.web.loadbalancer.server.port=80"

  api:
    build:
      context: api
      dockerfile: Dockerfile
    deploy:
      replicas: 3
    labels:
        - "traefik.enable=true"
        - "traefik.http.routers.api.rule=Host(`localhost`) && PathPrefix(`/api`)"
        - "traefik.http.services.api.loadbalancer.server.port=7000"
```

# Step 5

docker compose up -d --scale <instance_name>=<count>

# Step 6

```dockerfile
services:

  reverse-proxy:
    image: traefik:v2.10
    # Enables the web UI and tells Traefik to listen to docker
    command: --api.insecure=true --providers.docker --log.level=INFO
    volumes:
      # So that Traefik can listen to the Docker events
      - /var/run/docker.sock:/var/run/docker.sock
    ports:
      # the http port
      - "8000:80"
      # Traefik dashboard
      - "8080:8080"

  web:
    # When a build subsection is present for a service, Compose ignores the image attribute for the corresponding
    # service, as Compose can build an image from source
    build:
      # context specifies where to find the necessary files to build the image
      context: nginx
      # dockerfile specifies the name of the Dockerfile that contains the steps to build the image within
      # the defined context
      dockerfile: Dockerfile
    deploy:
      replicas: 3
    labels:
        - "traefik.enable=true"
        - "traefik.http.routers.web.rule=Host(`localhost`) && !PathPrefix(`/api`)"
        - "traefik.http.services.web.loadbalancer.server.port=80"

  api:
    build:
      context: api
      dockerfile: Dockerfile
    deploy:
      replicas: 3
    labels:
        - "traefik.enable=true"
        - "traefik.http.routers.api.rule=Host(`localhost`) && PathPrefix(`/api`)"
        - "traefik.http.services.api.loadbalancer.server.port=7000"
        - "traefik.http.services.api.loadBalancer.sticky.cookie=true"
        - "traefik.http.services.api.loadBalancer.sticky.cookie.name=api-cookie"

```

## Step 7

generate a self-signed certificate using the command

```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -sha256 -days 365
```

traefik.yaml

```dockerfile
providers:
  docker:
    endpoint: "unix:///var/run/docker.sock"

## Static configuration
entryPoints:
  http:
    address: ":80"

  https:
    address: ":443"

tls:
  certificates:
    - certFile: /etc/traefik/certificates/cert.pem
      keyFile: /etc/traefik/certificates/key.pem

api:
  dashboard: true
  insecure: true

```

docker-compose.yml

```dockerfile
services:

  reverse-proxy:
    image: traefik:v2.10
    volumes:
      # So that Traefik can listen to the Docker events
      - /var/run/docker.sock:/var/run/docker.sock
      # Mounting the certificate and the key
      - ./certificate:/etc/traefik/certificates
      ## Mouting the treafik configuration file
      - ./traefik.yaml:/etc/traefik/traefik.yaml
    ports:
      # the http port
      - "8000:80"
      # the https port
      - "443:443"
      # Traefik dashboard
      - "8080:8080"

  web:
    # When a build subsection is present for a service, Compose ignores the image attribute for the corresponding
    # service, as Compose can build an image from source
    build:
      # context specifies where to find the necessary files to build the image
      context: nginx
      # dockerfile specifies the name of the Dockerfile that contains the steps to build the image within
      # the defined context
      dockerfile: Dockerfile
    deploy:
      replicas: 2
    labels:
        - "traefik.http.services.web.loadbalancer.server.port=80"
        - "traefik.http.routers.secureweb.tls=true"
        - "traefik.http.routers.secureweb.rule=Host(`localhost`) && !PathPrefix(`/api`)"
        - "traefik.http.routers.secureweb.entrypoints=https"
        - "traefik.http.routers.web.rule=Host(`localhost`) && !PathPrefix(`/api`)"
        - "traefik.http.routers.web.entrypoints=http"

  api:
    build:
      context: api
      dockerfile: Dockerfile
    deploy:
      replicas: 2
    labels:
        - "traefik.http.services.api.loadbalancer.server.port=7000"
        - "traefik.http.routers.secureapi.tls=true"
        - "traefik.http.routers.api.rule=Host(`localhost`) && PathPrefix(`/api`)"
        - "traefik.http.routers.api.entrypoints=http"
        - "traefik.http.routers.secureapi.rule=Host(`localhost`) && PathPrefix(`/api`)"
        - "traefik.http.routers.secureapi.entrypoints=https"
        - "traefik.http.services.api.loadBalancer.sticky.cookie=true"
        - "traefik.http.services.api.loadBalancer.sticky.cookie.name=api-cookie"

```

portnair chez rafou:
admin
pourquoicamarchepaschezeva

## Step optional 1

```dockerfile
portainer:
    image: portainer/portainer-ce
    command: -H unix:///var/run/docker.sock # specifying the Docker socket path
    ports:
      # port to access portainer web interface with http
      - "9000:9000"
      # port to access portainer web interface with https
      - "9443:9443"
    volumes:
        # Mount the Docker socket from the host into the container
        - /var/run/docker.sock:/var/run/docker.sock
        - portainer_data:/data
        # Mount the directory containing SSL/TLS certificates
        - ./certificate:/certs

volumes:
  portainer_data:
```