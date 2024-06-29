#include "Color.h"

namespace pr {

const Color Color::white = Color(255,255,255);
const Color Color::red = Color(255,0,0);
const Color Color::blue = Color(0,0,255);
const Color Color::black = Color(0,0,0);


std::ostream & operator<<(std::ostream & os, const Color & col) {
	os << int(col.r) << ' ' << int(col.g) << ' ' << int(col.b) << '\n' ;
	return os;
}

} /* namespace pr */
