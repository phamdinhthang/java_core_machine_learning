package data.util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

public class FileDownloader {

	public static void saveFileFromUrl(String src, String savetopath) throws Exception {
		URL url = new URL(src);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		FileOutputStream fos = new FileOutputStream(savetopath);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		return;
	}
	public static File fileFromUrl(String src) throws Exception{
		File desktop = new File(System.getProperty("user.home"), "Desktop");
		URL url = new URL(src);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		File outputFile = new File(desktop.getAbsolutePath() + File.separator + "downloaded");
		FileOutputStream fos = new FileOutputStream(outputFile);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		return outputFile;
	}
	
	public static void main(String[] args) throws Exception {
		String src = "https://www.dropbox.com/s/23q56pn8mwpgmuz/iris.csv?dl=1";
		FileDownloader.saveFileFromUrl(src, "/Users/phamdinhthang/Desktop/iris.csv");
		
		File file = FileDownloader.fileFromUrl(src);
		List<String[]> iris = CSVUtil.readCSV(file);
		for (String[] line:iris) {
			for (String data:line) System.out.print(data+";");
			System.out.println();
		}
	}

}
