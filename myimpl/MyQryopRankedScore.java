package myimpl;

public class MyQryopRankedScore extends MyQryopScore {

	// private static final int NUM_DOCS = MiscUtil.getIndexReader().numDocs();

	protected MyQryopRankedScore(MyQryop q) {
		super(q);
	}

	@Override
	protected MyScoreList getScoreList(MyInvertedList invList) {
		MyScoreList scoreList = new MyScoreList();
		// double idf = Math.log(1.0d * NUM_DOCS / (invList.getDf() + 1));
		for (int docId : invList.getDocPostings().keySet()) {
			double tf = Math.log(invList.getTf(docId) + 1);
			// double score = tf * idf;
			double score = tf;
			scoreList.addScore(docId, score);
		}
		return scoreList;
	}

}
