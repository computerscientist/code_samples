#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <openssl/sha.h>
#include <openssl/md5.h>

#include <iostream>

using namespace std;

void printKey(unsigned char *key, int keyLength)
{
	for(int i=0;i<keyLength;i++)
		printf("%02x", key[i]);

	printf("\n");
}

void copyMD5Key(char *output, unsigned char *key)
{
	sprintf(output, "%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x", key[0], key[1], key[2], key[3], key[4], key[5], key[6], key[7], key[8], key[9], key[10], key[11], key[12], key[13], key[14], key[15]);
}

void copySHAKey(char *output, unsigned char *key)
{
	sprintf(output, "%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x", key[0], key[1], key[2], key[3], key[4], key[5], key[6], key[7], key[8], key[9], key[10], key[11], key[12], key[13], key[14], key[15], key[16], key[17], key[18], key[19]);
}

int main(int argc, char *argv[])
{
	if(argc!=3)
	{
		cout << "Error: Parameter format is [username] [password]" << endl;
		return 1;
	}

	char *firstOutput;
	char *tempSecondInput;
	char *secondInput;
	char *secondOutput;
	char *tempFinalInput;
	char *finalInput;
	char *finalOutput;

	firstOutput=(char *) malloc((MD5_DIGEST_LENGTH+1)*sizeof(char));
	tempSecondInput=(char *) malloc((2*MD5_DIGEST_LENGTH+1)*sizeof(char));
	secondInput=(char *) malloc((strlen(argv[1])+2*MD5_DIGEST_LENGTH+1)*sizeof(char));
	secondOutput=(char *) malloc((SHA_DIGEST_LENGTH+1)*sizeof(char));
	tempFinalInput=(char *) malloc((2*SHA_DIGEST_LENGTH+1)*sizeof(char));
	finalInput=(char *) malloc((strlen(argv[1])+2*SHA_DIGEST_LENGTH+1)*sizeof(char));
	finalOutput=(char *) malloc((MD5_DIGEST_LENGTH+1)*sizeof(char));

	MD5((const unsigned char *) argv[2], strlen(argv[2]), (unsigned char *) firstOutput);

	copyMD5Key(tempSecondInput, (unsigned char *) firstOutput);
	sprintf(secondInput, "%s%s", argv[1], tempSecondInput);

	SHA1((const unsigned char *) secondInput, strlen(secondInput), (unsigned char *) secondOutput);

	copySHAKey(tempFinalInput, (unsigned char *) secondOutput);
	sprintf(finalInput, "%s%s", argv[1], tempFinalInput);

	MD5((const unsigned char *) finalInput, strlen(finalInput), (unsigned char *) finalOutput);
	printKey((unsigned char *) finalOutput, MD5_DIGEST_LENGTH);

	free(firstOutput);
	free(tempSecondInput);
	free(secondInput);
	free(secondOutput);
	free(tempFinalInput);
	free(finalInput);
	free(finalOutput);

	return 0;
}
