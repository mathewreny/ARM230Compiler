package com.unl.cse;


public class J extends Instruction{


	public J(String name) {
		super.name = name;
	}


	public String toS230() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toBinaryInstruction(){
		String instructionString = "0110";
		instructionString = instructionString.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(0)),20));
		return instructionString.trim();
	}
	
}
