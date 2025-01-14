#include <vga.h>
#include <x86.h>


#define VGA_SCREEN_ADDRESS        ((uint16_t *) 0xb8000)
#define VGA_SCREEN_LINES          25
#define VGA_SCREEN_COLUMNS        80
#define VGA_CURSOR_ADDRESS_PORT   0x3d4
#define VGA_CURSOR_ADDRESS_LOW    0x0f
#define VGA_CURSOR_ADDRESS_HIGH   0x0e
#define VGA_CURSOR_DATA_PORT      0x3d5
#define VGA_COLOR_DEFAULT         0x700


static uint16_t cursor = 0;

static void update_cursor(uint16_t pos)
{
	cursor = pos;

	out8(VGA_CURSOR_ADDRESS_PORT, VGA_CURSOR_ADDRESS_LOW);
	out8(VGA_CURSOR_DATA_PORT, pos & 0xff);

	out8(VGA_CURSOR_ADDRESS_PORT, VGA_CURSOR_ADDRESS_HIGH);
	out8(VGA_CURSOR_DATA_PORT, (pos >> 8) & 0xff);
}

static void scroll(void)
{
	uint16_t *screen = VGA_SCREEN_ADDRESS;
	size_t line, cursor, end;

	for (line = 0; line < (VGA_SCREEN_LINES - 1); line++) {
		cursor = line * VGA_SCREEN_COLUMNS;
		end = cursor + VGA_SCREEN_COLUMNS;
		while (cursor < end) {
			screen[cursor] = screen[cursor + VGA_SCREEN_COLUMNS];
			cursor++;
		}
	}

	cursor = line * VGA_SCREEN_COLUMNS;
	end = cursor + VGA_SCREEN_COLUMNS;
	while (cursor < end) {
		screen[cursor] = VGA_COLOR_DEFAULT;
		cursor++;
	}
}


void clear(void)
{
	size_t i, total = VGA_SCREEN_LINES * VGA_SCREEN_COLUMNS;
	uint16_t *screen = VGA_SCREEN_ADDRESS;

	for (i = 0; i < total; i++)
		screen[i] = VGA_COLOR_DEFAULT;

	update_cursor(0);
}

void putc(char c)
{
	uint16_t n = cursor;
	uint16_t *screen = VGA_SCREEN_ADDRESS;

	switch (c) {
	case '\n':
		n += VGA_SCREEN_COLUMNS;
	case '\r':
		n = (n / VGA_SCREEN_COLUMNS) * VGA_SCREEN_COLUMNS;
		break;
	case '\t':
		n = ((n + 1) & (~0x7)) + 8;
		break;
	default:
		screen[n++] = VGA_COLOR_DEFAULT | c;
		break;
	}

	if (n >= VGA_SCREEN_LINES * VGA_SCREEN_COLUMNS) {
		scroll();
		n -= VGA_SCREEN_COLUMNS;
	}

	update_cursor(n);
}

void puts(const char *str, size_t n)
{
	size_t i;

	for (i = 0; i < n; i++)
		putc(str[i]);
}
