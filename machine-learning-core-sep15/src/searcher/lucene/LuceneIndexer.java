package searcher.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

public class LuceneIndexer 
{
	public String INDEX_DIR;
	public IndexWriter writer;
	
	public LuceneIndexer() {
		this.INDEX_DIR = "/Users/phamdinhthang/Desktop/Lucene_Index";
	}
	public LuceneIndexer(String index_dir) {
		this.INDEX_DIR = index_dir;
	}
	
	public void clearIndex() throws Exception {
		this.getWriter();
		this.writer.deleteAll();
		this.close();
	}
	
	public void indexDocs(List<Document> docs) throws Exception {
		System.out.println("Indexing...");
		this.getWriter();
		this.writer.addDocuments(docs);
		this.close();
	}
	public void indexDoc(Document doc) throws Exception {
		this.getWriter();
		this.writer.addDocument(doc);
	}

	public<T extends LuceneDocument> void index(List<T> docs) throws Exception {
		System.out.println("Indexing...");
		this.getWriter();
		
		List<Document> documents = new ArrayList<>();
		for (T obj:docs) documents.add(obj.toDocument());
		
		this.writer.addDocuments(documents);
		this.close();
	}
	public<T extends LuceneDocument> void index(T doc) throws Exception {
		this.getWriter();

		this.writer.addDocument(doc.toDocument());
		this.close();
	}

	public void close() throws Exception {
		if (this.writer != null) {
			this.writer.commit();
			this.writer.close();
		}
	}
	private void getWriter() throws IOException 
	{
		if (this.writer != null && this.writer.isOpen()) return;
		FSDirectory dir = FSDirectory.open(Paths.get(this.INDEX_DIR));
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		this.writer = new IndexWriter(dir, config);
		return;
	}
}
