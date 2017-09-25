package classifier.kNN;

import java.util.ArrayList;
import java.util.List;

import data.util.CSVUtil;
import data.util.FileDownloader;


public class __test_kNN_classifier {

	public static void main(String[] args) throws Exception{
		String src = "https://www.dropbox.com/s/23q56pn8mwpgmuz/iris.csv?dl=1";
		List<String[]> iris_data = CSVUtil.readCSV(FileDownloader.fileFromUrl(src));
		iris_data.remove(0);//Skip header
		List<Iris> iris = new ArrayList<>();
		
		for (String[] line:iris_data) iris.add(new Iris(line));
		
		kNNClassifier classifier = new kNNClassifier();
		classifier.train(iris);
		
		int correct=0;
		for (Iris i:iris) {
			Object label = classifier.classify(i, 5);
			if (label.equals(i.species)) correct++;
		}
		System.out.println("Accuracy = " + (double)correct/(double)iris.size());
	}
}
