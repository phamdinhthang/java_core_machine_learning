package cluster.kmeans;

import java.util.ArrayList;
import java.util.List;

import classifier.kNN.Iris;
import data.util.CSVUtil;
import data.util.FileDownloader;

public class __test_kmeans_iris {
	public static void main(String[] args) throws Exception {
		String src = "https://www.dropbox.com/s/23q56pn8mwpgmuz/iris.csv?dl=1";
		List<String[]> iris_data = CSVUtil.readCSV(FileDownloader.fileFromUrl(src));
		iris_data.remove(0);
		
		List<Iris> iris = new ArrayList<>();
		for (String[] line:iris_data) iris.add(new Iris(line));
		
		List<KmeansPoint> points = new ArrayList<KmeansPoint>();
		for (Iris line:iris) {
			KmeansPoint pt = new KmeansPoint();
			double[] features = {line.sepal_length,line.sepal_width,line.petal_length,line.petal_width};
			pt.feature = features;
			points.add(pt);
		}
		KmeansCluster eng = new KmeansCluster();
		eng.kmeans(points, 3, 20);
		eng.dumpCenters();
		eng.dumpLabels();
	}
}
