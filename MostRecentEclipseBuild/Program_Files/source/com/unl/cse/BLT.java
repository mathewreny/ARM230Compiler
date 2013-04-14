package com.unl.cse;


public class BLT extends Instruction{

	public BLT(String name) {
		super.name = name;
	}


	public String toS230() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toBinaryInstruction(){
		String instructionString = "00001101";
		instructionString = instructionString.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(0)),16));
		return instructionString.trim();		
	}
	
}
