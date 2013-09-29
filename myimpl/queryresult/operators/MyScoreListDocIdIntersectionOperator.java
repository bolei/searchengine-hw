package myimpl.queryresult.operators;

import java.util.TreeSet;

import myimpl.queryresult.MyScoreList;

public class MyScoreListDocIdIntersectionOperator implements
		MyScoreListDocIdOperator {

	@Override
	public TreeSet<Integer> operateDocId(MyScoreList sl1, MyScoreList sl2) {
		TreeSet<Integer> keySet = new TreeSet<Integer>(sl1.getDocIds());
		keySet.retainAll(sl2.getDocIds());
		return keySet;
	}

}
