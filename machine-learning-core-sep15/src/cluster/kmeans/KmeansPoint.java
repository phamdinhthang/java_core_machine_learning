package cluster.kmeans;

import java.util.Arrays;

public class KmeansPoint {
	public Integer label;
	public double[] feature;
	public Object original;
	
	public KmeansPoint() {}

	public KmeansPoint(double... args) {
		feature = args;
	}

	public void printFeatures() {
		System.out.print(" Features: ");
		for (int i=0;i<feature.length;i++) {
			System.out.print(feature[i]);
			if (i<feature.length-1) System.out.print(" - ");
			else System.out.println();
		}
	}

	public String toString() {
		return "kmeans_Point [label=" + label + ", feature=" + Arrays.toString(feature) + ", original=" + code(original)
				+ "]";
	}

	private String code(Object obj) {
		if (obj instanceof String && ((String) obj).length() < 100) return (String) obj;
		if (obj==null) return "0";
		else return Integer.toString(obj.hashCode());
	}
}
