package Folders;


import FileFunctions.ReadFile;

public class RandomFolder {
	
	public static String CalcRandomFolder(String path){
		String[] file;
		String[] folders;
		String[] weights;

		file = ReadFile.ReadFileFromPath(path+"folders.txt").toString().split("\\^");
		folders = file[0].split(",");
		weights = file[1].split(",");
		
		return folders[calcRandomFolderIndex(weights, file[0])];

		
	}
	
	private static int calcRandomFolderIndex(String[] weights, String folderList) {
		int sumWeights = 0;
		for (int i = 0; i < weights.length; i++) {
			sumWeights += Integer.parseInt(weights[i]);
		}
		int randomFolderIndex = 0;
		int sumSoFar = 0;
		int randomResult = (int) (sumWeights * Math.random());
		for (; randomFolderIndex < folderList.length(); randomFolderIndex++) {
			sumSoFar += Integer.parseInt(weights[randomFolderIndex]);
			if (sumSoFar > randomResult) {
				break;
			}
		}
		return randomFolderIndex;
	}
	
	
}