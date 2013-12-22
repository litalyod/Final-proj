import FileFunctions.*;
import Folders.RandomFolder;

import java.awt.*;
import javax.swing.JApplet;

public class Tipapplet extends JApplet implements Runnable {

	private final static int NUM_CHARS_PER_SEC = 25;
	private final static int NUM_QUOTES_FROM_SAME_FILE = 3;

	private final static int IMAGE_SLEEP_TIME = 1000;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String fileToRead;
	String folderToRead;
	TextArea txtArea;
	StringBuffer quote = new StringBuffer();
	Image image = null;
	MediaTracker mt;
	Thread runner, t;
	int count = 0;
	String[] quotes;
	boolean isTextFile = true;
	int optTipLength = 10000;

	String fileList;// = "bugs.txt,compQuotes.txt,cpp.txt,general.txt";
	String[] weights;// = { 50, 150, 25, 220 };
/*
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
	}*/

	public void start() {
		runner = new Thread((Runnable) this);
		runner.start();
	}

	@SuppressWarnings("static-access")
	public void run() {
		t = Thread.currentThread();
		while (t == runner) {
			repaint();
			if (isTextFile) {
				try {
					t.sleep(1000 * quote.length() / NUM_CHARS_PER_SEC);
				} catch (InterruptedException e) {
				}
			} else {
				try {
					t.sleep(IMAGE_SLEEP_TIME);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public void init() {

		txtArea = new TextArea("", 5, 100, TextArea.SCROLLBARS_NONE);
		txtArea.setEditable(false);
		txtArea.setFont(new Font("TimesRoman", Font.LAYOUT_LEFT_TO_RIGHT, 16));
	}

	public boolean ReadNewQuote() {
		if (count == 0) {
			folderToRead = RandomFolder.CalcRandomFolder("tipFolders/");
			folderToRead = "tipFolders/"+folderToRead;
			StringBuffer strBuff = ReadFile.ReadFileFromPath(folderToRead + "/" + "files.txt");
			if (strBuff == null) {
				return false;
			}
			String[] fileContent = strBuff.toString().split("\\^");
			fileList = fileContent[0];
			weights = fileContent[1].split(",");
			int randomFileIndex = RandomFile.calcRandomFileIndex(weights,
					fileList);
			String[] files = fileList.split(",");
			fileToRead = files[randomFileIndex];
			fileToRead = folderToRead+"/"+fileToRead;
			String prHtml = this.getParameter("fileToRead");
			if (prHtml != null)
				fileToRead = new String(prHtml);

			if (fileToRead.endsWith(".txt")) {
				isTextFile = true;
				strBuff = ReadFile.ReadFileContent(fileToRead);
				if (strBuff == null) {
					return false;
				}

				String context = strBuff.toString();

				quotes = context.split("\\^");

			} else {
				isTextFile = false;
				image = getImage(getDocumentBase(), fileToRead);
				count = 0;
			}
		}
		if (isTextFile) {
			quote.delete(0, quote.length());

			String tip = ReadTip.nextTip(quotes);

			while (tip.length() > optTipLength) {
				tip = ReadTip.nextTip(quotes);
			}

			quote.append(tip);
			quote.append(optTipLength);
			count++;
		}
		if (count > NUM_QUOTES_FROM_SAME_FILE) {
			count = 0;
		}
		return true;
	}

	public void paint(Graphics g) {

		if (!ReadNewQuote()) {
			return;
		}
		g.clearRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
		if (isTextFile) {
			add(txtArea);
			txtArea.setText(quote.toString());
		} else {
			remove(txtArea);
			g.drawImage(image,0,0,this);

		}

	}

}