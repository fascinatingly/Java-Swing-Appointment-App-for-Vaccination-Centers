package com.DiamondRose;

// Interface implemented by classes that can be stringified for saving
// to file
public interface Savable{

	static Savable fromString(String string){
		throw new IllegalStateException("Cannot save class to file");
	}

	String toString();
}
