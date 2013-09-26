package myimpl.queryop;

import java.io.IOException;
import java.util.Collections;

import myimpl.queryresult.MyScoreList;

public class MyQryopOr extends MyQryop {
	public MyQryopOr(MyQryop... q) {
		Collections.addAll(args, q);
	}

	@Override
	public MyScoreList evaluate() throws IOException {
		MyQryopScore impliedQryOp = MyQryopScore.createQryopScore(args.get(0));
		MyScoreList result = impliedQryOp.evaluate();
		for (int i = 1; i < args.size(); i++) {
			impliedQryOp = MyQryopScore.createQryopScore(args.get(i));
			MyScoreList iResult = impliedQryOp.evaluate();
			result = MyScoreList.union(result, iResult);
		}
		return result;
	}

}
