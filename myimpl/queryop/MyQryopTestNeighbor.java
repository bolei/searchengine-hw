package myimpl.queryop;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map.Entry;

import myimpl.queryresult.MyInvertedList;
import myimpl.queryresult.MyQryResult;

public abstract class MyQryopTestNeighbor extends MyQryop {

	protected int windowSize;

	public MyQryopTestNeighbor(int n, MyQryop... myQryops) {
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
			if (invList.getDocPostings().isEmpty()) {
				return invList.clear();
			}
			MyQryResult curResult = args.get(i).evaluate();
			if (curResult instanceof MyInvertedList == false) {
				throw new RuntimeException(
						"MyQryopNear.evaluate() input is not inverted list for arg:"
								+ args.get(i));
			}
			MyInvertedList curInvList = (MyInvertedList) curResult;

			// get all intersected doc ids
			TreeSet<Integer> intersectionDocIds = new TreeSet<Integer>(invList
					.getDocPostings().keySet());
			intersectionDocIds.retainAll(curInvList.getDocPostings().keySet());
			Iterator<Entry<Integer, TreeSet<Integer>>> entryIt = invList
					.getDocPostings().entrySet().iterator();
			while (entryIt.hasNext()) {
				Entry<Integer, TreeSet<Integer>> entry = entryIt.next();
				if (intersectionDocIds.contains(entry.getKey()) == false) {
					entryIt.remove();
				}
			}

			for (int docId : intersectionDocIds) {
				TreeSet<Integer> invPositions = invList.getDocPostings().get(
						docId);
				TreeSet<Integer> curPositions = curInvList.getDocPostings()
						.get(docId);
				TreeSet<Integer> newPostions = new TreeSet<Integer>();
				for (int pos : invPositions) {
					Integer high = curPositions.higher(pos);
					if (high != null && isInNeighbor(high, pos)) {
						newPostions.add(high);
					}
				}
				if (newPostions.isEmpty()) {
					invList.getDocPostings().remove(docId);
				} else {
					invList.getDocPostings().put(docId, newPostions);
				}
			}
		}

		return invList;

	}

	protected abstract boolean isInNeighbor(int high, int low);

}
