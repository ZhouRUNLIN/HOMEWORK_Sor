#ifndef RAYON_H_
#define RAYON_H_

#include "Vec3D.h"

namespace pr {

// Un POD (plain old data) qui lie la camera au point de l'ecran.
class Rayon {
public:
	Vec3D ori; // origine
	Vec3D dest; // destination

	Rayon(const Vec3D & ori, const Vec3D & dest) : ori(ori),dest(dest) {}
};

} /* namespace pr */

#endif /* RAYON_H_ */
