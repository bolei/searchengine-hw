package myimpl;

import java.util.Comparator;
import java.util.Map;

public class ScoreDescComparator<T extends Comparable<T>> implements
		Comparator<T> {

	private Map<T, Double> base;

	public ScoreDescComparator(Map<T, Double> base) {
		this.base = base;
	}

	@Override
	public int compare(T o1, T o2) {
		double diff = base.get(o1) - base.get(o2);
		// descending ordering by score
		if (diff > 0) {
			return -1;
		} else if (diff < 0) {
			return 1;
		} else {
			// ascending ordering by docId
			if (o1.compareTo(o2) > 0) {
				return 1;
			} else {
				return 0;
			}
		}
		// do not return 0 to prevent from merging
	}

}
