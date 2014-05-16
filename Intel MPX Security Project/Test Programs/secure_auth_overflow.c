#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *protected_buffer;
void string_copy(char *dst, char *src);

int check_authentication(char *password) {
	int auth_flag = 0;
	char password_buffer[16];
	protected_buffer = __bnd_set_ptr_bounds(password_buffer, 16);
	string_copy(protected_buffer, password);

	if(strcmp(password_buffer, "brillig") == 0)
		auth_flag = 1;
	if(strcmp(password_buffer, "outgrabe") == 0)
		auth_flag = 1;

	return auth_flag;
}

void string_copy(char *dst, char *src)
{
	while(*src!=0)
	{
		*dst=*src;
		src++;
		dst++;
	}
}

int main(int argc, char *argv[]) {
	if(argc < 2) {
		printf("Usage: %s <password>\n", argv[0]);
		exit(0);
	}
	if(check_authentication(argv[1])) {
		printf("\n-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");
		printf("      Access Granted.\n");
		printf("-=-=-=-=-=-=-=-=-=-=-=-=-=-\n");
	} else {
		printf("\nAccess Denied.\n");
   }
}
	
