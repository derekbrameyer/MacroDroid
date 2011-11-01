package com.doomonafireball.macrodroid;

public class Db4oIncrementedId {
	public long id;
	
	public Db4oIncrementedId() {
		this.id = 0;
	}
	
	public long getNextID() {
		return ++id;
	}
	
	public void resetID() {
		id = 0;
	}
}
