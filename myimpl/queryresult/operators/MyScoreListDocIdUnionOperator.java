package myimpl.queryresult.operators;

import java.util.TreeSet;

import myimpl.queryresult.MyScoreList;

public class MyScoreListDocIdUnionOperator implements MyScoreListDocIdOperator {

	@Override
	public TreeSet<Integer> operateDocId(MyScoreList sl1, MyScoreList sl2) {
		TreeSet<Integer> keySet = new TreeSet<Integer>(sl1.getScores().keySet());
		keySet.addAll(sl2.getScores().keySet());
		return keySet;
	}

}
