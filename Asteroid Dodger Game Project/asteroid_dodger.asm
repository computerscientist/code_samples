	addi $t0, $0, 19
	sw $t0, 48($0) #int spaceShipXLocation=19;
	
	sw $0, 52($0) #int iterations=0;
	
	addi $t0, $0, 1
	sll $t0, $t0, 22
	sw $t0, 56($0) #int delayTime=4194304;
	
	sw $0, 60($0) #int randomNumberIndex1=0; (used for randomInt(0, 39), 0-39 inclusive)
	sw $0, 64($0) #int randomNumberIndex2=0; (used for randomInt(1, 3), 1-3 inclusive)
	
	
	add $t0, $0, $0 #int i=0;
	addi $s4, $0, 3103
	addi $s7, $0, 20
	
main:	
	lw $s0, 60($0) #which random # in 776 random #'s in range [0, 39] are we going to load?
	addi $s1, $0, 256 #random # in range [0, 39] are stored in memory starting at location 256
	add $s2, $s0, $s1
	lw $s3, 0($s2) #load random # in range [0, 39]
	sw $s3, 0($t0) #asteroidXLocation[i]=randomInt(0, 39);
	
	addi $s0, $s0, 4
	slt $s5, $s4, $s0 #is randomNumberIndex1<4*776?
	beq $s5, $0, incrementRandomNumberIndexOne
	
	sw $0, 60($0) #If randomNumberIndex1>=4*776, reset it to 0
	beq $0, $0, endRandomNumberIndexOneCheck
	
incrementRandomNumberIndexOne:
	sw $s0, 60($0) #randomNumberIndex1+=4;

endRandomNumberIndexOneCheck:	
	srl $t9, $t0, 1 #Divide i by 2
	add $t6, $t0, $t9 #6*i (in higher level program)
	addi $t7, $0, -1
	sub $t3, $t7, $t6 #-1-6*i
	sw $t3, 24($t0) #asteroidYLocation[i]=-1-6*i;
	
	lw $s0, 64($0) #which random # in 776 random #'s in range [1, 3] are we going to load?
	addi $s1, $0, 3360 #random # in range [1, 3] are stored in memory starting at location 3360
	add $s2, $s0, $s1
	lw $s3, 0($s2) #load random # in range [1, 3]
	sw $s3, 68($t0) #asteroidColorCodes[i]=randomInt(1, 3);
	
	addi $s0, $s0, 4
	slt $s5, $s4, $s0 #is randomNumberIndex2<4*776?
	beq $s5, $0, incrementRandomNumberIndexTwo

	sw $0, 64($0) #If randomNumberIndex2>=4*776, reset it to 0
	beq $0, $0, endRandomNumberIndexTwoCheck
	
incrementRandomNumberIndexTwo:
	sw $s0, 64($0) #randomNumberIndex2+=4;
	
endRandomNumberIndexTwoCheck:
	addi $t0, $t0, 4
	slt $t2, $s7, $t0
	beq $t2, $0, main
	
	
playGame:
	jal updateAsteroidLocations
	jal updateDisplay
	jal checkForCollisions
	jal readInput
	jal delay
	
	beq $0, $0, playGame


clearScreen:
	add $t0, $0, $0
	addi $s7, $0, 20
	
clearScreenLoop:
	addi $t1, $0, 6464 #screenMemBeginning
	lw $t2, 24($t0)
	sll $t3, $t2, 7 #128*asteroidYLocation[i]
	sll $t4, $t2, 5 #32*asteroidYLocation[i]
	add $t5, $t3, $t4 #128*asteroidYLocation[i]+32*asteroidYLocation[i]=160*asteroidYLocation[i]
	
	add $t1, $t1, $t5 #screenMemBeginning+160*asteroidYLocation[i]
	
	lw $t6, 0($t0) #asteroidXLocation[i]
	sll $t6, $t6, 2 #4*asteroidXLocation[i]
	add $t1, $t1, $t6 #screenMemBeginning+160*asteroidYLocation[i]+4*asteroidXLocation[i]
	
	sw $0, 0($t1) #screenMem[screenMemBeginning+160*asteroidYLocation[i]+4*asteroidXLocation[i]]=0;
	addi $t0, $t0, 4
	
	slt $t2, $s7, $t0
	beq $t2, $0, clearScreenLoop

