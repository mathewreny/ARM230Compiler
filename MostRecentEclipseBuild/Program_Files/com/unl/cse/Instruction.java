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
	
	public String getComponentsToString(){
		String comps = "Instruction "+ this.name +" has components";
		for(String S: components){
			comps = comps.concat(" " + S);
		}
		return comps;
	}
	
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
	
	protected static boolean isLocation(String component){
		return !isRegister(component);
	}
	
	protected static String registerToBinary(String component){
		int compon = Integer.parseInt(component);
		return Compiler.returnBinaryNumber(compon, 4);
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
