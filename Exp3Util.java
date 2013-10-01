import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Exp3Util {
	public static void main(String[] args) throws IOException {
		BufferedReader bin = new BufferedReader(new FileReader(args[0]));
		try {
			String field = args[1];
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = bin.readLine()) != null) {
				int pos = line.indexOf(':');
				sb.append(line.substring(0, pos) + ":#AND (");
				String[] tokens = line.substring(pos + 1).split(" ");
				for (String t : tokens) {
					sb.append(t + "." + field + " ");
				}
				sb.append(")\n");

			}
			System.out.println(sb.toString());
		} finally {
			bin.close();
			bin = null;
		}
	}
}
