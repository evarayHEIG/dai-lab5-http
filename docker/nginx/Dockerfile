# base image for the Docker images
FROM nginx:latest

# copy the first file into the Docker image at the path given
# here, it is used to overwrite the default configutation
COPY nginx.conf /etc/nginx/conf.d/default.conf

# here, it is used copy the static website into the image
COPY website /var/www/html

# inform Docker that the container will listen on port 80
EXPOSE 80



