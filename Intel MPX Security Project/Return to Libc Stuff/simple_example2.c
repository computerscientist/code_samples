#include <stdio.h>
#include <stdlib.h>
#include <string.h>

inline void string_copy(char *dst, char *src)
{
	while(*src!='\0')
	{
		*dst=*src;
		src++;
		dst++;
	}
}

int main(int argc, char ** argv)
{
	char buffer[1000];
	char str[10]="/bin/sh";

	memset(buffer, 0, 1000);

	if(argc!=2)
	{
		printf("Error: parameter format is: %s <inputString>\n", argv[0]);
		return 1;
	}

	string_copy(buffer, argv[1]);
	printf("%p\n", str);
	return 0;
}
