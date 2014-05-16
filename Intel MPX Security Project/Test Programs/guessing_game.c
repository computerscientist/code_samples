#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int magicNumber=0;
int level=0;

void introduce_game(void)
{
	char playerName[50];
	printf("Welcome to the number guessing game!\n");
	printf("Enter your player name: ");
	gets(playerName);

	printf("Hello, %s! Choose your difficulty level [1-3]:\n", playerName);
	printf("Level 1: Guess a number between 1 and 10\n");
	printf("Level 2: Guess a number between 1 and 100\n");
	printf("Level 3: Guess a number between 1 and 1000\n");

	while(level<1 || level>3)
	{
		char currentLine[80];
		level=atoi(fgets(currentLine, 80, stdin));

		if(level<1 || level>3)
			printf("Invalid level! Choose difficulty level [1-3]:\n");
	}

	printf("You picked level %d\n", level);
}

void play_game(void)
{
	int upperBound=10;
	int guessedNumber=0, numberOfTries=0;
	srand(time(NULL));

	switch(level)
	{
		case 1:
			upperBound=10;
			break;
		case 2:
			upperBound=100;
			break;
		case 3:
			upperBound=1000;
	}

	magicNumber=(rand() % upperBound) + 1;
	printf("Guess the number between 1 and %d:\n", upperBound);

	while(guessedNumber!=magicNumber)
	{
		char currentLine[80];
                guessedNumber=atoi(fgets(currentLine, 80, stdin));

		printf("You guessed: %d\n", guessedNumber);
		numberOfTries++;

		if(guessedNumber<magicNumber)
			printf("Too low!\n");
		else if(guessedNumber>magicNumber)
			printf("Too high!\n");
		else
			printf("You win!\n");
	}

	printf("You guessed the magic number %d in %d tries!\n", magicNumber, numberOfTries);
}

int main(int argc, char ** argv)
{
	introduce_game();
	play_game();
}
