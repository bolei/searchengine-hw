package myimpl.queryresult.operators;

import java.util.TreeSet;

import myimpl.queryresult.MyScoreList;

public class MyScoreListDocIdUnionOperator implements MyScoreListDocIdOperator {

	@Override
	public TreeSet<Integer> operateDocId(MyScoreList sl1, MyScoreList sl2) {
		TreeSet<Integer> keySet = new TreeSet<Integer>(sl1.getDocIds());
		keySet.addAll(sl2.getDocIds());
		return keySet;
	}
}
