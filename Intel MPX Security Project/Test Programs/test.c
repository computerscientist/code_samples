#include <stdio.h>
#include <stdlib.h>

char *a, *b;

int main(int argc, char *argv[])
{
	a = (char *)malloc(100 * sizeof(char));
	b = __bnd_set_ptr_bounds(a, 100);
        a[200] = 0; // this does not cause error
	b[200] = 0; // this causes error
	printf("done\n");
	free(a);
	return 0;
}
