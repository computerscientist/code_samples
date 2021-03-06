#include <stdio.h>
#include <stdint.h>
#include <string.h>

void memory_copy(char *dst, char *src, size_t size)
{
	size_t i;
	for(i=0;i<size;i++)
		dst[i]=src[i];
}

int main(int argc, char ** argv)
{
	if(argc!=3)
	{
		printf("Error, call format is: %s <string> <amount_to_copy>\n", argv[0]);
		return 1;
	}

	char buffer[50];
	memset(buffer, 0, 50);
	memory_copy(buffer, argv[1], atoi(argv[2]));
	printf("%s\n", buffer);

	return 0;
}
