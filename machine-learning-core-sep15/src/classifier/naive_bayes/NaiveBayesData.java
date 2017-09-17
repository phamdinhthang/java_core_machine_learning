package classifier.naive_bayes;

import java.util.ArrayList;
import java.util.List;

public class NaiveBayesData {
	public List<Object> features;
	public Object label;
	
	public NaiveBayesData(Object... objs) {
		super();
		this.features = new ArrayList<Object>();
		for(Object obj: objs) features.add(obj);
	}
	
	public NaiveBayesData(List<Object> _features, Object _label) {
		super();
		this.features = _features;
		this.label = _label;
	}
	
	public NaiveBayesData() {
		this.features = new ArrayList<Object>();
	}

	public String toString() {
		StringBuilder bld = new StringBuilder();
		for (int i = 0;i<features.size();i++) {
			bld.append(features.get(i).toString());
			if (i<features.size()-1) bld.append(" - ");
		}
		return "NB_Data [features= " + bld.toString() + ", label=" + label + "]";
	}

	public int length() {
		return features.size();
	}
	
	public Object get(int k) {
		return features.get(k);
	}
}
