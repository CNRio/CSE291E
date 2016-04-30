#Server
import socket
import sys

HOST = ''
PORT = int(sys.argv[2])              # Arbitrary non-privileged port
DATA = open(sys.argv[1])

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(1)
conn, addr = s.accept()
#read string.txt as our dictionary
dic = DATA.readlines()
DATA.close()

#conner case
if not dic:
	print 'Empty file'
else:	
	i = 0
	while 1:
	    data = conn.recv(1024)
	    if not data: break
	    #start over at the top if reach the bottom
	    if data == 'LINE\n':
	    	if i < len(dic):
	    		line = dic[i].upper()
	    		i = i + 1
	    	else:
	    		i = 0
	    conn.sendall(line)
	conn.close()
