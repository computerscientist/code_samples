#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main(int argc, char ** argv)
{
	unsigned low, high;
	unsigned long tsc1, tsc2;
	asm volatile ("rdtsc" : "=a" (low), "=d" (high));
	tsc1 = (high << 32) | low;
	srand(time(NULL));

	int i;
	int length=16;
	int number_array[length];

	for(i=0;i<200000;i++)
		number_array[rand()%length]=rand();

	asm volatile ("rdtsc" : "=a" (low), "=d" (high));
        tsc2 = (high << 32) | low;

	if(tsc2>tsc1)
        	printf("%lu\n", tsc2-tsc1);
	else
		printf("%lu\n", (unsigned long)((signed long) tsc2-(signed long) tsc1));
}
