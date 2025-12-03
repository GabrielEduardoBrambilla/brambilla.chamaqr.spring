docker build -t spring-backend -f Dockerfile.backend .

docker run -d -p 8443:8443 --name backend chamadaqr-backend:latest

docker run -d \
  -p 8443:8443 \
  --name backend \
  spring-backend
