package myimpl;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

public class MyQryopNear extends MyQryop {

	private int windowSize;

	public MyQryopNear(int n, MyQryop... myQryops) {
		windowSize = n;
		Collections.addAll(args, myQryops);
	}

	@Override
	public MyInvertedList evaluate() throws IOException {
		MyQryResult result = args.get(0).evaluate();
		if (result instanceof MyInvertedList == false) {
			throw new RuntimeException(
					"MyQryopNear.evaluate() input is not inverted list for arg:"
							+ args.get(0));
		}
		MyInvertedList invList = (MyInvertedList) result;
		for (int i = 1; i < args.size(); i++) {
			MyQryResult curResult = args.get(i).evaluate();
			if (curResult instanceof MyInvertedList == false) {
				throw new RuntimeException(
						"MyQryopNear.evaluate() input is not inverted list for arg:"
								+ args.get(i));
			}
			MyInvertedList curInvList = (MyInvertedList) curResult;

			// get all intersected doc ids
			invList.getDocPostings().keySet()
					.retainAll(curInvList.getDocPostings().keySet());
			for (int docId : invList.getDocPostings().keySet()) {
				TreeSet<Integer> nearCompareSet = new TreeSet<Integer>(
						new NearComparator());
				nearCompareSet.addAll(curInvList.getDocPostings().get(docId));

				// remove all positions that not near that in curInvList
				invList.getDocPostings().get(docId).retainAll(nearCompareSet);
			}
		}

		return invList;

	}

	private class NearComparator implements Comparator<Integer> {

		@Override
		public int compare(Integer o1, Integer o2) {

			// o1 is pos in rhs arg of QryopSyn
			int diff = o1 - o2;
			if (diff < windowSize && diff > 0) {
				return 0;
			}
			return diff;
		}
	}
}
