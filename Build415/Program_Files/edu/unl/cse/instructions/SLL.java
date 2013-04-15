package edu.unl.cse.instructions;

public class SLL extends Instruction{
		public SLL(String name){
			super.name = name;
		}
		
		public String toBinaryInstruction(){
			return super.toBinaryALUInstructionRType();
		}
}
