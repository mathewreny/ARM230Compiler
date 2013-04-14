package edu.unl.cse;


public class AND extends Instruction{

	public AND(String name) {
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
