package myimpl;

import java.util.LinkedList;
import java.util.List;

/**
 * @author bolei<br/>
 * 
 *         If a query term has no explicit field (see below), default to 'body';
 */
public abstract class AbstractDefaultQryParser implements QryParser {
	@Override
	public MyQryop parseQuery(String[] queryTokens) {
		List<MyQryopTerm> qryTerms = new LinkedList<MyQryopTerm>();

		for (String token : queryTokens) {
			int dotPos = token.indexOf(".");
			if (dotPos == -1) {
				qryTerms.add(new MyQryopTerm(token));
			} else {
				qryTerms.add(new MyQryopTerm(token.substring(0, dotPos), token
						.substring(dotPos) + 1));
			}
		}
		return getQryOperator(qryTerms);
	}

	public abstract MyQryop getQryOperator(List<MyQryopTerm> qryTerms);
}
