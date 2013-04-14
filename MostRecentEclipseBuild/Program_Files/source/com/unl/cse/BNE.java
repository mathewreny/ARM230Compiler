package com.unl.cse;


public class BNE extends Instruction{


	public BNE(String name) {
		super.name = name;
	}

	public String toS230() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toBinaryInstruction(){
		String instructionString = "00000001";
		instructionString = instructionString.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(0)),16));
		return instructionString.trim();		
	}
	
}
