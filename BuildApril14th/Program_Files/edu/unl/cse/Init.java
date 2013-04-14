package edu.unl.cse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Init {

	private ArrayList<String> argList = new ArrayList<String>();
	
	//constructor
	public Init(String[] args){
		for(int i = 0; i<args.length; i++){
			String arg = args[i];
			this.argList.add(arg);
			System.out.println("added "+arg);
		}
		
	}
	
	//Get the scanner object. Chose to make this not static 
	//because it is a global thing that needs to be reset often.
	public Scanner getScanner(){
		
		boolean hasFile = false;
		String filename = "";
		for(int i = 0; i<argList.size(); i++ ){
			if(argList.get(i).equals("-f")){
				hasFile = true;
				filename = argList.get(i+1);
			}
		}
		System.out.println(System.getProperty("user.dir") + " ... " + filename);
		File file = new File(System.getProperty("user.dir")+"\\ARM230Compiler\\InputOutputFolder\\", filename);
		Scanner toReturn = null;
		try {
			toReturn = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	}
	
	
	//public ArrayList<String> sanatizeDataInstr(String line){
	//	line = line.trim();
		
		
		
	//}
	
	
	//check to see if valid instruction
	public static boolean isInstruction(String line){
		String[] validInstructions = {"add", "and", "or", "xor", "addi",
				"lw", "sw", "jr", "cmp", "b", "bal", "j", "jal", "li", 
				"blt", "beq", "bne"};
		
		String inst2bedet = line.trim().substring(0, line.indexOf(" "));
		for(int i = 0; i<validInstructions.length; i++){
				if(validInstructions[i].equals(inst2bedet)){
					return true;
				}
		}
		return false;
	}
	
	
	//get the instruction
	//public Instruction getInstruction(ArrayList<String> components){
		
		
		
	//}
	
}
