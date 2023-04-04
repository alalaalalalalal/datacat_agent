#메이븐 빌드(jar생성)
mvn clean package -Dmaven.test.skip=true #무한루프때문에 테스트 돌리면 안끝남...
#도커 빌드 
docker build -f ./Dockerfile -t datacat_agent . --platform linux/amd64