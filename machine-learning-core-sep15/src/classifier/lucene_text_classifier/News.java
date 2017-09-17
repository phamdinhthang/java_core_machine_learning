package classifier.lucene_text_classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class News implements LuceneTextDocument{
	public String file_id;
	public String label;
	public String message_id;
	public String path;
	public String from;
	public String subject;
	public String contents;
	
	public News(File f,String lbl) {
		this.label = lbl;
		this.file_id = f.getName();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line = null;
			StringBuilder header = new StringBuilder();
			StringBuilder contents = new StringBuilder();
			boolean isHeader = true;
			while ((line = reader.readLine()) != null) {
				if (isHeader) header.append(line + System.getProperty("line.separator"));
				else contents.append(line + System.getProperty("line.separator"));
				if (line.length() == 0) isHeader = false;
			}
			this.contents = contents.toString().trim();
			
			String[] headers = header.toString().split(System.getProperty("line.separator"));
			
			Map<String, String> fields = new HashMap<String,String>();
			for (String lk:headers) 
				if (lk.indexOf(":") > 0) 
					fields.put(lk.substring(0, lk.indexOf(":")).toLowerCase(), lk.substring(lk.indexOf(":")+1).toLowerCase());
				
			
			this.message_id = fields.get("Message-ID".toLowerCase());
			this.path = fields.get("Path".toLowerCase());
			this.from = fields.get("From".toLowerCase());
			this.subject = fields.get("Subject".toLowerCase());
			
			reader.close();
		} catch (Exception e) { e.printStackTrace();}
	}

	public String toString() {
		return "File = " + file_id + ", label = " + label + "\nmessage_id = "
				+ message_id + "\npath = " + path + "\nfrom = " + from
				+ "\nsubject = " + subject + "\ncontents = " + contents + "]";
	}

	
	public static List<News> scanNews(File news_group) {
		System.out.println("Start fetch news...");
		List<News> res = new ArrayList<>();
		for (File folder:news_group.listFiles())
			if (folder.isDirectory()) {
				for (File ik:folder.listFiles()) {
//					System.out.println("File: " + ik.getName() + ", label = " + folder.getName());
					res.add(new News(ik, folder.getName()));
				}
			}
		System.out.println("Fetch done.");
		return res;
	}

	@Override
	public Object getLabel() {
		return this.label;
	}
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.message_id;
	}

	@Override
	public String getContents() {
		// TODO Auto-generated method stub
		return this.contents;
	}

	@Override
	public Map<String, String> getMetaData() {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("path", this.path);
		metadata.put("from", this.from);
		metadata.put("subject", this.subject);
		return metadata;
	}
}
