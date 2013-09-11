package myimpl;

import java.io.IOException;
import java.util.Collections;

public class MyQryopNear extends MyQryop {
	public MyQryopNear(MyQryop... myQryops) {
		Collections.addAll(args, myQryops);
	}

	@Override
	public MyInvertedList evaluate() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
