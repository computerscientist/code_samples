#include <stdio.h>
#include <stdlib.h>
#include <string.h>

unsigned int get_sp()
{
	__asm__("movl %esp, %eax");
}

void string_copy(char *dst, char *src)
{
	while(*src!='\0')
	{
		*src=*dst;
		src++;
		dst++;
	}
}

int main(int argc, char ** argv)
{
	char buffer[1000];

	if(argc!=2)
	{
		printf("Error: parameter format is: %s <inputString>\n", argv[0]);
		exit(1);
	}

	string_copy(buffer, argv[1]);
	printf("0x%x\n", buffer);
	//printf("%s\n", buffer);

	return 0;
}
