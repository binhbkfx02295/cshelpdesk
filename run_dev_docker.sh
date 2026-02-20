mvn clean package -DskipTests

docker build -t cshelpdesk:latest .


echo "Running the CShelpdesk app ..."
docker run \
    -p 8080:8080 \
    --network local-dev-env \
    --name cshelpdesk-app \ 
    --env-file=.env.dev.docker cshelpdesk:latest