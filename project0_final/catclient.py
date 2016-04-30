#Client
import socket
import sys
import time

HOST = "catserver"    # The remote host
PORT = int(sys.argv[2])              # The same port as used by the server
DATA = open(sys.argv[1])

#build the client dictionary
myfile = []
while 1:
	line = DATA.readline()
	if not line:
		break
	myfile.append(line.upper())
DATA.close()

#conner case
if not myfile:
	print 'Empty file'
else:
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((HOST, PORT))
	for i in xrange(0,10):
		s.sendall('LINE\n')
		data = s.recv(1024)
		#chech if the word is in string.txt
		if data in myfile:
			print 'OK'
		else:
			print 'MISSING'
		#every 3 seconds
		time.sleep(3)
	s.close()