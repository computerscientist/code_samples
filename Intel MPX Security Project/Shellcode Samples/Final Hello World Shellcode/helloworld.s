BITS 32

jmp short beginning

code:
	pop ecx
	xor eax, eax
	mov al, 0x4
	xor ebx, ebx
	mov bl, 0x1
	xor edx, edx
	mov [ecx+15], dl
	xor edx, edx
	mov dl, 0xe
	int 0x80

	mov al, 0x1
	xor ebx, ebx
	int 0x80

beginning:
	call code
	db "Hello, World!", 0x0a, 0x0d
