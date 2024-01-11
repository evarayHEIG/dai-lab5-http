# dai-lab5-http

## Step 1

## Step 2

## Step 3
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

## Step 4

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