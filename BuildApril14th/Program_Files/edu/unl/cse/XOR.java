package edu.unl.cse;


public class XOR extends Instruction{


	public XOR(String name) {
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
