
import FileFunctions.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JApplet;

public class Tipapplet extends JApplet implements Runnable {

	
	private final static int NUM_CHARS_PER_SEC         = 25;
    private final static int NUM_QUOTES_FROM_SAME_FILE =  3;
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String fileToRead;
	TextArea txtArea;
	StringBuffer quote = new StringBuffer();
	Thread runner, t;
	int count = 0;
	String[] quotes;
	int optTipLength = 0;
	

	String fileList = "bugs.txt,compQuotes.txt,cpp.txt,general.txt";
	int[] weights = { 50, 150, 25, 220 };

	public StringBuffer readFile() {
		StringBuffer strBuff;
		String line;
		URL url = null;
		try {
			url = Tipapplet.class.getResource(fileToRead);
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

	public void start() {
		runner = new Thread((Runnable) this);
		runner.start();
	}

	@SuppressWarnings("static-access")
	public void run() {
		t = Thread.currentThread();
		while (t == runner) {
			repaint();
			try {
				t.sleep(1000 * quote.length() / NUM_CHARS_PER_SEC);
			} catch (InterruptedException e) {
			}
		}
	}

	public void init() {

		txtArea = new TextArea("", 5, 100, TextArea.SCROLLBARS_NONE);
		txtArea.setEditable(false);
		txtArea.setFont(new Font("TimesRoman", Font.LAYOUT_LEFT_TO_RIGHT, 16));
		add(txtArea);
		optTipLength = TipLength.CalcTipOptLength(fileList.split(","));
		System.out.println(optTipLength);

	}
	
	public boolean ReadNewQuote() {
		if (count == 0) {
			int randomFileIndex = RandomFile.calcRandomFileIndex(weights, fileList);
			String[] files = fileList.split(",");
			fileToRead = files[randomFileIndex];
			String prHtml = this.getParameter("fileToRead");
			if (prHtml != null)
				fileToRead = new String(prHtml);
			StringBuffer strBuff = readFile();
			if (strBuff == null) {
				return false;
			}

			String context = strBuff.toString();

			quotes = context.split("\\^");
		}
		quote.delete(0, quote.length());
		
		String tip = ReadTip.nextTip(quotes);
		
		while (tip.length() > optTipLength) {
			tip = ReadTip.nextTip(quotes);
		}
		
		quote.append(tip);
		count++;
		if (count > NUM_QUOTES_FROM_SAME_FILE) {
			count = 0;
		}
		return true;
	}

	public void paint(Graphics g) {

		if (!ReadNewQuote()) {
			return;
		}
		txtArea.setText(quote.toString());

	}

}