global _start

section .text

_start:
	xor eax, eax
	mov al, 0x4
	xor ebx, ebx
	mov bl, 0x1
	push 0x20200a0d
	push 0x21646c72
	push 0x6f57206f
	push 0x6c6c6548
	mov ecx, esp
	xor edx, edx
	mov [ecx+14], dl
	xor edx, edx
	mov dl, 0xe
	int 0x80

	mov al, 0x1
	xor ebx, ebx
	int 0x80
