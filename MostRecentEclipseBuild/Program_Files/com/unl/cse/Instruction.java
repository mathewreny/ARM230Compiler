package com.unl.cse;

import java.util.ArrayList;
import java.util.List;

public class Instruction {
	
	
	
	
	protected ArrayList<String> components = new ArrayList<String>();
	protected String name;
	
	public Instruction(){
		this.name = "";
	}
	
	public Instruction(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	//checks to see if the two instructions are equal!
	public boolean equals(Instruction instr){
		if (instr.getName().equals(this.name)){
			for(int i = 0; i<components.size(); i++){
				if (!components.get(i).equals(instr.getComponents().get(i))){
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	
	
	//returns components
	public ArrayList<String> getComponents(){
		return this.components;
	}
	
	
	//outputs components in a nice string.
	public String getComponentsToString(){
		String comps = "Instruction "+ this.name +" has components";
		for(String S: components){
			comps = comps.concat(" " + S);
		}
		return comps;
	}
	
	
	//use this command to add components to the components arraylist
	public void setNextComponent(String component){
		this.components.add(component);
	}
	
	//used to tell if the component is a register. Needs to be static.
	protected static boolean isRegister(String component){
		if(component.substring(0,1).equals("r")){
			if(Integer.parseInt(component.substring(1)) != -1){
				return true;
			} else {
				return false;
			}
		}	else {
			return false;
		}
	}
	
	//OUTDATED DO NOT USE!
	protected static boolean isLocation(String component){
		return !isRegister(component);
	}
	
	protected static String registerToBinary(String component){
		int compon = Integer.parseInt(component);
		return Compiler.returnBinaryNumber(compon, 4);
	}
	
	
	//-------- TODO MIGHT NEED TO CHANGE FOR SW AND LW! --------
	public void linker(ArrayList<Location> locs, int currentAddress){

		//save some time wit this if statement!
	   if(this instanceof B || this instanceof BAL || this instanceof BEQ || this instanceof BLT || this instanceof BNE)
		for(Location loc: locs){
			if(components.contains(loc.getName())){
				components.remove(loc.getName());
				//LOC-currentAddress+2 gives the correct branch offset.
				components.add(""+(loc.getAddress()-(currentAddress+2)));
			}
		}
	}
	
	
	public String toS230Comment(){
		return "";
	}
	
	public String toS230(){
		return "THIS ISN'T IMPLEMENTED!";
	}
	
	public String toBinaryInstruction(){
		String toReturn = "000000000000000000000000; --FILL IN:("+this.name;
		for(String s : this.components){
			toReturn = toReturn.concat(" "+s);
		}
		toReturn = toReturn + ") MANUALLY! INSTRUCTION NOT SUPPORTED!";
		return toReturn;
	}
	
	public String generateMIFComment(){
		String toReturn = "\t--Command:("+this.name;
		for(String s: components){
			toReturn = toReturn.concat(" "+s);
		}
		toReturn = toReturn.concat(")");
		return toReturn;
	}
	

	//ROOM FOR 3 MORE!
	private enum RTypeInstruction{
		add, sub, and, or, xor;
	}
	
	protected String toBinaryALUInstructionRType(){
		String toReturn = "10101110";
		toReturn = toReturn.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(this.components.get(1).substring(1)),4));
		toReturn = toReturn.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(this.components.get(2).substring(1)),4));
		toReturn = toReturn.concat(Compiler.returnBinaryNumber(
				Integer.parseInt(this.components.get(0).substring(1)),4));
		RTypeInstruction instr = RTypeInstruction.valueOf(this.name.trim());
		//put in opx
		switch(instr) {
			case add:
					toReturn = toReturn.concat("100");
					break; 
			case sub:
					toReturn = toReturn.concat("000");
					break;
			case and:
					toReturn = toReturn.concat("010");
					break;
			case or:
					toReturn = toReturn.concat("011");
					break;
			case xor:
					toReturn = toReturn.concat("001");
					break;
		}
		toReturn = toReturn.concat("0");
		return toReturn;
		
	}
}
