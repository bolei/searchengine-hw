package myimpl.queryop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import myimpl.queryop.score.MyQryopScore;
import myimpl.queryresult.MyScoreList;

public abstract class MyQryopCombineScoreLists extends MyQryopScoreList {

	protected List<MyQryopScoreList> args = new ArrayList<MyQryopScoreList>();

	public MyQryopCombineScoreLists(MyQryop... q) {
		for (int i = 0; i < q.length; i++) {
			if (q[i] instanceof MyQryopInvertedList) {
				this.args.add(MyQryopScore
						.createQryopScore((MyQryopInvertedList) q[i]));
			} else {
				args.add((MyQryopScore) q[i]);
			}
		}
	}

	@Override
	public MyScoreList evaluate() throws IOException {
		if (args.isEmpty()) {
			return new MyScoreList();
		}
		return doEvaluation();
	}

	protected abstract MyScoreList doEvaluation() throws IOException;
}
