export HOST_IP=$(ip route get 1 | awk '{print $7}')
docker compose -f docker-compose-dev.yml --env-file .env.dev up -d