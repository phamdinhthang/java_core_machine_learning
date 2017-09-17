package searcher.lucene;

import org.apache.lucene.document.Document;

public interface LuceneDocument {
	public Document toDocument();
}
