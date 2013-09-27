package myimpl.queryop;

import java.io.IOException;

import myimpl.MiscUtil;
import myimpl.queryop.score.MyQryopBM25Score;
import myimpl.queryresult.MyScoreList;

public class MyQryopSum extends MyQryop {

	public MyQryopSum(MyQryop[] q) {
		for (int i = 0; i < q.length; i++) {
			this.args.add(q[i]);
		}

	}

	@Override
	public MyScoreList evaluate() throws IOException {
		if (args.isEmpty()) {
			return new MyScoreList();
		}
		double k1 = Double.parseDouble(MiscUtil.getProp().get("BM25:k_1"));
		double b = Double.parseDouble(MiscUtil.getProp().get("BM25:b"));
		double k3 = Double.parseDouble(MiscUtil.getProp().get("BM25:k_3"));
		MyScoreList scoreList = new MyQryopBM25Score(args.get(0), k1, b, k3)
				.evaluate();
		int size = args.size();
		for (int i = 1; i < size; i++) {
			MyScoreList curScoreList = new MyQryopBM25Score(args.get(i), k1, b,
					k3).evaluate();
			scoreList = MyScoreList.plus(scoreList, curScoreList);
		}
		return scoreList;
	}

}
