import java.util.List;

public class DefaultAndQryParser extends AbstractDefaultQryParser {

	@Override
	public Qryop getQryOperator(List<QryopTerm> qryTerms) {
		return new QryopAnd(qryTerms.toArray(new QryopTerm[] {}));
	}

}
