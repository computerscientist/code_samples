# cracking token1
from Crypto.Cipher import AES
import hashlib

count =0
total = 3*33000*1000
print total


#info
HTTP_USER_AGENT = 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0'
server_addr = '152.2.130.15'
server_port = '80'
remote_addr = '152.23.21.145'
uid = '65534'
gid = '65534'
interval = '::'

token1='sNhXxAPqwI4vjWX3Kv4LC0iV3d2y/8dQaztmlKG5WAYQKuLB9CA9MZpaVqiEbj2y'
ciphertext = token1
ct = ciphertext.decode('base64')
#



for time in range(1366918767+56,1366918770+56):
  for pid in range(32768):
      #for remote_port in range(8859,8864):
      for remote_port in range(8000,9000):
        if count%500000 == 0:
	  print 'progress is ' , str(count/(total+0.001) *100), '%'

        count += 1

        data = interval + str(time) + interval + HTTP_USER_AGENT + interval + \
                str(pid) + interval + server_addr + interval + str(server_port) +\
                 interval + remote_addr + interval +\
                 str(remote_port) + interval + uid + interval + gid 



        m = hashlib.md5()
        m.update(data)
        key = m.digest()
	del m

        obj = AES.new(key,AES.MODE_CBC,key)




        answer = obj.decrypt(ct)

	if 'SecureToken' in answer.strip():
		f = open('output','w')
		f.write(data)
		f.write(answer.strip())
		f.close()
		print "found it"
		exit(0)

