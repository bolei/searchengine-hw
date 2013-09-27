package myimpl.queryresult;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class MyScoreList implements MyQryResult {
	// <docId, score>
	private TreeMap<Integer, Double> scores = new TreeMap<Integer, Double>();

	public void addScore(Integer docId, Double score) {
		scores.put(docId, score);
	}

	public TreeMap<Integer, Double> getScores() {
		return scores;
	}

	public TreeMap<Integer, Double> getSortedScores() {
		TreeMap<Integer, Double> sortedScores = new TreeMap<Integer, Double>(
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
			double score = 0d;
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

	public static MyScoreList plus(MyScoreList sl1, MyScoreList sl2) {
		TreeSet<Integer> keySet = new TreeSet<Integer>(sl1.scores.keySet());
		keySet.addAll(sl2.scores.keySet());
		MyScoreList newSl = new MyScoreList();
		for (int docId : keySet) {
			double score = 0d;
			if (sl1.scores.containsKey(docId) && !sl2.scores.containsKey(docId)) {
				score = sl1.scores.get(docId);
			} else if (sl2.scores.containsKey(docId)
					&& !sl1.scores.containsKey(docId)) {
				score = sl2.scores.get(docId);
			} else if (sl1.scores.containsKey(docId)
					&& sl2.scores.containsKey(docId)) {
				score = sl1.scores.get(docId) + sl2.scores.get(docId);
			}
			newSl.addScore(docId, score);
		}
		return newSl;
	}

	private class ScoreDescComparator implements Comparator<Integer> {
		private Map<Integer, Double> base;

		public ScoreDescComparator(Map<Integer, Double> base) {
			this.base = base;
		}

		@Override
		public int compare(Integer o1, Integer o2) {
			double diff = base.get(o1) - base.get(o2);
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
