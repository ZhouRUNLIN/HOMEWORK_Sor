#ifndef COLOR_H_
#define COLOR_H_

#include <random>
#include <iostream>
#include <algorithm>
#include <chrono>


namespace pr {

// couleur RGB sur 24 bits
class Color {
	unsigned char r;
	unsigned char g;
	unsigned char b;
	friend std::ostream & operator<<(std::ostream & os, const Color & col);
public:
	Color(char r,char g, char b):r(r),g(g),b(b) {}
	// default to white
	Color():r(white.r),g(white.g),b(white.b) {}
	// shade by ratio / assombrir
	Color operator *(double ratio) {
		if (ratio > 1) {
			ratio = 1.0;
		}
		return Color(char(double(r)*ratio), char(double(g) *ratio) , char(double(b)* ratio));
	}
	// sommer deux couleurs (RGB donc additif)
	Color operator+ (const Color & c2) const {
		return Color(std::min(255,(int)r+c2.r),std::min(255,(int)g+c2.g),std::min(255,(int)b+c2.b));
	}
	// une couleur aleatoire (static)
	static Color random() {
		static std::default_random_engine re(std::chrono::system_clock::now().time_since_epoch().count());
		std::uniform_int_distribution<int> distrib(30, 255); // eviter les teintes trop sombres
		return Color(distrib(re), distrib(re), distrib(re));
	}
	// quelques constantes
	static const Color white;
	static const Color red;
	static const Color blue;
	static const Color black;

};

std::ostream & operator<<(std::ostream & os, const Color & col);


} /* namespace pr */

#endif /* COLOR_H_ */
