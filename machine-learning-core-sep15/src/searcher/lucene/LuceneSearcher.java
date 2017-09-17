package searcher.lucene;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.search.similarities.Similarity.SimScorer;
import org.apache.lucene.search.similarities.Similarity.SimWeight;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;


public class LuceneSearcher 
{
	private static final String INDEX_DIR = "/Users/phamdinhthang/Desktop/Lucene_Index";
	
	public static Query createTextQuery(String q,String field) throws Exception {
		QueryParser qParser = new QueryParser(field, new StandardAnalyzer());
		return qParser.parse(q);
	}

	public static List<Document> search(Query q,int limit) throws Exception {
		IndexSearcher searcher = createSearcher();
		TopDocs foundDocs = searcher.search(q, limit);
		List<Document> res = new ArrayList<>();
		for (ScoreDoc sd : foundDocs.scoreDocs) res.add(searcher.doc(sd.doc));
		return res;
	}
	
	public static List<Document> searchMultipleTextField(String query,int limit,String...fields) throws Exception {
		IndexSearcher searcher = createSearcher();
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
		Query q = queryParser.parse(query);
		TopDocs foundDocs = searcher.search(q,limit);
		List<Document> res = new ArrayList<>();
		for (ScoreDoc sd : foundDocs.scoreDocs) res.add(searcher.doc(sd.doc));
		return res;
	}
	public static List<Document> searchMoreLikeThis(int kpar,Document doc,String fieldToQuery, String...allfields) throws Exception {
		MoreLikeThis mlt = new MoreLikeThis(createReader());
		IndexSearcher searcher = createSearcher();
	
		mlt.setMinTermFreq(0);
	    mlt.setMinDocFreq(0);
	    mlt.setAnalyzer(new StandardAnalyzer());
		mlt.setFieldNames(allfields); 

		
		Reader sReader = new StringReader(doc.get(fieldToQuery));
	    Query query = mlt.like(fieldToQuery, sReader);
	     
	    TopDocs foundDocs = searcher.search(query,kpar);
	    List<Document> res = new ArrayList<>();
		for (ScoreDoc sd : foundDocs.scoreDocs) res.add(searcher.doc(sd.doc));
		return res;
	}
	public static List<Document> searchMoreLikeThisTFIDF(Document doc, String...fields) {
		return null;
	}
	
	private static IndexSearcher createSearcher() throws IOException {
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
	private static IndexReader createReader() throws Exception {
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		return DirectoryReader.open(dir);
	}
		
}
