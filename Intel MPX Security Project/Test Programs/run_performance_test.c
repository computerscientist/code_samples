#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char ** argv)
{
	int i;

	if(argc<2 || argc>3)
	{
		printf("Error, call format is: %s <program_to_test> [-mpx-mode]\n", argv[0]);
		return 1;
	}

	else if(argc==3 && strcmp(argv[2], "-mpx-mode")!=0)
	{
		printf("Error: bad option for MPX capability\n");
		return 1;
	}

	for(i=0;i<500;i++)
	{
		char commandString[81];
		memset(commandString, 0, 81);
		sprintf(commandString, "$SDE_KIT/sde %s -- %s", (argc==3 ? argv[2] : ""), argv[1]);

		FILE *fp=popen(commandString, "r");
		char output_buffer[81];

		if(fp==NULL)
		{
			printf("Error running performance_test!\n");
			continue;
		}

		while(fgets(output_buffer, 81, fp)!=NULL)
			printf("%s", output_buffer);

		pclose(fp);
	}

	return 0;
}
