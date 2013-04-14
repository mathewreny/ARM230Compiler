package edu.unl.cse;

import java.util.ArrayList;

import edu.unl.cse.instructions.Instruction;

public class Block {

	private ArrayList<Instruction> instructions = new ArrayList<Instruction>();
	private	String name;
	
	public Block(String name){
		this.name = name;
	}
	
	public void addInstruction(Instruction instr){
		instructions.add(instr);
	}
	
	private int blockSize(){
		return instructions.size();
	}
	
	public int getInstructionIndex(Instruction instr){
		for(int i = 0; i<instructions.size(); i++){
			if (instr.equals(instructions.get(i))){
				return i;
			}
		}
		return -1; //This happens if the instruction is not found;
	}
	
	//links the branch instructions with the beginning of the block;
	//linking to other blocks will occor in the linker stage;
	//linking with jumps will be the best way to branch!	
	public void blockLinker(){
		
	}
}