endClearScreen:	
	jr $ra #return
	

updateAsteroidLocations:
	add $k0, $0, $ra #Store $ra in $k0
	jal clearScreen
	
	add $ra, $0, $k0 #Restore $ra so we can return to playGame loop
	add $t0, $0, $0
	addi $s4, $0, 3103
	addi $s7, $0, 20
	
updateAsteroidLocationsLoop:
	lw $t1, 24($t0)
	addi $t1, $t1, 1
	sw $t1, 24($t0) #asteroidYLocation[i]+=1;
	
	addi $t2, $0, 29
	slt $s0, $t2, $t1
	
	beq $s0, $0, asteroidLocationsEndIf #if asteroidYLocation[i]<30, skip if statement
	
	#if statement
	addi $t1, $0, -6
	sw $t1, 24($t0) #asteroidYLocation[i]=-6;
	
	lw $s0, 60($0) #which random # in 776 random #'s in range [0, 39] are we going to load?
	addi $s1, $0, 256 #random # in range [0, 39] are stored in memory starting at location 256
	add $s2, $s0, $s1
	lw $s3, 0($s2) #load random # in range [0, 39]
	sw $s3, 0($t0) #asteroidXLocation[i]=randomInt(0, 39);
	
	addi $s0, $s0, 4
	slt $s5, $s4, $s0 #is randomNumberIndex1<4*776?
	beq $s5, $0, incrementRandomNumberOne
	
	sw $0, 60($0) #If randomNumberIndex1>=4*776, reset it to 0
	beq $0, $0, asteroidLocationsEndIf
	
incrementRandomNumberOne:
	sw $s0, 60($0) #randomNumberIndex1+=4;
	
asteroidLocationsEndIf:
	addi $t0, $t0, 4
	slt $t2, $s7, $t0
	beq $t2, $0, updateAsteroidLocationsLoop #check to see if we need to keep looping

	jr $ra
	
	
updateDisplay:
	addi $t1, $0, 11104 #screenMemBeginning+4*40*29
	lw $t2, 48($0) #spaceShipXLocation
	sll $t2, $t2, 2 #4*spaceShipXLocation
	add $t2, $t2, $t1 #screenMemBeginning+4*40*29+4*spaceShipXLocation
	
	addi $t3, $0, 4
	sw $t3, 0($t2) #screenMem[screenMemBeginning+4*40*29+4*spaceShipXLocation]=4;
	
	add $t0, $0, $0
	addi $s7, $0, 20
	
updateDisplayLoop:
	lw $t1, 24($t0)
	
	addi $t2, $0, -1
	slt $t3, $t2, $t1
	
	beq $t3, $0, endUpdateDisplayIf
	
	#if statement
	addi $t1, $0, 6464 #screenMemBeginning
	lw $t2, 24($t0)
	sll $t3, $t2, 7 #128*asteroidYLocation[i]
	sll $t4, $t2, 5 #32*asteroidYLocation[i]
	add $t5, $t3, $t4 #128*asteroidYLocation[i]+32*asteroidYLocation[i]=160*asteroidYLocation[i]
	
	add $t1, $t1, $t5 #screenMemBeginning+160*asteroidYLocation[i]
	
	lw $t6, 0($t0) #asteroidXLocation[i]
	sll $t6, $t6, 2 #4*asteroidXLocation[i]
	add $t1, $t1, $t6 #screenMemBeginning+160*asteroidYLocation[i]+4*asteroidXLocation[i]
	
	lw $t7, 68($t0) #asteroidColorCodes[i]
	sw $t7, 0($t1) #screenMem[screenMemBeginning+160*asteroidYLocation[i]+4*asteroidXLocation[i]]=asteroidColorCodes[i];
	
endUpdateDisplayIf:
	addi $t0, $t0, 4
	slt $t2, $s7, $t0
	beq $t2, $0, updateDisplayLoop
	
	jr $ra


delay:	
	add $t0, $0, $0 #int i;
	lw $t1, 56($0) #delayTime

