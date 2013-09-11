import java.util.List;

/**
 * @author bolei<br/>
 * 
 *         If a query has no explicit query operator, default to #OR;<br/>
 */
public class DefaultOrQryParser extends AbstractDefaultQryParser {

	@Override
	public Qryop getQryOperator(List<QryopTerm> qryTerms) {
		return new QryopOr(qryTerms.toArray(new QryopTerm[] {}));
	}
}
