#ifndef VEC3D_H_
#define VEC3D_H_

namespace pr {

// un Vecteur 3D, ou un Point 3D selon l'usage qui en est fait.
// on l'appelle Vec3D car il propose les operations vectorielles usuelles
class Vec3D {
public:
	double x;
	double y;
	double z;

	Vec3D(double x=0,double y=0,double z=0):x(x),y(y),z(z) {}

	// somme de vecteurs
	Vec3D operator+ (const Vec3D & o) const { return Vec3D(x+o.x, y+o.y, z+o.z); }
	// difference utile pour calculer un vecteur a partir de deux points
	Vec3D operator- (const Vec3D & o) const { return Vec3D(x-o.x, y-o.y, z-o.z); }
	// produit par un scalaire, rallonger ou reduire
	Vec3D operator* (double d) const { return Vec3D(x*d, y*d, z*d); }
	Vec3D operator/ (double d) const { return Vec3D(x/d, y/d, z/d); }

	// produit scalaire (dot product) donne l'angle d'incidence entre deux vecteurs
	double operator&(const Vec3D & r) const { return x*r.x + y*r.y + z*r.z ; }

	double length() const ; // norme/longueur du vecteur
	Vec3D normalize(); // vector de longueur 1
};

// produit par un scalaire (rallonger/reduire) mais scalaire a gauche 3 * v
Vec3D operator* (double d, const Vec3D & v) ;

} /* namespace pr */

#endif /* VEC3D_H_ */
