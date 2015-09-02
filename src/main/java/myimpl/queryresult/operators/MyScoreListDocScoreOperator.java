package myimpl.queryresult.operators;

import myimpl.queryresult.MyScoreList;

public interface MyScoreListDocScoreOperator {
	public double operateDocScore(MyScoreList sl1, MyScoreList sl2, int docId);

	public double operateDefaultScore(MyScoreList sl1, MyScoreList sl2);
}