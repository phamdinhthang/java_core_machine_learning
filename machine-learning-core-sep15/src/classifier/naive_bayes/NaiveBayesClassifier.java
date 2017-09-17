package classifier.naive_bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import data.util.JaccardIndex;


public class NaiveBayesClassifier {
	public List<NaiveBayesData> train_data;
	public Set<Integer> train_labels;
	public Map<Integer, Double> p_label;
	public NaiveBayesClassifier(List<NaiveBayesData> train_data) {
		this.train_data = train_data;
		train_labels = new TreeSet<Integer>();
		for (NaiveBayesData ik:train_data) train_labels.add(ik.label.hashCode());
		this.p_label = new HashMap<>();
		for (Integer label:this.train_labels) this.p_label.put(label, p_label(label));
	}
	private double p_label(Integer label) {
		int cnt=0;
		for (NaiveBayesData ik:train_data) if (ik.label.hashCode() == label.hashCode()) cnt++;
		return (double)cnt/(double)train_data.size();
	}

	public Integer classify(NaiveBayesData test) {
		double max_prob = 0d;
		Integer res = Integer.MAX_VALUE;
		System.out.println("===== " + test + " =====");
		for (Integer label:train_labels) {
			double prob_label_data = p_label_data(test,label);
			System.out.println("P(label = " + label + ") = " + prob_label_data);
			if (prob_label_data > max_prob) {
				max_prob = prob_label_data; 
				res = label;
			}
		}
		System.out.println("Predicted label = " + res);
		return res;
	}

	private double p_label_data(NaiveBayesData test, Integer label) {
		double p_label = this.p_label.get(label);
		double p_data_label = 1;
		List<NaiveBayesData> filtered = filterByLabel(train_data, label);
		for (int i=0;i<test.features.size();i++) {
			List<Object> train_colum = new ArrayList<Object>();
			for (NaiveBayesData ik:filtered) train_colum.add(ik.features.get(i));
			p_data_label = p_data_label * get_p_element(test.features.get(i),train_colum);
		}
		return p_data_label*p_label;
	}

	public static double get_p_element(Object k, List<Object> ls) {
		Double dk = null;
		List<Double> dls = null;
		if (k instanceof Double ) {
			dk = (Double) k;
			dls = new ArrayList<Double>();
			for (Object obj:ls) dls.add((Double)obj);
			return get_p_double(dk, dls);
		}
		if (k instanceof Integer) {
			dk = new Double(((Integer)k).intValue());
			dls = new ArrayList<Double>();
			for (Object obj:ls) dls.add(new Double(((Integer)obj).intValue()));
			return get_p_double(dk, dls);
		}
		String sk = null;
		List<String> sls = null;
		if (k instanceof String) {
			sk = (String) k;
			sls = new ArrayList<String>();
			for (Object obj:ls) sls.add((String)obj);
			return get_p_string(sk, sls);
		}
		return 0.0;
	}
	
	public static double get_p_string(String k, List<String> ls) {
		StringBuilder s = new StringBuilder();
		for (String lk:ls) s.append(lk + " ");
		return JaccardIndex.compareWordHistogram(JaccardIndex.toBagOfWords(k), JaccardIndex.toBagOfWords(s.toString().trim()));
	}
	
	public static double get_p_double(Double k, List<Double> ls) {
		double mean = mean(ls),variance = variance(ls);
		double p_double = Math.pow(Math.E, -((k-mean)*(k-mean))/(2*variance)) * 1/(Math.sqrt(2*Math.PI*variance));
		p_double = p_double > 1 ? 1:p_double;
//		System.out.println("Element = " + k + " Colums = " + ls);
//		System.out.println("P element = " + p_double); 
		return p_double;
	}
	public static List<NaiveBayesData> filterByLabel(List<NaiveBayesData> inp, int label) {
		List<NaiveBayesData> filtered = new ArrayList<NaiveBayesData>();
		for (NaiveBayesData ik:inp) if (ik.label.hashCode() == label) filtered.add(ik);
		return filtered;
	}
	public static double mean(List<Double> ls) {
		if (ls.size() == 0) return 0d;
		double mean = 0d;
		for (Double num:ls) mean += num.doubleValue();
		return mean/ls.size();
	}
	public static double variance(List<Double> ls) {
		if (ls.size() == 0) return 0d;
		double mean = mean(ls);
		double variance = 0d;
		for (Double num:ls) variance += (num.doubleValue()-mean)*(num.doubleValue()-mean);
		return variance == 0 ? 1 : variance/ls.size();
	}
}
