package cluster.kmeans;

import java.util.ArrayList;
import java.util.List;


public class __test_kmeans_cluster {
	public static void main(String[] args) 
	{
		List<KmeansPoint> points = new ArrayList<KmeansPoint>();
		
		points.add(new KmeansPoint(12, 0, 0));
		points.add(new KmeansPoint(13, 0, 0));
		points.add(new KmeansPoint(14, 0, 0));
		points.add(new KmeansPoint(10, 0, 0));
		points.add(new KmeansPoint(11, 0, 0));
		points.add(new KmeansPoint(12, 0, 0));
		points.add(new KmeansPoint(13, 0, 0));
		points.add(new KmeansPoint(14, 0, 0));
		points.add(new KmeansPoint(0, 202, 0));
		points.add(new KmeansPoint(0, 203, 0));
		points.add(new KmeansPoint(0, 204, 0));
		points.add(new KmeansPoint(0, 205, 0));
		points.add(new KmeansPoint(0, 206, 0));
		points.add(new KmeansPoint(0, 204, 0));
		points.add(new KmeansPoint(0, 205, 0));
		points.add(new KmeansPoint(0, 206, 0));
		points.add(new KmeansPoint(0, 0, 101));
		points.add(new KmeansPoint(0, 0, 102));
		points.add(new KmeansPoint(0, 0, 103));
		points.add(new KmeansPoint(0, 0, 104));
		points.add(new KmeansPoint(0, 0, 105));
		points.add(new KmeansPoint(0, 0, 102));
		points.add(new KmeansPoint(0, 0, 103));
		points.add(new KmeansPoint(0, 0, 104));
		
		//If there is a group with huge distance to all remaining groups, kmeans may converge to 2 groups.
		//for this case, either it is the natural of the data (only 2 groups) or the values shall be normalized
		points.add(new KmeansPoint(0, 0, 30500));
		points.add(new KmeansPoint(0, 0, 30500));
		points.add(new KmeansPoint(0, 0, 30500));
		points.add(new KmeansPoint(0, 0, 30500));
		points.add(new KmeansPoint(0, 0, 30500));
		
		KmeansCluster eng = new KmeansCluster();
		eng.kmeans(points, 4, 20);
		eng.dumpCenters();
		eng.dumpLabels();
	}
}
