docker rm -f $(docker ps -a -q)

docker-machine create --driver virtualbox yuewangvm
eval "$(docker-machine env yuewangvm)"
#create a network bridge
docker network create -d bridge mynetwork
#data container
docker build -f DataDockerfile -t dataimage .
docker run -it -d -P --name datacontainer dataimage:latest
#server container
docker build -f ServerDockerfile -t serverimage .
docker run -d -it --net=mynetwork --name catserver \
--volumes-from datacontainer serverimage:latest python /src/catserver.py /data/string.txt 2001
#clieng container
docker build -f ClientDockerfile -t clientimage .
docker run -it -P --net=mynetwork --name catclient  \
--volumes-from datacontainer clientimage:latest python /src/catclient.py /data/string.txt 2001

#docker logs -f catserver
echo 'the output of the client catched by docker logs: '
docker logs -f catclient

exit 

