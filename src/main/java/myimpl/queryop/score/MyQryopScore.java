package myimpl.queryop.score;

import java.io.IOException;

import myimpl.AlgorithmType;
import myimpl.MiscUtil;
import myimpl.queryop.MyQryopInvertedList;
import myimpl.queryop.MyQryopScoreList;
import myimpl.queryresult.MyInvertedList;
import myimpl.queryresult.MyQryResult;
import myimpl.queryresult.MyScoreList;

public abstract class MyQryopScore extends MyQryopScoreList {
	private MyQryopInvertedList arg;

	/**
	 * The SCORE operator accepts just one argument.
	 */
	protected MyQryopScore(MyQryopInvertedList q) {
		this.arg = q;
	}

	/**
	 * convert invert list into score list.
	 */
	public MyScoreList evaluate() throws IOException {

		// Evaluate the query argument.
		MyQryResult result = arg.evaluate();

		if (result instanceof MyScoreList) {
			return (MyScoreList) result;
		}

		if (result instanceof MyInvertedList == false) {
			throw new RuntimeException(
					"MyQryopScore.evaluate() input is not inverted list");
		}

		MyInvertedList invList = (MyInvertedList) result;
		return getScoreList(invList);

	}

	protected abstract MyScoreList getScoreList(MyInvertedList invList)
			throws IOException;

	public static MyQryopScore createQryopScore(MyQryopInvertedList arg) {
		AlgorithmType algType = MiscUtil.getAlgorithmType();
		switch (algType) {
		case RankedBoolean:
			return new MyQryopRankedScore(arg);
		case UnrankedBoolean:
			return new MyQryopUnrakedScore(arg);
		case BM25:
			try {
				return new MyQryopBM25Score(arg);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		case Indri:
			return new MyQryopIndriScore(arg);
		default:
			return null;
		}
	}

}
