package perfsonarserver.fetchData.minheap;

import perfsonarserver.fetchData.transferObjects.DelayJitterLossData;
import perfsonarserver.fetchData.transferObjects.UtilizationData;
/**
 * min heap for utilization data
 * 
 * @author Zhou Chen
 * 
 */
public class UtilMinHeap  
{  
   
   private UtilizationData[] data;  
   /***constructor***/
    public UtilMinHeap(UtilizationData[] data, boolean empty)  
    {  
        this.data = data;  
        
        if(empty)
    	{
    	for(int i=0;i<=data.length-1;i++)
    	  {
    		data[i]=new UtilizationData();
            data[i].setUtilPercentage(0);
    	  }
    	}
        buildHeap();  
    }  
    
   /**sort the whole heap**/	private void buildHeap()  
    {  
        for (int i = (data.length) / 2 - 1; i >= 0; i--)   
        {  	          
            heapify(i);  
        }  
    }  
	/***heapify:adjust the position of a single node***/
    private void heapify(int i)  
    {   
        int l = left(i);    
        int r = right(i);  
           
        int smallest = i;  
          
        if (l < data.length &&data[l].getUtilPercentage()< data[i].getUtilPercentage()) 
            smallest = l;    
           
        if (r < data.length && data[r].getUtilPercentage() <data[smallest].getUtilPercentage())    
            smallest = r;    
           
        if (i == smallest)    
            return;    
 
        swap(i, smallest);  
     
        heapify(smallest);  
    }  
    
    private int right(int i)  
    {    
        return (i + 1) << 1;    
    }     
   
    private int left(int i)   
    {    
        return ((i + 1) << 1) - 1;    
    }  
 
    private void swap(int i, int j)   
    {    
        UtilizationData tmp = data[i];    
        data[i] = data[j];    
        data[j] = tmp;    
    }  
 
    public UtilizationData getRoot()  
    {  
            return data[0];  
    }  
    /***reset the root as new utilization value***/
    public void setRoot(UtilizationData root)  
    {  
        data[0] = root;  
        heapify(0);  
        buildHeap();
    }  
}  