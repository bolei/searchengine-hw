package myimpl.queryresult.operators;

import myimpl.queryresult.MyScoreList;

public class MyScoreListDocScoreMinOperator implements
		MyScoreListDocScoreOperator {

	@Override
	public double operateDocScore(MyScoreList sl1, MyScoreList sl2, int docId) {
		double sc1 = sl1.getDefaultScore(), sc2 = sl2.getDefaultScore();
		if (sl1.getScores().containsKey(docId)) {
			sc1 = sl1.getScores().get(docId);
		}
		if (sl2.getScores().containsKey(docId)) {
			sc2 = sl2.getScores().get(docId);
		}
		return Math.min(sc1, sc2);
	}

	@Override
	public double operateDefaultScore(MyScoreList sl1, MyScoreList sl2) {
		return Math.min(sl1.getDefaultScore(), sl2.getDefaultScore());
	}

}
