package perfsonarserver.fetchDataAndProcess;

import java.util.LinkedList;

import perfsonarserver.fetchDataAndProcess.transferObjects.DelayJitterLossData;
/**
 * this is a cluster used in clustering algorithm
 * 
 * @author Zhou Chen
 * 
 * @param data
 * a list storing delay data
 * @param refer_point
 * first value of cluster
 * @param intrinsicDelay_point
 * the point with intrinsic delay
 *           
 * 
 */
public class Cluster {

	private LinkedList<DelayJitterLossData> data;
	
	private DelayJitterLossData refer_point;
	
	private DelayJitterLossData intrinsicDelay_point;
	
	public Cluster(DelayJitterLossData refer_point)
	{
		
		this.refer_point=refer_point;
		data=new LinkedList<DelayJitterLossData>();
	}
	
	
	public int getSize()
	{
		return data.size();
	}
	

	
	public void SetReferPoint(DelayJitterLossData refer_point)
	
	{
		this.refer_point=refer_point;
	}	
	
	
	public DelayJitterLossData getReferPoint()
	{
		return refer_point;
	}
	
	public LinkedList<DelayJitterLossData>  getData()
	{
		return data;
	}
	
	
	public void merge(DelayJitterLossData point)
	{
		data.add(point);
	}
	
	public void merge(Cluster cluster )
	{
		for(DelayJitterLossData nextData: cluster.getData())
		data.add(nextData);
	}
	
	public void remove()
	{
		data.remove();
	}
	
	public DelayJitterLossData get(int index)
	{
		return data.get(index);
	}
	
	public  double getIntrinsicDelay()
	{
		intrinsicDelay_point=data.get(0);
		
		for(DelayJitterLossData currentPoint:data)
		{
			if(currentPoint.getMinDelay()<intrinsicDelay_point.getMinDelay())
			{this.SetReferPoint(currentPoint);}
		}
		return intrinsicDelay_point.getMinDelay();
	}	
	
}
