package myimpl;

import java.io.IOException;

public class MyQryopAnd extends MyQryop {

	/**
	 * It is convenient for the constructor to accept a variable number of
	 * arguments. Thus new qryopAnd (arg1, arg2, arg3, ...).
	 */
	public MyQryopAnd(MyQryop... q) {
		for (int i = 0; i < q.length; i++)
			this.args.add(q[i]);
	}

	/**
	 * Evaluate the query operator.
	 */
	public MyScoreList evaluate() throws IOException {

		MyQryopScore impliedQryOp = new MyQryopScore(args.get(0));
		MyScoreList result = impliedQryOp.evaluate();

		// Each pass of the loop evaluates one query argument.
		for (int i = 1; i < args.size(); i++) {

			impliedQryOp = new MyQryopScore(args.get(i));
			MyScoreList iResult = impliedQryOp.evaluate();

			result = MyScoreList.intersection(result, iResult);
		}

		return result;
	}
}
