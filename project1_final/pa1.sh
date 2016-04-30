docker-machine create --driver virtualbox yuewangvm
eval "$(docker-machine env yuewangvm)"

#create a network bridge
docker network create -d bridge mynetwork

#server container
docker build -f ServerDockerfile -t serverimage .
echo 'Start running server'
docker run -d -it --net=mynetwork --name catserver \
serverimage:latest java -cp '.:/src/rmi.jar' ServerTest
#client container
docker build -f ClientDockerfile -t clientimage .
echo 'Start running client'
docker run -it -P --net=mynetwork --name catclient  \
clientimage:latest java -cp '.:/src/rmi.jar' ClientTest catserver 9999

docker stop catclient
docker rm catclient
docker stop catserver
docker rm catserver
#docker rm $(docker ps -a -q)
#docker rmi $(docker images | grep "^<none>" | awk '{print $3}')

exit 

