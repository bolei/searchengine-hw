import java.io.IOException;
import java.util.Collections;

public class QryopOr extends Qryop {
	public QryopOr(Qryop... q) {
		Collections.addAll(args, q);
	}

	@Override
	public QryResult evaluate() throws IOException {
		Qryop impliedQryOp = new QryopScore(args.get(0));
		QryResult result = impliedQryOp.evaluate();
		for (int i = 1; i < args.size(); i++) {
			impliedQryOp = new QryopScore(args.get(i));
			QryResult iResult = impliedQryOp.evaluate();
			int rDoc = 0; /* Index of a document in result. */
			int iDoc = 0; /* Index of a document in iResult. */
			while (rDoc < result.docScores.scores.size()) {
				while ((iDoc < iResult.docScores.scores.size())
						&& (result.docScores.getDocid(rDoc) > iResult.docScores
								.getDocid(iDoc))) {
					iDoc++;
				}
				if ((iDoc < iResult.docScores.scores.size())
						&& (result.docScores.getDocid(rDoc) == iResult.docScores
								.getDocid(iDoc))) {
					rDoc++;
					iDoc++;
				} else {
					result.docScores.add(result.docScores.getDocid(rDoc),
							result.docScores.getDocidScore(rDoc));
				}
			}
		}
		return result;
	}

}
