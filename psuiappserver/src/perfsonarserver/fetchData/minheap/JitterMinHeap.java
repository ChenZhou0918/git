package perfsonarserver.fetchData.minheap;

import perfsonarserver.fetchData.transferObjects.DelayJitterLossData;

/**
 * min heap for jitter data
 * 
 * @author Zhou Chen
 * 
 */
public class JitterMinHeap extends DelayMinHeap{
	
	
	   public JitterMinHeap( DelayJitterLossData[] data,boolean empty)  
	    {  
	        super(data,empty);  
	    }  
	   
	   
	   protected void heapify(int i)  
	    {   
	        int l = this.left(i);    
	        int r = this.right(i);  
	           
	        int smallest = i;  
	          
	        if (l < data.length &&Math.abs( data[l].getMaxJitterPercentage())< Math.abs(data[i].getMaxJitterPercentage())  ) 
	            smallest = l;    
	           
	        if (r < data.length && Math.abs(data[r].getMaxJitterPercentage()) < Math.abs(data[smallest].getMaxJitterPercentage()))    
	            smallest = r;    
	           
	        if (i == smallest)    
	            return;    
	 
	        swap(i, smallest);  
	     
	        heapify(smallest);  
	    }  
	    
	       
	
	
	

}
