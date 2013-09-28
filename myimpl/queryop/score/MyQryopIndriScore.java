package myimpl.queryop.score;

import java.io.IOException;

import myimpl.MiscUtil;
import myimpl.queryop.MyQryopInvertedList;
import myimpl.queryresult.MyInvertedList;
import myimpl.queryresult.MyScoreList;

public class MyQryopIndriScore extends MyQryopScore {
	private double lambda, mu;
	private String smoothing = "ctf";

	protected MyQryopIndriScore(MyQryopInvertedList q) {
		super(q);
		this.lambda = Double
				.parseDouble(MiscUtil.getProp().get("Indri:lambda"));
		this.mu = Double.parseDouble(MiscUtil.getProp().get("Indri:mu"));
		this.smoothing = MiscUtil.getProp().get("Indri:smoothing");
	}

	@Override
	protected MyScoreList getScoreList(MyInvertedList invList)
			throws IOException {
		MyScoreList scoreList = new MyScoreList();
		double qic = getMLEofQiC(invList);
		for (int docId : invList.getDocPostings().keySet()) {
			double tf = invList.getTf(docId);
			double docLen = MiscUtil.getDocLengthStore().getDocLength(
					invList.getFieldString(), docId);
			double score = Math.log(lambda * (tf + mu * qic) / (docLen + mu)
					+ (1 - lambda) * qic);
			scoreList.putScore(docId, score);
		}
		return scoreList;
	}

	private double getMLEofQiC(MyInvertedList invList) throws IOException {
		if (smoothing.equals("df")) {
			double df = invList.getDf();
			double lenDoc = MiscUtil.getIndexReader().getDocCount(
					invList.getFieldString());
			return df / lenDoc;
		} else { // default ctf
			double ctf = invList.getCtf();
			double lenTerm = MiscUtil.getIndexReader().getSumTotalTermFreq(
					invList.getFieldString());
			return ctf / lenTerm;

		}
	}

}
