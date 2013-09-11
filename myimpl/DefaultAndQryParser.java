package myimpl;
import java.util.List;

public class DefaultAndQryParser extends AbstractDefaultQryParser {

	@Override
	public MyQryop getQryOperator(List<MyQryopTerm> qryTerms) {
		return new MyQryopAnd(qryTerms.toArray(new MyQryopTerm[] {}));
	}

}
