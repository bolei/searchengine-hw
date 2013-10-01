package myimpl.queryop;

import java.io.IOException;

import myimpl.queryresult.MyQryResult;

public class MyQryopEmpty extends MyQryop {

	@Override
	public MyQryResult evaluate() throws IOException {
		return null;
	}

}
