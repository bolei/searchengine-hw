package myimpl;

import java.util.List;

/**
 * @author bolei<br/>
 * 
 *         If a query has no explicit query operator, default to #OR;<br/>
 */
public class DefaultOrQryParser extends AbstractDefaultQryParser {

	@Override
	public MyQryop getQryOperator(List<MyQryopTerm> qryTerms) {
		return new MyQryopOr(qryTerms.toArray(new MyQryopTerm[] {}));
	}
}
