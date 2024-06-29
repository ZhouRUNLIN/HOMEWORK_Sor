/*
 * Vec3D.cpp
 *
 *  Created on: Sep 20, 2018
 *      Author: ythierry
 */

#include "Vec3D.h"
#include <cmath>

namespace pr {

double Vec3D::length() const {
	return ::sqrt(x*x + y*y + z*z);
}

Vec3D Vec3D::normalize () {
	double l = length();
	if (l == 0) return *this;
	return Vec3D(x / l, y/ l, z /l);
}

Vec3D operator* (double d, const Vec3D & v) { return v * d; }

} /* namespace pr */
