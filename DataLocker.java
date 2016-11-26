package Mechanics;

import java.util.concurrent.atomic.AtomicInteger;

public class DataLocker {
	
	AtomicInteger ATcount ;
	AtomicInteger ATsum ;
	int count;
	int sum;
	
	
	public DataLocker(){
		ATcount = new AtomicInteger(0);
		ATsum = new AtomicInteger(0);
		count = 0;
		sum = 0;
	}
	
	public void add(int value){
		count = ATcount.incrementAndGet();
		sum = ATsum.addAndGet(value);
	}
	
	public int getCount(){
		return count;
	}
	public int getSum(){
		return sum;
	}

}
