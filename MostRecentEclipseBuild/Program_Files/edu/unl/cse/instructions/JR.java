package edu.unl.cse.instructions;

import edu.unl.cse.Compiler;


public class JR extends Instruction{


	public JR(String name) {
		super.name = name;
	}


	public String toS230() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toBinaryInstruction(){
		String instructionString = "11111110";
		instructionString = instructionString.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(0).substring(1)),4));
		instructionString = instructionString.concat("000000000000");
		return instructionString.trim();
	}
	
	
}
