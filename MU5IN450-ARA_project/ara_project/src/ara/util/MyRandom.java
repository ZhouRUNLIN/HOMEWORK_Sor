package ara.util;

import peersim.core.CommonState;

public class MyRandom {

	public static long nextLong(long mean, double variance) {
		// remplace le NextPoisson qui limite la moyenne à 900
		// tirage uniforme en fonction d'une moyenne + ou - la variance
		long min = (long) (mean * (1.0 - variance));
		long max = (long) (mean * (1.0 + variance));
		return CommonState.r.nextLong(max - min) + min;
	}

}
