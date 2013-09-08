import java.util.LinkedList;
import java.util.List;

/**
 * @author bolei<br/>
 * 
 *         If a query term has no explicit field (see below), default to 'body';
 */
public abstract class AbstractDefaultQryParser implements QryParser {
	@Override
	public Qryop parseQuery(String[] queryTokens) {
		List<QryopTerm> qryTerms = new LinkedList<QryopTerm>();

		for (String token : queryTokens) {
			int dotPos = token.indexOf(".");
			if (dotPos == -1) {
				qryTerms.add(new QryopTerm(token));
			} else {
				qryTerms.add(new QryopTerm(token.substring(0, dotPos), token
						.substring(dotPos) + 1));
			}
		}
		return getQryOperator(qryTerms);
	}

	public abstract Qryop getQryOperator(List<QryopTerm> qryTerms);
}
