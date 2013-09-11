package myimpl;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class MyScoreList implements MyQryResult {
	// <docId, score>
	private TreeMap<Integer, Float> scores = new TreeMap<Integer, Float>();

	public void addScore(Integer docId, Float score) {
		scores.put(docId, score);
	}

	public TreeMap<Integer, Float> getScores() {
		return scores;
	}

	public TreeMap<Integer, Float> getSortedScores() {
		TreeMap<Integer, Float> sortedScores = new TreeMap<Integer, Float>(
				new ScoreDescComparator(scores));
		sortedScores.putAll(scores);
		return sortedScores;
	}

	/**
	 * Perform intersection on scores of the two score lists. score will take
	 * the minimum between the two.
	 * 
	 * @return a new score list object
	 */
	public static MyScoreList intersection(MyScoreList sl1, MyScoreList sl2) {
		TreeSet<Integer> keySet = new TreeSet<Integer>(sl1.scores.keySet());
		keySet.retainAll(sl2.scores.keySet());
		MyScoreList newSl = new MyScoreList();
		for (int docId : keySet) {
			newSl.addScore(docId,
					Math.min(sl1.scores.get(docId), sl2.scores.get(docId)));
		}
		return newSl;
	}

	public static MyScoreList union(MyScoreList sl1, MyScoreList sl2) {
		TreeSet<Integer> keySet = new TreeSet<Integer>(sl1.scores.keySet());
		keySet.addAll(sl2.scores.keySet());
		MyScoreList newSl = new MyScoreList();
		for (int docId : keySet) {
			float score = 0f;
			if (sl1.scores.containsKey(docId) && !sl2.scores.containsKey(docId)) {
				score = sl1.scores.get(docId);
			} else if (sl2.scores.containsKey(docId)
					&& !sl1.scores.containsKey(docId)) {
				score = sl2.scores.get(docId);
			} else if (sl1.scores.containsKey(docId)
					&& sl2.scores.containsKey(docId)) {
				score = Math.max(sl1.scores.get(docId), sl2.scores.get(docId));
			}
			newSl.addScore(docId, score);
		}
		return newSl;
	}

	private class ScoreDescComparator implements Comparator<Integer> {
		private Map<Integer, Float> base;

		public ScoreDescComparator(Map<Integer, Float> base) {
			this.base = base;
		}

		@Override
		public int compare(Integer o1, Integer o2) {
			float diff = base.get(o1) - base.get(o2);
			// descending ordering by score
			if (diff > 0) {
				return -1;
			} else if (diff < 0) {
				return 1;
			} else {
				// ascending ordering by docId
				if (o1 - o2 > 0) {
					return 1;
				} else {
					return -1;
				}
			}

			// do not return 0 to prevent from merging
		}

	}
}
