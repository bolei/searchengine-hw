package myimpl;

import java.io.IOException;

public interface QryParser {
	public MyQryop parseQuery(String queryStr) throws IOException;
}
