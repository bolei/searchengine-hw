package myimpl.queryresult;

import java.util.TreeMap;
import java.util.TreeSet;

import myimpl.ScoreDescComparator;
import myimpl.queryresult.operators.MyScoreListDocIdOperator;
import myimpl.queryresult.operators.MyScoreListDocScoreOperator;

public class MyScoreList implements MyQryResult {
	// <docId, score>
	private TreeMap<Integer, Double> scores = new TreeMap<Integer, Double>();

	private double defaultScore = 0;

	public void setDefaultScore(double defaultScore) {
		this.defaultScore = defaultScore;
	}

	public void putScore(Integer docId, Double score) {
		scores.put(docId, score);
	}

	public double getScoreForDoc(int docId) {
		if (scores.containsKey(docId)) {
			return scores.get(docId);
		} else {
			return defaultScore;
		}
	}

	public TreeSet<Integer> getDocIds() {
		return new TreeSet<Integer>(scores.keySet());
	}

	public TreeMap<Integer, Double> getSortedScores() {
		TreeMap<Integer, Double> sortedScores = new TreeMap<Integer, Double>(
				new ScoreDescComparator<Integer>(scores));
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
		newSl.defaultScore = docScoreOp.operateDefaultScore(sl1, sl2);
		return newSl;
	}

}
