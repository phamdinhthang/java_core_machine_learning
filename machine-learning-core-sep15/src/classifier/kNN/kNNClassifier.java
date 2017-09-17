package classifier.kNN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class kNNClassifier {
	public List<? extends kNNData> trainData;
	public Set<Object> trainLabels;
	
	public void train(List<? extends kNNData> train) {
		this.trainData = train;
		this.trainLabels = new HashSet<Object>();
		for (kNNData pk:this.trainData) trainLabels.add(pk.get_label());
	}
	public<T extends kNNData> Object classify(T test, int kpar) {
		List<kNNData> k_nearest = new ArrayList<kNNData>();
		for (kNNData dk:this.trainData)
			if (k_nearest.size() < kpar) k_nearest.add(dk);
			else add_if_smaller(k_nearest,test,dk);
		return getMajorityLabel(k_nearest);
	}

	private Object getMajorityLabel(List<kNNData> k_nearest) {
		Map<Object, Integer> labels_map = new HashMap<Object,Integer>();
		for (kNNData dk:k_nearest) {
			Integer count = labels_map.get(dk.get_label());
			labels_map.put(dk.get_label(), count == null ? 1: new Integer(count.intValue()+1));
		}
		return getMaxLabel(labels_map);
	}
	public static Object getMaxLabel(Map<? extends Object, Integer> label_count) {
		int max_count = 0;
		Object max_label = null;
		for (Object label:label_count.keySet()) {
			if (label_count.get(label).intValue() > max_count) {
				max_count = label_count.get(label);
				max_label = label;
			}
		}
		return max_label;
	}

	private void add_if_smaller(List<kNNData> k_nearest, kNNData test, kNNData dk) {
		double max_d = 0.0d;
		for (kNNData kj:k_nearest) if (kj.distance_to(test) > max_d) max_d = kj.distance_to(test);
		if (dk.distance_to(test) < max_d) {
			removeMax(k_nearest,test);
			k_nearest.add(dk);
		}
	}

	private void removeMax(List<kNNData> k_nearest, kNNData test) {
		double max_d = 0.0d;
		kNNData max_obj = null;
		for (kNNData kj:k_nearest) 
			if (kj.distance_to(test) > max_d) {
				max_d = kj.distance_to(test);
				max_obj = kj;
			}
		k_nearest.remove(max_obj);
	}	
}
