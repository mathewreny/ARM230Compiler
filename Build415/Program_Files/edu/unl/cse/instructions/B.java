package edu.unl.cse.instructions;

import edu.unl.cse.Compiler;


public class B extends Instruction{

	public B(String name) {
		super.name = name;
	}


	public String toS230() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toBinaryInstruction(){
		String instructionString = "0000"+super.condition;
		instructionString = instructionString.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(0)),16));
		return instructionString.trim();
	}
	
}
