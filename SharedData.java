package Mechanics;

import java.util.concurrent.atomic.AtomicInteger;

public class SharedData {

	private DataLocker[] dataLockers;
	AtomicInteger sumofallsum = new AtomicInteger();
	AtomicInteger countallnums = new AtomicInteger();

	public SharedData(int size) {
		dataLockers = new DataLocker[size];
		for(int i = 0; i < dataLockers.length; i++){
			dataLockers[i] = new DataLocker();
		}
	}
	
	public void addToLocker(int index, int value) throws IllegalArgumentException{
		
		if(index < 0 || index >= dataLockers.length){
			throw new IllegalArgumentException("Locker index out of bounds");
		}
		dataLockers[index].add(value);
		sumofallsum.addAndGet(value);
		countallnums.incrementAndGet();
		System.out.println("SharedData recieved value: "+value);
		
	}
	
	public AtomicInteger sumAll(){
		return sumofallsum;
	}
	
	public int getMax(){

		int j=0;
		int max=-99999999;
		for(int i = 0; i < dataLockers.length; i++){
			if((dataLockers[i].getSum()) > max){
				max=dataLockers[i].getSum();
				j=i;
			}
		}
		return j+1;
		
	}
	
	public AtomicInteger countAll(){
		return countallnums;
	}

	public DataLocker getDataLocker(int i){
		return dataLockers[i];
	}
	
}
