# base image for the Docker images
FROM nginx

# copy the first file into the Docker image at the path given
# here, it is used to overwrite the default configutation
COPY nginx.conf /etc/nginx/nginx.conf

# default command to run when a container is started from the image
CMD ["nginx", "-g", "deamon off"]

# inform Docker that the container will listen on port 80
EXPOSE 80
