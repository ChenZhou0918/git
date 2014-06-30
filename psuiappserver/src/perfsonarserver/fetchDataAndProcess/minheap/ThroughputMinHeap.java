package perfsonarserver.fetchDataAndProcess.minheap;

import perfsonarserver.fetchDataAndProcess.transferObjects.ThroughputData;

public class ThroughputMinHeap {

	

	/**
	 * min heap for throughput data
	 * 
	 * @author Zhou Chen
	 * 
	 */
	

		protected ThroughputData[] data;  
	       
	    public ThroughputMinHeap( ThroughputData[] data,boolean empty)  
	    {  
	    	
	    	   if(empty)
	       	{
	       	for(int i=0;i<=data.length-1;i++)
	       	  {
	       		data[i]=new ThroughputData();
	               data[i].setValue(0);
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
	          
	        if (l < data.length && data[l].getValue()< data[i].getValue()) 
	            smallest = l;    
	           
	        if (r < data.length && data[r].getValue() < data[smallest].getValue()) 
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
	        ThroughputData tmp = data[i];    
	        data[i] = data[j];    
	        data[j] = tmp;    
	    }  
	 
	    public ThroughputData getRoot()  
	    {  
	            return data[0];  
	    }  
	 
	    public void setRoot(ThroughputData root)  
	    {  
	        data[0] = root;  
	        heapify(0);  
	        buildHeap();
	    }  
	}  


