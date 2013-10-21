import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Hw3QueryResultMAPCollector {
	public static void main(String[] args) throws IOException {
		String resultFile = args[0];
		BufferedReader bin = new BufferedReader(new FileReader(resultFile));
		String line;
		boolean summary = false;

		String p10 = "p10", p20 = "p20", p30 = "p30", mapAll = "mapAll", numq = "numq";

		while ((line = bin.readLine()) != null) {
			String[] strArr = line.split("\\s+");

			if (summary == false) {
				if (strArr[0].equals("map")) {
					System.out.println(strArr[2]);
					continue;
				}
				if (strArr[0].equals("num_q") && strArr[1].equals("all")) {
					numq = strArr[2];
					summary = true;
					continue;
				}
			} else {
				if (strArr[0].equals("map")) {
					mapAll = strArr[2];
					continue;
				}
				if (strArr[0].equals("P10")) {
					p10 = strArr[2];
					continue;
				}
				if (strArr[0].equals("P20")) {
					p20 = strArr[2];
					continue;
				}
				if (strArr[0].equals("P30")) {
					p30 = strArr[2];
					continue;
				}
			}
		}
		System.out.println("===========");
		System.out.println(p10);
		System.out.println(p20);
		System.out.println(p30);
		System.out.println(mapAll);
		System.out.println(numq);
		bin.close();
	}
}
