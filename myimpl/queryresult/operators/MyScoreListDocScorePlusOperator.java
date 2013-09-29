package myimpl.queryresult.operators;

import myimpl.queryresult.MyScoreList;

public class MyScoreListDocScorePlusOperator implements
		MyScoreListDocScoreOperator {

	@Override
	public double operateDocScore(MyScoreList sl1, MyScoreList sl2, int docId) {
		double sc1 = sl1.getScoreForDoc(docId), sc2 = sl2.getScoreForDoc(docId);
		return sc1 + sc2;
	}

	@Override
	public double operateDefaultScore(MyScoreList sl1, MyScoreList sl2) {
		return sl1.getScoreForDoc(-1) + sl2.getScoreForDoc(-1);
	}

}
