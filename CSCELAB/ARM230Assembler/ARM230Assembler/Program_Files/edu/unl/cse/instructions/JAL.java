package edu.unl.cse.instructions;

import edu.unl.cse.Compiler;


public class JAL extends Instruction{


	public JAL(String name) {
		super.name = name;
	}


	public String toS230() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toBinaryInstruction(){
		String instructionString = "0010";
		instructionString = instructionString.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(0)),20));
		return instructionString.trim();
	}
	
}
