OUTPUT_FORMAT(elf64-x86-64)

TASK_VMA = 0x2000000000;

SECTIONS
{
  . = TASK_VMA;

  .header : ALIGN(0x1000) {
    __task_start = .;
    *(.header);
  }

  .text : {
    *(.text);
  }

  .data : {
    *(.data*);
    *(.rodata*);
  }

  .bss : ALIGN(0x1000) {
    __task_end = .;
    *(.bss);
    *(COMMON);

    . = ALIGN(0x1000);
    __bss_end = .;
  }

  /DISCARD/ : {
    *(.eh_frame .comment);
  }
}
