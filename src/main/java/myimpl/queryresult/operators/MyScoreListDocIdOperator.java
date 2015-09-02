package myimpl.queryresult.operators;

import java.util.TreeSet;

import myimpl.queryresult.MyScoreList;

public interface MyScoreListDocIdOperator {
	public TreeSet<Integer> operateDocId(MyScoreList sl1, MyScoreList sl2);
}
