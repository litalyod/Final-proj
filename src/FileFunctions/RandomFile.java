package FileFunctions;

public class RandomFile {
	
	public static int calcRandomFileIndex(String[] weights, String fileList) {
		int sumWeights = 0;
		for (int i = 0; i < weights.length; i++) {
			sumWeights += Integer.parseInt(weights[i]);
		}
		int randomFileIndex = 0;
		int sumSoFar = 0;
		int randomResult = (int) (sumWeights * Math.random());
		for (; randomFileIndex < fileList.length(); randomFileIndex++) {
			sumSoFar += Integer.parseInt(weights[randomFileIndex]);
			if (sumSoFar > randomResult) {
				break;
			}
		}
		return randomFileIndex;
	}
}