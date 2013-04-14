package edu.unl.cse.instructions;

import edu.unl.cse.Compiler;


public class CMP extends Instruction{

	public CMP(String name) {
		super.name = name;
	}


	public String toS230() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toBinaryInstruction(){
		String instructionString = "11101110";
		instructionString = instructionString.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(0).substring(1)),4));
		instructionString = instructionString.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(1).substring(1)),4));
		instructionString = instructionString.concat("00000001");
		return instructionString.trim();
	}
	
}
