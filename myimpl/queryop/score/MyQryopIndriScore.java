package myimpl.queryop.score;

import java.io.IOException;

import myimpl.MiscUtil;
import myimpl.queryop.MyQryopInvertedList;
import myimpl.queryresult.MyInvertedList;
import myimpl.queryresult.MyScoreList;

public class MyQryopIndriScore extends MyQryopScore {
	private static double lambda = Double.parseDouble(MiscUtil.getProp().get(
			"Indri:lambda"));
	private static double mu = Double.parseDouble(MiscUtil.getProp().get(
			"Indri:mu"));
	private static String smoothing = MiscUtil.getProp().get("Indri:smoothing");

	protected MyQryopIndriScore(MyQryopInvertedList q) {
		super(q);
	}

	@Override
	protected MyScoreList getScoreList(MyInvertedList invList)
			throws IOException {
		MyScoreList scoreList = new MyScoreList();
		if (invList.getDf() == 0) {
			scoreList.setDefaultScore(0d);
			return scoreList;
		}
		double qic = getMLEofQiC(invList);
		for (int docId : invList.getDocPostings().keySet()) {
			double tf = invList.getTf(docId);
			double docLen = MiscUtil.getDocLengthStore().getDocLength(
					invList.getFieldString(), docId);
			double score = Math.log(lambda * (tf + mu * qic) / (docLen + mu)
					+ (1 - lambda) * qic);
			scoreList.putScore(docId, score);
		}

		double avgDocLen = MiscUtil.getIndexReader().getSumTotalTermFreq(
				invList.getFieldString())
				/ (double) MiscUtil.getIndexReader().getDocCount(
						invList.getFieldString());
		double prob = lambda * (mu * qic) / (avgDocLen + mu) + (1 - lambda)
				* qic;

		scoreList.setDefaultScore(Math.log(prob));
		return scoreList;
	}

	private double getMLEofQiC(MyInvertedList invList) throws IOException {
		if (smoothing.equals("df")) {
			int df = invList.getDf();
			if (df == 0) {
				return 0;
			}
			double lenDoc = MiscUtil.getIndexReader().getDocCount(
					invList.getFieldString());
			return df / lenDoc;
		} else { // default ctf
			int ctf = invList.getCtf();
			if (ctf == 0) {
				return 0;
			}
			double lenTerm = MiscUtil.getIndexReader().getSumTotalTermFreq(
					invList.getFieldString());
			return ctf / lenTerm;
		}
	}

}
