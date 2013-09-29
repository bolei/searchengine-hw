package myimpl.queryop;

import java.io.IOException;

import myimpl.queryresult.MyScoreList;
import myimpl.queryresult.operators.MyScoreListDocIdUnionOperator;
import myimpl.queryresult.operators.MyScoreListDocScorePlusOperator;

public class MyQryopWeight extends MyQryopCombineScoreLists {

	private double[] weights;
	private double totalWeight = 0;

	public MyQryopWeight(MyQryop[] q, double[] weights) {
		super(q);
		if (weights.length != q.length) {
			throw new RuntimeException(
					"weight lenth is not equal to qury operator length");
		}
		this.weights = weights;
		for (int i = 0; i < weights.length; i++) {
			totalWeight += weights[i];
		}
	}

	@Override
	public MyScoreList doEvaluation() throws IOException {
		MyScoreList sl = applyWeight(0);

		for (int i = 1; i < weights.length; i++) {
			MyScoreList curSl = applyWeight(i);
			MyScoreList.operate(sl, curSl, new MyScoreListDocIdUnionOperator(),
					new MyScoreListDocScorePlusOperator());
		}
		return sl;
	}

	private MyScoreList applyWeight(int index) throws IOException {
		MyScoreList scoreList = args.get(index).evaluate();
		for (int docId : scoreList.getScores().keySet()) {
			double oldScore = scoreList.getScores().get(docId);
			scoreList.putScore(docId, oldScore * weights[index] / totalWeight);
		}
		return scoreList;
	}

}
