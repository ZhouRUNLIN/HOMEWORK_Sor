# Documentation for multiboot2 can be found in
#   The Multiboot Specification version 1.6
#   http://nongnu.askapache.com/grub/phcoder/multiboot.pdf

	.section ".multiboot2"
	.align 64

.header_start:
	# The magic field of Multiboot header
	.long   0xe85250d6
	.long   0
	.long   (.header_end - .header_start)
	.long   -(0xe85250d6 + 0 + (.header_end - .header_start))

	# The address tag of Multiboot header
	.balign 8
	.word   2
	.word   0
	.long   24
	.long   .header_start
	.long   __kernel_text_start
	.long   __kernel_data_end
	.long   __kernel_bss_end

	# The entry address taf of Multiboot header
	.balign 8
	.word   3
	.word   0
	.long   12
	.long   entry_multiboot2

	# The terminating tag of Multiboot header
	.balign 8
	.word   0
	.word   0
	.long   8
.header_end:

	.section ".text"
	.balign 8
	.code32
entry_multiboot2:
	# Setup a valid GDT as multiboot2 does not guarantee it.
	# Load the DS segment as a data segment.
	# Load the CS segment as the code segment with a long jump.
	lgdt    gdtr32
	movw	$0x10, %ax
	movw    %ax, %ds
	ljmp    $0x08, $1f

1:
	# Load the CR3 with the identity page table pml4
	movl    $pml4, %eax
	movl	%eax, %cr3

	# Add the PAE and PGE flags to the CR4
	movl	%cr4, %eax
	orl     $0xa0, %eax
	movl    %eax, %cr4

	# Set the LME flag in the EFER register
	movl    $0xc0000080, %ecx
	rdmsr
	orl     $0x100, %eax
	wrmsr

	# Add the paging with the flags PG and WP in CR0
	movl    %cr0, %eax
	orl     $0x80000100, %eax
	movl    %eax, %cr0

	# Setup a 64-bits GDT and load the CS segment with a long jump.
	lgdt    gdtr64
	ljmp    $0x08, $1f

	.code64
1:
	# From this point we are executing in 64-bits mode.
	# We can now load the 64-bits data segment.
	movw	$0x10, %ax
	movw	%ax, %ds
	movw	%ax, %es
	movw	%ax, %fs
	movw	%ax, %gs
	movw	%ax, %ss

	# Setup an initial stack, set %ebp (mb2 info) as first argument, then
	# jump to C code.
	movq    $boot_stack, %rsp
	movq    $main_multiboot2, %rax
	xorq    %rdi, %rdi
	movl    %ebx, %edi
	jmpq    *%rax


# Documentation for 32-bits Segment Descriptors can be found in
#   AMD64 Architecture Programmer's Manual, Volume 2: System Programming
#   Section 4.7: Legacy Segment Descriptors

	.section ".data"
gdtr32:
	.word   3 * 8 - 1
	.long   gdt32
gdt32:
	.word   0,      0, 0,      0         # zero segment
	.word	0xffff, 0, 0x9a00, 0x00cf    # code segment
	.word   0xffff, 0, 0x9200, 0x00cf    # data segment

# Documentation for 64-bits Segment Descriptors can be found in
#   AMD64 Architecture Programmer's Manual, Volume 2: System Programming
#   Section 4.7: Legacy Segment Descriptors

	.globl  tss64
gdtr64:
	.word   7 * 8 - 1
	.long   gdt64
gdt64:
	.word   0, 0, 0,      0              # zero segment
	.word   0, 0, 0x9a00, 0x0020         # kernel code segment
	.word   0, 0, 0x9200, 0              # kernel data segment
	.word   0, 0, 0xf200, 0              # user data segment
	.word   0, 0, 0xfa00, 0x0020         # user code segment
tss64:
	.word   0, 0, 0x8900, 0              # mandatory tss
	.word   0, 0, 0,      0              # tss upper long

# Documentation for 64-bits Page Translation can be found in
#   AMD64 Architecture Programmer's Manual, Volume 2: System Programming
#   Section 5.3: Long-Mode Page Translation

	.align  0x1000
pml4:
	.quad   pml3 + 0x7    # pml4[0] = pml3 | U | W | P
	.space  0xff8, 0      # pml4[n] = empty
pml3:
	.quad   pml2 + 0x7    # pml3[0] = pml2 | U | W | P
	.space  0xff8, 0      # pml3[n] = empty
pml2:
	.quad   0x19b         # pml2[0] = G | PS | PCD | PWT | W | P
	.quad   apic + 0x1b   # pml2[1] = apic | G | PCD | PWT | W | P
	.space  0xff0, 0      # pml2[n] = empty
apic:
	.quad   0xfee0011b    # apic[0] = 0xfee00000 | G | PCD | PWT | W | P
	.space  0xff8, 0
	.section ".bss"
	.space  0x1000, 0     # initial stack of 4 KiB
boot_stack:
	.space  0x1000, 0     # syscall stack of 4 KiB
syscall_stack:
