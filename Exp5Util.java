import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Exp5Util {
	public static void main(String[] args) throws IOException {
		BufferedReader bin1 = new BufferedReader(new FileReader(args[0]));
		BufferedReader bin2 = new BufferedReader(new FileReader(args[1]));
		String line1, line2;
		try {
			while ((line1 = bin1.readLine()) != null
					&& (line2 = bin2.readLine()) != null) {
				String[] arr1 = line1.split(":");
				String[] arr2 = line2.split(":");
				System.out.println(arr1[0] + ":#WEIGHT (" + args[2] + " "
						+ arr1[1] + " " + args[3] + " " + arr2[1] + ")");
			}
		} finally {
			bin1.close();
			bin2.close();
		}

	}
}
