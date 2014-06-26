package perfsonarserver.fetchData.minheap;

import perfsonarserver.fetchData.transferObjects.DelayJitterLossData;
import perfsonarserver.fetchData.transferObjects.UtilizationData;

/**
 * min heap for delay data
 * 
 * @author Zhou Chen
 * 
 */
public class DelayMinHeap {


   
	protected DelayJitterLossData[] data;  
       
    public DelayMinHeap( DelayJitterLossData[] data,boolean empty)  
    {  
    	
    	   if(empty)
       	{
       	for(int i=0;i<=data.length-1;i++)
       	  {
       		data[i]=new DelayJitterLossData();
               data[i].setMaxDelay(0);
       	  }
       	}
    	
        this.data = data;  
        buildHeap();  
    }  
       


    protected void buildHeap()  
    {  
        for (int i = (data.length) / 2 - 1; i >= 0; i--)   
        {  	          
            heapify(i);  
        }  
    }  
      
	protected void heapify(int i)  // for each layer, swap only once
    {   
        int l = left(i);    
        int r = right(i);  
           
        int smallest = i;  
          
        if (l < data.length && data[l].getMaxDelayPercentage()< data[i].getMaxDelayPercentage()) 
            smallest = l;    
           
        if (r < data.length && data[r].getMaxDelayPercentage() < data[smallest].getMaxDelayPercentage()) 
            smallest = r;    
           
        if (i == smallest)    
            return;    
 
        swap(i, smallest);  
     
        heapify(smallest);  
    }  
    
    protected int right(int i)  
    {    
        return (i + 1) << 1;    
    }     
   
    protected int left(int i)   
    {    
        return ((i + 1) << 1) - 1;    
    }  
 
    protected void swap(int i, int j)   
    {    
        DelayJitterLossData tmp = data[i];    
        data[i] = data[j];    
        data[j] = tmp;    
    }  
 
    public DelayJitterLossData getRoot()  
    {  
            return data[0];  
    }  
 
    public void setRoot(DelayJitterLossData root)  
    {  
        data[0] = root;  
        heapify(0);  
        buildHeap();
    }  
}  


