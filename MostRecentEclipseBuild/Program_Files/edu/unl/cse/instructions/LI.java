package edu.unl.cse.instructions;

import edu.unl.cse.Compiler;


public class LI extends Instruction{


	public LI(String name) {
		super.name = name;
	}


	public String toS230() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toBinaryInstruction(){
		String toReturn = "0011";
		toReturn = toReturn.concat(
			Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(0).substring(1)), 4));
		toReturn = toReturn.concat(
				Compiler.returnBinaryNumber(
					Integer.parseInt(super.components.get(1).substring(1)), 16));
		return toReturn;
	}
	
}
