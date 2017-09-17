package data.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class JaccardIndex {
	public static double[] transformWithBasis(Object dk, List<Object> b) 
	{
		double[] res = new double[b.size()];
		for (int i=0;i<b.size();i++) res[i] = JaccardIndex.jaccardStringOrMap(dk, b.get(i));
		return res;
	}
	@SuppressWarnings("unchecked")
	public static double jaccardStringOrMap(Object v1, Object v2) 
	{
		if(v1 instanceof String) v1 = toBagOfWords((String)v1);
		if(v2 instanceof String) v2 = toBagOfWords((String)v2);
		
		return compareWordHistogram((Map<String, Double>)v1, (Map<String, Double>)v2);
	}
	

	public static Map<String, Double> toBagOfWords(List<? extends Object> lst) 
	{
		StringBuilder s = new StringBuilder();
		for (Object str:lst) s.append(str.toString() + " ");
		return toBagOfWords(s.toString().trim());
	}
	public static Map<String, Double> toBagOfWords(String text) 
	{
		Map<String, Double> res = new TreeMap<String, Double>();
		
		for(String word: text.split("\\W+"))
		{
			Double dk = res.get(word);
			res.put(word.toLowerCase(), dk==null ? 1 : dk+1);
		}
			
		return res;
	}
	

	public static double compareWordHistogram(Map<String, Double> hist1, Map<String, Double> hist2) 
	{
//		System.out.println(hist1);
//		System.out.println(hist2);
		
		Set<String> u = new TreeSet<>(hist1.keySet());
		u.addAll(hist2.keySet());
		
		double low = 0, hi = 0;
		
		for (String key: u) 
		{
			Double ak = hist1.get(key);
			if(ak == null) ak = 0d;
			
			Double bk = hist2.get(key);
			if(bk == null) bk = 0d;
			
			hi += Math.min(ak, bk);
			low += Math.max(ak, bk);
		}
		
		return hi/(low==0 ? 1 : low);
	}
	
	
	public static double jaccard_distance(double[] a, double[] b) 
	{
		double hi = 0, low = 0;
		for(int k=0; k<a.length; k++)
		{
			hi += Math.min(a[k], b[k]);			
			low += Math.max(a[k], b[k]);			
		}
		
		return 1 - hi/low;
	}

	public static double euclidean_distance(double[] a, double[] b) 
	{
		double s = 0;
		
		for(int k=0; k<a.length; k++) 
		{
			double dk = a[k] - b[k];
			s += dk * dk;
		}
		
		int n = a.length;
		return Math.sqrt(s / (n==0 ? 1 : n) );
	}
}
