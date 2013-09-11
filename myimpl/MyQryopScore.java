package myimpl;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

public class MyQryopScore extends MyQryop {

	/**
	 * The SCORE operator accepts just one argument.
	 */
	public MyQryopScore(MyQryop q) {
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

		MyScoreList scoreList = new MyScoreList();

		// Each pass of the loop computes a score for one document. Note: If
		// the evaluate operation above returned a score list (which is very
		// possible), this loop gets skipped.

		MyInvertedList invList = (MyInvertedList) result;

		TreeMap<Integer, TreeSet<Integer>> postings = invList.getDocPostings();

		for (Entry<Integer, TreeSet<Integer>> entry : postings.entrySet()) {

			// DIFFERENT RETRIEVAL MODELS IMPLEMENT THIS DIFFERENTLY.
			// Unranked Boolean. All matching documents get a score of 1.0.
			scoreList.addScore(entry.getKey(), (float) 1.0);
		}

		return scoreList;
	}
}
