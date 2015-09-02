package myimpl.queryop;

import java.io.IOException;

import myimpl.queryresult.MyInvertedList;

public abstract class MyQryopInvertedList extends MyQryop {

	@Override
	public abstract MyInvertedList evaluate() throws IOException;

}
