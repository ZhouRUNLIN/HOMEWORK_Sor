#include <printk.h>
#include <stdarg.h>
#include <string.h>
#include <vga.h>


struct vsnprintk_state
{
	char    *buffer;
	size_t   len;
	size_t   ptr;
};

static bool_t vsnprintk_handler(void *user, char c)
{
	struct vsnprintk_state *st = (struct vsnprintk_state *) user;

	if (st->ptr >= st->len)
		return 0;

	st->buffer[st->ptr] = c;
	st->ptr++;

	return 1;
}

static bool_t vprintk_handler(void *user __attribute__((unused)), char c)
{
	putc(c);
	return 1;
}


struct vhprintk_format
{
	bool_t  alternate_form;
	bool_t  zero_pad;
	bool_t  right_pad;
	bool_t  positive_blank;
	bool_t  visible_sign;
	bool_t  long_operand;
	size_t  minimum_size;
	char    type;
};

struct vhprintk_state
{
	const char               *input;
	bool_t                  (*handler)(void *, char);
	void                     *user;
	struct vhprintk_format    format;
	size_t                    done;
	bool_t                    stop;
};

static size_t number_length(uint64_t number, uint8_t base)
{
	size_t len = 0;

	do {
		number /= base;
		len++;
	} while (number > 0);

	return len;
}

static void vhprintk_read_format(struct vhprintk_state *st)
{
	struct vhprintk_format *f = &st->format;
	const char *input = st->input;
	char c;

	memset(f, 0, sizeof (*f));

	while ((c = *(input++)) != '\0') {
		switch (c) {
		case '#':
			f->alternate_form = 1;
			break;
		case '0':
			f->zero_pad = 1;
			break;
		case '-':
			f->right_pad = 1;
			break;
		case ' ':
			f->positive_blank = 1;
			break;
		case '+':
			f->visible_sign = 1;
			break;
		default:
			input--;
			goto next;
		}
	}

	goto out;

 next:
	while ((c = *(input++)) != '\0') {
		if (c >= '0' && c <= '9') {
			f->minimum_size = f->minimum_size * 10 + (c - '0');
		} else {
			f->type = c;
			break;
		}
	}

 out:
	if (f->type == 'l') {
		f->long_operand = 1;
		f->type = *(input++);
	}

	st->input = input;
}

static bool_t vhprintk_print_one(struct vhprintk_state *st, char c)
{
	if (!st->stop) {
		if (!st->handler(st->user, c))
			st->stop = 1;
		else
			st->done++;
	}

	return !(st->stop);
}

static void vhprintk_pad(struct vhprintk_state *st, size_t len, char pad)
{
	size_t i;

	for (i = 0; i < len; i++)
		if (!vhprintk_print_one(st, pad))
			return;
}

static void vhprintk_print_radical(struct vhprintk_state *st, uint64_t radical,
				   uint8_t base, size_t len, char upchar)
{
	size_t div = 1;
	size_t unit;
	char c;

	while (len > 1) {
		div *= base;
		len--;
	}

	do {
		unit = radical / div;
		if (unit < 10)
			c = '0' + unit;
		else
			c = upchar + (unit - 10);

		if (!vhprintk_print_one(st, c))
			return;

		radical = radical % div;
		div /= base;
	} while (div > 0);
}

static void vhprintk_print_number(struct vhprintk_state *st, uint64_t positive,
				  uint8_t base, const char *prefix,
				  char upbase)
{
	size_t len = number_length(positive, base);
	size_t preflen = strlen(prefix);
	size_t totlen = len + preflen;
	size_t padlen = 0;
	size_t i;

	if (st->format.minimum_size > totlen)
		padlen = st->format.minimum_size - totlen;

	if (!st->format.right_pad && !st->format.zero_pad)
		vhprintk_pad(st, padlen, ' ');

	for (i = 0; i < preflen; i++)
		vhprintk_print_one(st, prefix[i]);

	if (!st->format.right_pad && st->format.zero_pad)
		vhprintk_pad(st, padlen, '0');

	vhprintk_print_radical(st, positive, base, len, upbase);

	if (st->format.right_pad)
		vhprintk_pad(st, padlen, ' ');
}

static void vhprintk_print_signed(struct vhprintk_state *st, uint8_t base,
				  int64_t arg)
{
	const char *prefix = "";
	uint64_t positive;

	if (arg < 0) {
		positive = -arg;
		prefix = "-";
	} else {
		positive = arg;
		if (st->format.positive_blank)
			prefix = " ";
		else if (st->format.visible_sign) {
			prefix = "+";
		}
	}

	vhprintk_print_number(st, positive, base, prefix, 0);
}

