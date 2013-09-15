package myimpl;

public class MyQryopRankedScore extends MyQryopScore {

	protected MyQryopRankedScore(MyQryop q) {
		super(q);
	}

	@Override
	protected MyScoreList getScoreList(MyInvertedList invList) {
		MyScoreList scoreList = new MyScoreList();
		// int numDocs = MiscUtil.getIndexReader().numDocs();
		// double idf = Math.log(1.0d * numDocs / (invList.getDf() + 1));
		for (int docId : invList.getDocPostings().keySet()) {
			double tf = Math.log(invList.getTf(docId) + 1);
			// double score = tf * idf;
			double score = tf;
			scoreList.addScore(docId, score);
		}
		return scoreList;
	}

}
