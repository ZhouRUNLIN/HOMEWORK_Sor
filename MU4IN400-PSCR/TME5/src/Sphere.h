#ifndef SPHERE_H_
#define SPHERE_H_

#include "Vec3D.h"
#include "Color.h"
#include "Rayon.h"
#include <limits>
#include <algorithm>

namespace pr {

// une sphere 3D, coloree, ayant un rayon et une position pour son centre
class Sphere {
	Vec3D centre;
	double rayon;
	Color color;
public:
	Sphere(const Vec3D & centre={0,0,0}, double rayon=10.0, const Color & c=Color::white):centre(centre),rayon(rayon),color(c) {}

	// return distance to closest intersection with the given object
	float intersects (Rayon ray) const {
		// trouver vecteur unitaire l donnant la direction
		Vec3D l = (ray.dest - ray.ori).normalize();
		// trouver le vector liant le centre de la sphere a l'obs
		Vec3D oc = ray.ori - centre;
		// Combien le polynome (degre 2) a-t-il de solutions ? a x^2 + b x + c = 0
		auto a = 1; // longueur de l au carre
		auto b = 2 * (l & oc); // produit scalaire note &
		auto c = (oc&oc) - (rayon*rayon) ; // carre de oc - carre du rayon
		auto discriminant = b*b - 4*a*c;
		if (discriminant < 0) {
			// pas d'inter
			return std::numeric_limits<float>::max();
		} else if (discriminant == 0) {
			// tangeant au cercle
			return -b / (2* a);
		} else {
			// on veut la distance la plus petite a l'obs = la plus petite racine
			auto r1 = (-b + sqrt(discriminant)) / (2*a);
			auto r2 = (-b - sqrt(discriminant)) / (2*a);
			return std::min(r1,r2);
		}

	}
	// normale a la sphere au point d'intersection (sur la sphere)
	Vec3D getNormale(const Vec3D & intersection) const {
		// simplement le vecteur du centre au point d'intersection 
		// normalise donc divise par le rayon vu que le point est sur la sphere
		return (intersection - centre) / rayon;
	}

	const Color & getColor () const {return color ;}
};

} /* namespace pr */

#endif /* SPHERE_H_ */
