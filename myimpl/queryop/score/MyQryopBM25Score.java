package myimpl.queryop.score;

import java.io.IOException;

import myimpl.MiscUtil;
import myimpl.queryop.MyQryop;
import myimpl.queryresult.MyInvertedList;
import myimpl.queryresult.MyScoreList;

public class MyQryopBM25Score extends MyQryopScore {
	private double k1, b, k3;

	public MyQryopBM25Score(MyQryop q, double k1, double b, double k3)
			throws IOException {
		super(q);
		this.k1 = k1;
		this.b = b;
		this.k3 = k3;
	}

	@Override
	protected MyScoreList getScoreList(MyInvertedList invList)
			throws IOException {
		MyScoreList scoreList = new MyScoreList();
		String fieldName = invList.getFieldString();
		double N = MiscUtil.getIndexReader().getDocCount(fieldName);
		double totalLenth = MiscUtil.getIndexReader().getSumTotalTermFreq(
				fieldName);
		double avg_doclen = totalLenth / N;
		double df = invList.getDf();
		double qtf = 1;
		double rsjWeight = Math.log((N - df + 0.5) / (df + 0.5));
		double userWeight = (k3 + 1) * qtf / (k3 + qtf); // effectively equals 1
		for (int docId : invList.getDocPostings().keySet()) {
			double docLen = MiscUtil.getDocLengthStore().getDocLength(
					fieldName, docId);
			double tf = invList.getTf(docId);
			double tfWeight = tf
					/ (tf + k1 * (1 - b + b * (docLen / avg_doclen)));
			double score = rsjWeight * tfWeight * userWeight;
			scoreList.addScore(docId, score);
		}
		return scoreList;
	}

}
