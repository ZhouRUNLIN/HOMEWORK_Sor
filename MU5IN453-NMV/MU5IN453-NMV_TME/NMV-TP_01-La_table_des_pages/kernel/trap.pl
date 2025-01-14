#!/usr/bin/perl -l

use strict;
use warnings;


sub main
{
    my ($output, @err) = @_;
    my (@errvec) = qw(8 10 11 12 13 14 17);
    my ($vec, $fh);

    if (@err) {
	printf(STDERR "%s: unexpected operand\n", $0);
	printf(STDERR "Usage: %s <output>\n", $0);
	return 1;
    }

    if (!open($fh, '>', $output)) {
	printf(STDERR "%s: %s\n", $0, $!);
	printf(STDERR "Usage: %s <output>\n", $0);
	return 1;
    }

    printf($fh "%s", <<'EOF');
	.section ".text"
_trap:
	pushq   %rax
	pushq   %rdi
	pushq   %rsi
	pushq   %rdx
	pushq   %rcx
	pushq   %r8
	pushq   %r9
	pushq   %r10
	pushq   %r11
	pushq   %r12
	pushq   %r13
	pushq   %r14
	pushq   %r15
	pushq   %rbx
	pushq   %rbp

	movq    %rsp, %rdi
	call    trap

	popq    %rbp
	popq    %rbx
	popq    %r15
	popq    %r14
	popq    %r13
	popq    %r12
	popq    %r11
	popq    %r10
	popq    %r9
	popq    %r8
	popq    %rcx
	popq    %rdx
	popq    %rsi
	popq    %rdi
	popq    %rax

	addq    $16, %rsp
	iretq


EOF

    foreach $vec (0 .. 255) {
	printf($fh "__trap_vector_%s:\n", $vec);
	if (!grep { $vec == $_ } @errvec) {
	    printf($fh "\tpushq   \$0\n");
	}
	printf($fh "\tpushq   \$%s\n", $vec);
	printf($fh "\tjmp     _trap\n");
    }

    printf($fh "%s", <<'EOF');


	.section ".data"
	.globl  trap_vector
trap_vector:
EOF

    foreach $vec (0 .. 255) {
	printf($fh "\t.quad   __trap_vector_%s\n", $vec);
    }

    close($fh);
    return 0;
}

exit (main(@ARGV));
__END__
