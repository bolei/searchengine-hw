package myimpl.queryop;

import java.io.IOException;

import myimpl.queryresult.MyScoreList;
import myimpl.queryresult.operators.MyScoreListDocIdUnionOperator;
import myimpl.queryresult.operators.MyScoreListDocScoreMaxOperator;

public class MyQryopOr extends MyQryopCombineScoreLists {
	public MyQryopOr(MyQryop[] myQryops) {
		super(myQryops);
	}

	@Override
	public MyScoreList doEvaluation() throws IOException {
		MyScoreList result = args.get(0).evaluate();
		for (int i = 1; i < args.size(); i++) {
			MyScoreList iResult = args.get(i).evaluate();
			result = MyScoreList.operate(result, iResult,
					new MyScoreListDocIdUnionOperator(),
					new MyScoreListDocScoreMaxOperator());

		}
		return result;
	}

}
