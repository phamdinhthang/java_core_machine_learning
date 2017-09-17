package classifier.kNN;

import java.util.ArrayList;
import java.util.List;


public class Iris implements classifier.kNN.kNNData {
	public float sepal_length;
	public float sepal_width;
	public float petal_length;
	public float petal_width;
	public String species;
	public Iris(String[] data) {
		if (data.length == 5) {
			this.sepal_length = Float.parseFloat(data[0]);
			this.sepal_width = Float.parseFloat(data[1]);
			this.petal_length = Float.parseFloat(data[2]);
			this.petal_width = Float.parseFloat(data[3]);
			this.species = data[4];
		}
	}
	@Override
	public List<? extends Object> get_features() {
		List<Double> features = new ArrayList<>();
		features.add(new Double(this.sepal_length));
		features.add(new Double(this.sepal_width));
		features.add(new Double(this.petal_length));
		features.add(new Double(this.petal_width));
		return features;
	}
	@Override
	public Object get_label() {
		return this.species;
	}
	@Override
	public <T extends kNNData> double distance_to(T a) {
		List<? extends Object> features1 = a.get_features();
		List<? extends Object> features2 = this.get_features();
		if (features1.size() != features2.size()) return Double.MAX_VALUE;
		double distance = 0;
		for (int i=0;i<features1.size();i++) {
			if (!(features1.get(i) instanceof Double) || !(features2.get(i) instanceof Double)) return Double.MAX_VALUE;
			Double a1 = (Double)features1.get(i);
			Double a2 = (Double)features2.get(i);
			distance += (a1.doubleValue()-a2.doubleValue())*(a1.doubleValue()-a2.doubleValue());
		}
		
		return Math.sqrt(distance);
	}
}
