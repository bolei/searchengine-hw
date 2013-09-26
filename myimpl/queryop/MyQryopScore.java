package myimpl.queryop;

import java.io.IOException;

import myimpl.queryresult.MyInvertedList;
import myimpl.queryresult.MyQryResult;
import myimpl.queryresult.MyScoreList;

public abstract class MyQryopScore extends MyQryop {

	private static boolean isRankedModel = false;

	/**
	 * The SCORE operator accepts just one argument.
	 */
	protected MyQryopScore(MyQryop q) {
		this.args.add(q);
	}

	/**
	 * convert invert list into score list.
	 */
	public MyScoreList evaluate() throws IOException {

		// Evaluate the query argument.
		MyQryResult result = args.get(0).evaluate();

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

	protected abstract MyScoreList getScoreList(MyInvertedList invList);

	public static void setRankedModel(boolean isRanked) {
		isRankedModel = isRanked;
	}

	public static MyQryopScore createQryopScore(MyQryop arg) {
		if (isRankedModel) {
			return new MyQryopRankedScore(arg);
		} else {
			return new MyQryopUnrakedScore(arg);
		}
	}

}
