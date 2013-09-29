package myimpl.queryresult;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import myimpl.queryresult.operators.MyScoreListDocIdOperator;
import myimpl.queryresult.operators.MyScoreListDocScoreOperator;

public class MyScoreList implements MyQryResult {
	// <docId, score>
	private TreeMap<Integer, Double> scores = new TreeMap<Integer, Double>();

	private double defaultScore = 0;

	public double getDefaultScore() {
		return defaultScore;
	}

	public void setDefaultScore(double defaultScore) {
		this.defaultScore = defaultScore;
	}

	public void putScore(Integer docId, Double score) {
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

	public static MyScoreList operate(MyScoreList sl1, MyScoreList sl2,
			MyScoreListDocIdOperator docIdOp,
			MyScoreListDocScoreOperator docScoreOp) {
		TreeSet<Integer> keySet = docIdOp.operateDocId(sl1, sl2);
		MyScoreList newSl = new MyScoreList();
		for (int docId : keySet) {
			newSl.putScore(docId, docScoreOp.operateDocScore(sl1, sl2, docId));
		}
		newSl.setDefaultScore(docScoreOp.operateDefaultScore(sl1, sl2));
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