delayLoop:
	addi $t0, $t0, 1
	slt $t2, $t1, $t0 #delayTime<i?
	beq $t2, $0, delayLoop
	
	lw $t3, 52($0)
	addi $t3, $t3, 1
	sw $t3, 52($0) #iterations++;
	
	addi $t3, $t3, -200 #see if iterations is >200
	slt $s4, $0, $t3 #is 0<iterations-200?
	
	beq $s4, $0, endDelayIf
	
	#if statement
	sw $0, 52($0) #iterations=0;
	
	addi $t1, $0, 22143
	sll $t1, $t1, 3
	addi $t1, $t1, 3 #result is 177147
	
	lw $t2, 56($0) #delayTime
	sub $t3, $t1, $t2 #177147-delayTime
	addi $t3, $t3, 1
	
	slt $t4, $t3, $0 #177147-delayTime<0?
	beq $t4, $0, endDelayIf #skip to end if delayTime isn't going to change
	
	add $t5, $t2, $t2
	add $t5, $t5, $t2 #3*delayTime
	
	sra $t5, $t5, 2 #(delayTime*3)/4
	sw $t5, 56($0) #delayTime=(delayTime*3)/4;
	
endDelayIf:
	jr $ra
	
	
checkForCollisions:
	add $t0, $0, $0 #int i=0;
	addi $s7, $0, 20

checkForCollisionsLoop:
	lw $t1, 24($t0) #asteroidYLocation[i]
	addi $t2, $0, 29
	sub $t3, $t1, $t2 #result=asteroidYLocation[i]-29
	addi $t4, $0, 1
	addi $t5, $0, -1
	slt $t6, $t3, $t4 #result<1?
	slt $t7, $t5, $t3 #-1<result?
	and $t8, $t6, $t7 #if -1<result<1, result=0
	
	lw $t1, 0($t0) #asteroidXLocation[i]
	lw $t2, 48($0) #spaceShipXLocation
	sub $t3, $t1, $t2 #result=asteroidXLocation[i]-spaceShipXLocation
	slt $t6, $t3, $t4 #result<1?
	slt $t7, $t5, $t3 #-1<result?
	and $t9, $t6, $t7 #if -1<result<1, result=0
	
	and $s0, $t8, $t9 #asteroidYLocation[i]==29 && asteroidXLocation[i]==spaceShipXLocation
	addi $k1, $0, 1
	slt $t8, $s0, $k1 #if asteroidYLocation[i]==29 && asteroidXLocation[i]==spaceShipXLocation, $s0 should be "1", making $t8 "0"

	beq $t8, $0, gameOver
	
	addi $t0, $t0, 4
	slt $t2, $s7, $t0
	beq $t2, $0, checkForCollisionsLoop
	
	jr $ra
	

readInput:
	addi $t1, $0, 0x6B
	lw $t6, 248($0) #load memory location for keyboard key
	
	beq $t1, $t6, leftKeyPressed
	j readInputElseIf

leftKeyPressed:
	lw $t1, 48($0) #spaceShipXLocation
	beq $t1, $0, readInputElseIf
	
	sll $t2, $t1, 2 #4*spaceShipXLocation
	sw $0, 11104($t2)
	
	addi $t1, $t1, -1
	sw $t1, 48($0) #spaceShipXLocation--;
	
	jr $ra
	
readInputElseIf:
	addi $t1, $0, 14365
	sll $t1, $t1, 2 #Shift 14365 left two places to get 0xE074
	
	beq $t1, $t6, rightKeyPressed
	jr $ra
	
rightKeyPressed:
	lw $t1, 48($0) #spaceShipXLocation
	addi $t0, $0, 39
	beq $t1, $t0, endReadInput

	sll $t2, $t1, 2 #4*spaceShipXLocation
	sw $0, 11104($t2)
	
	addi $t1, $t1, 1
	sw $t1, 48($0) #spaceShipXLocation++;
	
endReadInput:
	jr $ra
	

gameOver:
	jal clearScreen
	
	addi $t1, $0, 11104 #screenMemBeginning+4*40*29
	lw $t2, 48($0) #spaceShipXLocation
	sll $t2, $t2, 2 #4*spaceShipXLocation
	add $t2, $t2, $t1 #screenMemBeginning+4*40*29+4*spaceShipXLocation
	
	sw $0, 0($t2)

gameOverLoop:
	beq $0, $0, gameOverLoop
	
