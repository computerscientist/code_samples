#include <stdio.h>
#include <stdlib.h>

int main(int argc, char ** argv)
{
	char array[50];

	if(argc!=2)
	{
		printf("Error, program call format is: %s <index>\n", argv[0]);
		return 1;
	}

	array[atoi(argv[1])]='A';
}
