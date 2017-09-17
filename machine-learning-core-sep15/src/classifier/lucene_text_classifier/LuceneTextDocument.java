package classifier.lucene_text_classifier;

import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexOptions;

public interface LuceneTextDocument {
	public String getId();
	public String getContents();
	//May return empty or null
	public Map<String, String> getMetaData();
		
	public Object getLabel();
	
	public default Document toLuceneDocument() {
		String iD = getId();
		String contents = getContents();
		Object label = getLabel();
		Map<String,String> metaData = getMetaData();
		
		FieldType type = new FieldType();
	    type.setIndexOptions(IndexOptions.DOCS);
	    type.setStored(true);
	    type.setStoreTermVectors(true);
		
		Document d = new Document();
		if (iD != null) d.add(new StringField("id", iD, Field.Store.YES));
		d.add(new Field("contents", contents, type));
		d.add(new Field("label", label.toString(), type));
		if (metaData != null) 
			for (String key:metaData.keySet()) d.add(new Field(key, metaData.get(key), type));
		return d;
	}
}
