#include <stdio.h>
#include <string.h>

int main(int argc, char ** argv)
{
	if(argc!=3)
	{
		printf("Error, call format is: %s <string> <amount_to_copy>\n", argv[0]);
		return 1;
	}

	char buffer[50];
	memset(buffer, 0, 50);
	memcpy(buffer, argv[1], atoi(argv[2]));
	printf("%s\n", buffer);

	return 0;
}
