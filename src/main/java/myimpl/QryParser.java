package myimpl;

import java.io.IOException;

import myimpl.queryop.MyQryop;

public interface QryParser {
	public MyQryop parseQuery(String queryStr) throws IOException;
}
