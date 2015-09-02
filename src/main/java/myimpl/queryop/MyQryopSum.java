package myimpl.queryop;

import java.io.IOException;

import myimpl.queryresult.MyScoreList;
import myimpl.queryresult.operators.MyScoreListDocIdUnionOperator;
import myimpl.queryresult.operators.MyScoreListDocScorePlusOperator;

public class MyQryopSum extends MyQryopCombineScoreLists {
	public MyQryopSum(MyQryop... q) {
		super(q);
	}

	@Override
	public MyScoreList doEvaluation() throws IOException {
		MyScoreList scoreList = args.get(0).evaluate();
		int size = args.size();
		for (int i = 1; i < size; i++) {
			MyScoreList curScoreList = args.get(i).evaluate();
			scoreList = MyScoreList.operate(scoreList, curScoreList,
					new MyScoreListDocIdUnionOperator(),
					new MyScoreListDocScorePlusOperator());
		}
		return scoreList;
	}

}
