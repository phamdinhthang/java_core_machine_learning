package cluster.kmeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import data.util.JaccardIndex;



public class KmeansCluster {
	protected Random coin = new Random(197);

	private List<KmeansPoint> dataItems;
	private List<KmeansCentroid> dataCenters = new ArrayList<KmeansCentroid>();
	private int dataClusters;

	public void kmeans(List<KmeansPoint> items, int kpar, int times) {
		kmeans(items, kpar, times, new Random(197));
	}
	
	public void kmeans(List<KmeansPoint> items, int kpar, int times, Random c) {
		kmeans(items, kpar, times, c, true);
	}
	
	public void kmeans(List<KmeansPoint> items, int kpar, int times, Random c,Boolean center_first) 
	{
		coin = c;
		
		dataItems = items;
		dataClusters = kpar;
		
		if (center_first == true) {
			initCenters(items, kpar);
//			dumpCenters();
		} else {
			initLabels();
//			dumpLabels();
		}
		
		System.out.println("Start clustering");
		for(int tt=0; tt<times; tt++) {
			System.out.println("======= Iteration " + tt + "/" + times + " ========");
			if (center_first == true) {
				computeLabels();
//				dumpLabels();

				computeCenters();
//				dumpCenters();
			} else {
				computeCenters();
//				dumpCenters();
				
				computeLabels();
//				dumpLabels();
			}
		}
		return;
	}

	private void initCenters(List<KmeansPoint> items, int kpar) 
	{	
		for(int k=0; k<kpar; k++) 
		{
			KmeansCentroid ck = new KmeansCentroid();
			ck.count = 1;
			int rk = (int)Math.floor(coin.nextDouble()*items.size() );
			ck.data = items.get(rk).feature;
			dataCenters.add(ck);
		}
	}
	private void initLabels() 
	{
		for(KmeansPoint ik: dataItems)
		{
			int lk = (int)Math.floor(coin.nextDouble()*dataClusters);
			ik.label = lk;
		}
	}
	public void dumpCenters()
	{
		System.out.println("---- centers");
		for(KmeansCentroid center:dataCenters) System.out.println(center);
	}
	
	public void dumpLabels()
	{
		System.out.println("---- dataItem label");
		for(KmeansPoint ik:dataItems) 
		{
			System.out.print("Label: " + ik.label);
			if (ik.original != null) System.out.println(" - Original: " + ik.original);
			else ik.printFeatures();
		}		
	}
	

	private void computeCenters() 
	{
		for(KmeansCentroid ck: dataCenters) ck.clear();
		
		for(KmeansPoint ik: dataItems)
		{
			int k = ik.label;
			dataCenters.get(k).add(ik);
		}
		
		for(KmeansCentroid ck: dataCenters) ck.average();
	}	

	private void computeLabels() 
	{
		for(KmeansPoint ik: dataItems)
		{
			int lmin = -1;
			double dmin = Double.MAX_VALUE;

			//System.out.println(Arrays.toString(ik.feature));

			for(int k=0; k<dataCenters.size(); k++)
			{
				KmeansCentroid ck = dataCenters.get(k);
				double djk = distance(ck, ik);
				
				//System.out.println(djk + ":: " + Arrays.toString(ck.data));
				if(djk < dmin) { dmin = djk; lmin = k;  }
			}

			ik.label = lmin;
		}
	}
	
	protected double distance(KmeansCentroid cj, KmeansPoint ik) 
	{
		if(cj.count == 0) return Double.MAX_VALUE;
		
		double[] a = cj.data;
		double[] b = ik.feature;
		
		return JaccardIndex.euclidean_distance(a, b);
	}

	
}
