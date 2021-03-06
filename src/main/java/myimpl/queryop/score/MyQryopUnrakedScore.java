package myimpl.queryop.score;

import java.util.TreeMap;
import java.util.TreeSet;

import myimpl.queryop.MyQryopInvertedList;
import myimpl.queryresult.MyInvertedList;
import myimpl.queryresult.MyScoreList;

public class MyQryopUnrakedScore extends MyQryopScore {

	protected MyQryopUnrakedScore(MyQryopInvertedList q) {
		super(q);
	}

	@Override
	protected MyScoreList getScoreList(MyInvertedList invList) {
		MyScoreList scoreList = new MyScoreList();
		TreeMap<Integer, TreeSet<Integer>> postings = invList.getDocPostings();

		// Each pass of the loop computes a score for one document.
		for (int docId : postings.keySet()) {

			// Unranked Boolean. All matching documents get a score of 1.0.
			scoreList.putScore(docId, 1.0d);
		}

		return scoreList;
	}

}
