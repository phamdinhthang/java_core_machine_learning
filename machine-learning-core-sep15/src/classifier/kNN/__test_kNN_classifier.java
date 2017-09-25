package classifier.kNN;

import java.util.ArrayList;
import java.util.List;

import data.util.CSVUtil;


public class __test_kNN_classifier {

	public static void main(String[] args) throws Exception{
		String filePath = "/Users/phamdinhthang/Desktop/Datasets/Iris/iris.csv";
		List<String[]> iris_data = CSVUtil.readCSV(filePath);
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
