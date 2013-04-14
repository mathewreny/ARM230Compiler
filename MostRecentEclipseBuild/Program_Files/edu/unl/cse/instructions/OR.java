package edu.unl.cse.instructions;


public class OR extends Instruction{


	public OR(String name) {
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
