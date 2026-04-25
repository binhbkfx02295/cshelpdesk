set -a;
source .env.dev.docker
set +a;

#check docker is running
if ! docker info > /dev/null 2>&1; then
  echo "Docker is not running. Please start Docker and try again."
  exit 1
fi

#run docker compose up -
echo "Running dependencies..."
docker compose -f docker-compose.dev.yml up -d

echo "Running the CShelpdesk app ..."
./mvnw spring-boot:run -DskipTests