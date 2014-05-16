#include <unistd.h>

int main(int argc, char ** argv)
{
	char program[]="/sbin/shutdown\x00";
	char zero[]="0\x00";
	char *arguments[3];

	char *envp[1];
	envp[0]=0;

	arguments[0]=program;
	arguments[1]=zero;
	arguments[2]=0;

	execve(arguments[0], arguments, envp);
}
