# Stage 1: Compile and Build angular codebase

# Use official node image as the base image
FROM node:18.18-alpine as build

# Set the working directory
WORKDIR /angular/app

# Add the source code to app
COPY ./ /angular/app/

# Install all the dependencies
RUN npm install

# Generate the build of the application
RUN npm run build --prod


# Stage 2: Serve app with nginx server

# Use official nginx image as the base image
FROM nginx:latest

#COPY nginx.conf /etc/nginx/nginx.conf

# Copy the build output to replace the default nginx contents.
COPY --from=build /angular/app/dist/news-ui /usr/share/nginx/html

# Expose port 80
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]


#Note command excute docker
#docker build -t quan89/news-ui .
#docker image ls
#docker run --network news-mysql-angular --name news-ui-container -p 4200:80 -d quan89/news-ui
#docker ps
#docker login -u <username> -p <password>
#docker push quan89/news-ui-image:latest

#Start all container
#docker container start $(docker container ls -a -q)

#Stop all container
#docker container stop $(docker container ls -a -q)