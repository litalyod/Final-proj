package FileFunctions;

public class ReadTip {
	
	
	public static String nextTip(String[] quotes) {
		int randomQuoteIndex = (int) Math.round((quotes.length - 1)
				* Math.random());
		String quote = quotes[randomQuoteIndex];
		return quote;
	}
	
	
}