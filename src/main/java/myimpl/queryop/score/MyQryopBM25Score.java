package myimpl.queryop.score;

import java.io.IOException;

import myimpl.MiscUtil;
import myimpl.queryop.MyQryopInvertedList;
import myimpl.queryresult.MyInvertedList;
import myimpl.queryresult.MyScoreList;

public class MyQryopBM25Score extends MyQryopScore {

	private static double k1 = Double.parseDouble(MiscUtil.getProp().get(
			"BM25:k_1"));
	private static double b = Double.parseDouble(MiscUtil.getProp().get(
			"BM25:b"));
	private static double k3 = Double.parseDouble(MiscUtil.getProp().get(
			"BM25:k_3"));

	public MyQryopBM25Score(MyQryopInvertedList q) throws IOException {
		super(q);
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
			scoreList.putScore(docId, score);
		}
		// default score is 0. no need to set
		return scoreList;
	}

}
