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

