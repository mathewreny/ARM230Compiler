package edu.unl.cse.instructions;


public class ADD extends Instruction{


	public ADD(String name) {
		super.name = name;
	}


	public String toS230() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toBinaryInstruction(){
		return super.toBinaryALUInstructionRType();
	}
	
}
