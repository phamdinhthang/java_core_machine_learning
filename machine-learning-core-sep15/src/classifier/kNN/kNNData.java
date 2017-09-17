package classifier.kNN;

import java.util.List;

public interface kNNData {
	public List<? extends Object> get_features();
	public Object get_label();
	public<T extends kNNData> double distance_to(T a);
}