static void vhprintk_print_unsigned(struct vhprintk_state *st, uint8_t base,
				    char upbase, uint64_t arg)
{
	const char *prefix = "";

	if (st->format.type == 'x' || st->format.type == 'X') {
		if (st->format.alternate_form)
			prefix = "0x";
	} else if (st->format.type == 'o') {
		if (st->format.alternate_form)
			prefix = "0";
	} else if (st->format.type == 'b') {
		if (st->format.alternate_form)
			prefix = "0b";
	} else {
		if (st->format.positive_blank)
			prefix = " ";
		else if (st->format.visible_sign) {
			prefix = "+";
		}
	}

	vhprintk_print_number(st, arg, base, prefix, upbase);
}

static void vhprintk_print_char(struct vhprintk_state *st, uint32_t arg)
{
	size_t padlen = 0;
	size_t len = 1;

	if (st->format.minimum_size > len)
		padlen = st->format.minimum_size - len;

	if (!st->format.right_pad)
		vhprintk_pad(st, padlen, ' ');

	vhprintk_print_one(st, arg);

	if (st->format.right_pad)
		vhprintk_pad(st, padlen, ' ');
}

static void vhprintk_print_string(struct vhprintk_state *st, const char *arg)
{
	size_t padlen = 0;
	size_t len, i;

	if (arg == NULL)
		arg = "(null)";

	len = strlen(arg);

	if (len == 0 && st->format.positive_blank)
		padlen = 1;
	if (st->format.minimum_size > len)
		padlen = st->format.minimum_size - len;

	if (!st->format.right_pad)
		vhprintk_pad(st, padlen, ' ');

	for (i = 0; i < len; i++)
		vhprintk_print_one(st, *arg++);

	if (st->format.right_pad)
		vhprintk_pad(st, padlen, ' ');
}

static size_t vhprintk(bool_t handler(void *, char), void *user,
		       const char *input, va_list ap)
{
	struct vhprintk_state st;
	bool_t fmt = 0;
	uint8_t base;
	char upbase;
	char c;

	memset(&st, 0, sizeof (st));
	st.input = input;
	st.handler = handler;
	st.user = user;

	while ((c = *(input++)) != '\0') {
		if (st.stop)
			break;

		if (c == '%') {
			if (fmt) {
				fmt = 0;
			} else {
				fmt = 1;
				continue;
			}
		}

		if (!fmt) {
			vhprintk_print_one(&st, c);
			continue;
		}

		st.input = input - 1;
		vhprintk_read_format(&st);

		base = 0;
		upbase = 'a';

		if (st.format.type == 'p') {
			st.format.type = 'x';
			st.format.alternate_form = 1;
			st.format.long_operand = 1;
		}

		switch (st.format.type) {
		case 'c':
			vhprintk_print_char(&st, va_arg(ap, uint32_t));
			break;
		case 'd':
		case 'i':
			if (st.format.long_operand)
				vhprintk_print_signed(&st, 10,
						      va_arg(ap, int64_t));
			else
				vhprintk_print_signed(&st, 10,
						      va_arg(ap, int32_t));
			break;
		case 's':
			vhprintk_print_string(&st, va_arg(ap, char *));
			break;
		case 'X':
			upbase = 'A';
		case 'x':
			base += 6;
		case 'u':
			base += 2;
		case 'o':
			base += 6;
		case 'b':
			base += 2;
			if (st.format.long_operand)
				vhprintk_print_unsigned(&st, base, upbase,
							va_arg(ap, uint64_t));
			else
				vhprintk_print_unsigned(&st, base, upbase,
							va_arg(ap, uint32_t));
			break;
		}

		fmt = 0;
		input = st.input;
	}

	return st.done;
}


size_t printk(const char *format, ...)
{
	va_list ap;
	size_t ret;

	va_start(ap, format);
	ret = vprintk(format, ap);
	va_end(ap);

	return ret;
}

size_t vprintk(const char *format, va_list ap)
{
	return vhprintk(vprintk_handler, NULL, format, ap);
}

size_t snprintk(char *buffer, size_t size, const char *format, ...)
{
	va_list ap;
	size_t ret;

	va_start(ap, format);
	ret = vsnprintk(buffer, size, format, ap);
	va_end(ap);

	return ret;
}

size_t vsnprintk(char *buffer, size_t size, const char *format, va_list ap)
{
	struct vsnprintk_state st = {
		.buffer = buffer,
		.len    = size,
		.ptr    = 0
	};

	return vhprintk(vsnprintk_handler, &st, format, ap);
}
