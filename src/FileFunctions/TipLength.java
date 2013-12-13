package FileFunctions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class TipLength {

	public static int CalcTipOptLength(String[] fileList) {

		StringBuffer tipsBuff = new StringBuffer();
		
		for (int i = 0; i < fileList.length; i++) {
			tipsBuff.append(ReadTipFile(fileList[i]));
			if (i != fileList.length-1) {
				tipsBuff.append("^");
			}
		}
		
		String[] tips = tipsBuff.toString().split("\\^");
		
		return FindOptTipLength(tips);
	}

	private static StringBuffer ReadTipFile(String fileName) {
		StringBuffer strBuff;
		String line;
		URL url = null;
		try {
			url = TipLength.class.getResource(fileName);
		} catch (Exception e) {
			return null;
		}

		try {
			InputStream in = url.openStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			strBuff = new StringBuffer();
			while ((line = bf.readLine()) != null) {
				strBuff.append(line + "\n");
			}
			return strBuff;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	private static int FindOptTipLength(String[] tips) {
		int[] sizeTip = new int[tips.length];

		for (int i = 0; i < tips.length; i++) {
			sizeTip[i] = tips[i].length();
		}

		int numOfWantedTips = (int) 0.95 * sizeTip.length;
		BubbleSort(sizeTip);
		return sizeTip[numOfWantedTips];
	}

	private static void BubbleSort(int[] intArray) {
		int n = intArray.length;
		int temp = 0;

		for (int i = 0; i < n; i++) {
			for (int j = 1; j < (n - i); j++) {

				if (intArray[j - 1] > intArray[j]) {
					// swap the elements!
					temp = intArray[j - 1];
					intArray[j - 1] = intArray[j];
					intArray[j] = temp;
				}

			}
		}

	}

}