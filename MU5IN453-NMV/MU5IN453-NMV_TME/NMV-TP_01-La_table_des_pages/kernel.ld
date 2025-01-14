OUTPUT_FORMAT(elf64-x86-64)

/* Kernel Linear (= Physical) Memory Address */
KERNEL_LMA = 0x100000;

SECTIONS
{
  /* For what address to compile the code */
  . = KERNEL_LMA;

  .text : ALIGN(0x1000) {
    __kernel_text_start = .;
    *(.multiboot2);
    *(.text);
    __kernel_text_end = .;
  }

  .data : {
    __kernel_data_start = .;
    *(.data*);
    *(.rodata*);
    __kernel_data_end = .;
  }

  .bss : {
    __kernel_bss_start = .;
    *(.bss);
    *(COMMON);
    __kernel_bss_end = .;
  }

  /DISCARD/ : {
    *(.eh_frame .comment);
  }
}
