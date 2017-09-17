package classifier.lucene_text_classifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.document.Document;

import classifier.kNN.kNNClassifier;
import searcher.lucene.LuceneIndexer;
import searcher.lucene.LuceneSearcher;


public class LuceneTextClassifier {
	public String index_dir;
	List<? extends LuceneTextDocument> train_data;
	
	public LuceneTextClassifier() {
		this.index_dir = "/Users/phamdinhthang/Desktop/Lucene_Index";
	}
	public LuceneTextClassifier(String indexFolder) {
		this.index_dir = indexFolder;
	}
	
	public double crossValidation(int folds,boolean printDetails) throws Exception {
		if (folds < 1) folds = 1;
		int testSize = this.train_data.size()/folds;
		double avg_accuracy = 0.0;
		if (printDetails) System.out.println("Start cross validation. Number of folds: " + folds + ". Please wait...");
		for (int i=0;i<folds;i++) {
			double start = System.nanoTime();
			List<? extends LuceneTextDocument> test = this.train_data.subList(i*testSize, (i+1)*testSize);
			List<? extends LuceneTextDocument> train = new ArrayList<>(this.train_data);
			train.removeAll(test);
			
			double accuracy = this.validate(train, test);
			avg_accuracy += accuracy;
			double end = System.nanoTime();
			if (printDetails) {
				System.out.println("====== Fold " + (i+1) + ", train size = " + train.size() 
											+ ", test size = " + test.size()
											+ ", accuracy = " + accuracy
											+ ", time = " + (end-start)/1E9 + "(s)");
			}
		}
		avg_accuracy = avg_accuracy/folds;
		if (printDetails) System.out.println("Average accuracy = " + avg_accuracy);
		return avg_accuracy;
	}
	
	public double validate(List<? extends LuceneTextDocument> train, List<? extends LuceneTextDocument> test) throws Exception {
		if (test.size() < 1) return Double.MAX_VALUE;
		this.indexPartial(train);
		int correct = 0;
		for (LuceneTextDocument t:test) {
			Object label = this.classify(t);
			if (label.equals(t.getLabel())) correct++;
		}
		return (double)correct/(double)test.size();
	}
	private void indexPartial(List<? extends LuceneTextDocument> train) throws Exception {
		LuceneIndexer indexer = new LuceneIndexer();
		indexer.clearIndex();
		for (LuceneTextDocument t:this.train_data)
			indexer.indexDoc(t.toLuceneDocument());
		indexer.close();
	}
	public void train(List<? extends LuceneTextDocument> train) throws Exception {
		System.out.println("Indexing...");
		this.train_data = train;
		double start = System.nanoTime();
		LuceneIndexer indexer = new LuceneIndexer();
		indexer.clearIndex();
		for (LuceneTextDocument t:this.train_data)
			indexer.indexDoc(t.toLuceneDocument());
		indexer.close();
		double end = System.nanoTime();
		System.out.println("Index complete. Time = " + (end-start)/1E9 + "(s)");
	}
	
	public String classify(LuceneTextDocument test) throws Exception {
		if (this.train_data == null || this.train_data.size() == 0) {
			System.out.println("No train data. Please specified train progress first");
			return "";
		}
		
		List<Document> hits = LuceneSearcher.searchMoreLikeThis(10, test.toLuceneDocument(), "contents", "contents");
		if (hits.size() < 1) return "";
		
		Map<String, Integer> label_count = new TreeMap<>();
		for (Document d:hits) {
			Integer count = label_count.get(d.get("label"));
			if (count == null) count = 1;
			else count = new Integer(count.intValue()+1);
			label_count.put(d.get("label"), count);
		}
		return (String) kNNClassifier.getMaxLabel(label_count);
	}
}
