package edu.unl.cse.instructions;

import edu.unl.cse.Compiler;

public class SI extends Instruction{
	
	public SI(String name) {
		super.name = name;
	}


	public String toS230() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toBinaryInstruction(){
		String toReturn = "1101"+super.condition;
		toReturn = toReturn.concat(
			Compiler.returnBinaryNumber(
				Integer.parseInt(super.components.get(0).substring(1)), 4));
		toReturn = toReturn.concat("0000");
		toReturn = toReturn.concat(
				Compiler.returnBinaryNumber(
					Integer.parseInt(super.components.get(1).substring(1)), 7));
		toReturn = toReturn.concat("0");
		return toReturn;
	}
	
}
