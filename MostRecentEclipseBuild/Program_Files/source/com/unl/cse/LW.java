package com.unl.cse;


public class LW extends Instruction{


	public LW(String name) {
		super.name = name;
	}


	
	public String toS230() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toBinaryInstruction(){
		String instructionString = "10001110";
		instructionString = instructionString.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(1).substring(1)),4));
		instructionString = instructionString.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(0).substring(1)),4));
		instructionString = instructionString.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(2)),7));
		instructionString = instructionString.concat("0");
		return instructionString.trim();
	}
	

}
