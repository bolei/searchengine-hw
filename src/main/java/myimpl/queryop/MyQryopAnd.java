package myimpl.queryop;

import java.io.IOException;

import myimpl.queryresult.MyScoreList;
import myimpl.queryresult.operators.MyScoreListDocIdIntersectionOperator;
import myimpl.queryresult.operators.MyScoreListDocScoreMinOperator;

public class MyQryopAnd extends MyQryopCombineScoreLists {
	public MyQryopAnd(MyQryop... q) {
		super(q);
	}

	/**
	 * Evaluate the query operator.
	 * @throws IOException 
	 */
	@Override
	protected MyScoreList doEvaluation() throws IOException {
		MyScoreList result = args.get(0).evaluate();

		// Each pass of the loop evaluates one query argument.
		for (int i = 1; i < args.size(); i++) {

			MyScoreList iResult = args.get(i).evaluate();

			result = MyScoreList.operate(result, iResult,
					new MyScoreListDocIdIntersectionOperator(),
					new MyScoreListDocScoreMinOperator());
		}

		return result;
	}
}
