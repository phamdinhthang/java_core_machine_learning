package cluster.kmeans;

import java.util.Arrays;


public class KmeansCentroid {
	double[] data;
	int count = 0;
	
	public void clear() {
		data = null;
		count = 0;
	}

	public void add(KmeansPoint pt) {
		if(data == null) data = Arrays.copyOf(pt.feature, pt.feature.length);
		else for(int k=0; k<data.length; k++) data[k] += pt.feature[k];
		count++;
	}

	public void average() {
		if(count > 0) for (int k=0; k<data.length; k++) data[k] /= count;
	}
	

	
	public String toString() {
		return "kmeans_Center [data=" + Arrays.toString(data) + ", count=" + count + "]";
	}
}
