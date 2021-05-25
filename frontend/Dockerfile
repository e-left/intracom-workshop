# First stage, build the application
FROM node:lts-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm i
COPY . .
RUN npm run build
RUN rm -rf node_modules .parcel-cache src .dockerignore .eslintrc.json Dockerfile package.json package-lock.json


# Second stage, copy the artifacts in a new stage and 
# build the image
FROM nginx:stable-alpine
COPY --from=build-stage /app /usr/share/nginx/html
# COPY --from=build-stage /app/index.html /var/share/nginx/html/
# COPY --from=build-stage /app/favicon.*.ico /var/share/nginx/html/
# COPY --from=build-stage /app/index.*.css /var/share/nginx/html/
# COPY --from=build-stage /app/index.*.js /var/share/nginx/html/
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]