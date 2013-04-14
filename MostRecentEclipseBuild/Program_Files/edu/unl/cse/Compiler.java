package edu.unl.cse;
/* 
	This file was created by
	Mathew Reny
	Mhreny@gmail.com
	
	Email me if you have any questions.
	
	I HAD TO MAKE THIS ALL IN ONE PACKAGE FOR A REASON!
	THE REASON IS THAT I WASN'T ABLE TO GET THE PACKAGE
	TO WORK WITH JAVAC. IF YOU FIGURE OUT HOW TO DO THIS
	PLEASE EMAIL ME! IT WOULD ALLOW THIS CODE TO BE SO
	MUCH MORE MANAGEABLE.
	
*/


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import edu.unl.cse.instructions.Instruction;


public class Compiler {

//There is at least function at the bottom...
	
	public static void main(String[] args){

 
 		//***********************************
		//this is the initialization stage. *
		//***********************************
		ArrayList<String> tempargs = new ArrayList<String>();
		if(isWindows()){
			tempargs.add("-f");
			tempargs.add("assembly.s230");			
		} else {
			int i=0;
			for(String arg: args){
				tempargs.add(arg);
			}
		}
		String[] argsHC = tempargs.toArray(new String[tempargs.size()]);
		
		InitS230 initS230 = new InitS230(argsHC);		
		InitMIF initMIF = new InitMIF(argsHC); 
		
		 
		//CREATE THE SCANNER OBJECT		
		Scanner reader;
		//This is needed to generate valid locations! It is set to # to offset for 
		//the jump at the beginning of the code.  The jump is needed to skip
		//the constants that the program creates in memory.
		int MemoryAddress = 1;		//SET TO 1 because the j (constants.size()) hard coded in the .mif generator.
		
		
		
		ArrayList<Location> locationsS230 = new ArrayList<Location>();
		ArrayList<Instruction> instructionsS230 = new ArrayList<Instruction>();
		ArrayList<Constant> constantsS230 = new ArrayList<Constant>();
		
		
		/*  	THIS IS THE .S230 CONSTANT ADDRESS GENERATOR
		 *
		 *	 	Puts Constants into an arraylist so that the .mif writer
		 *  	can put them into the memory before the instructions.  This
		 *		is needed so that we can load things like the stack pointer
		 *		address into the memory in an address location that is  
		 *		small enough for the addi r13 r0 LOC; lw r14 0(r13); code
		 *		to work properly. That code replaces lw SP 0(LOC). Since addi 
		 *		only allows bunbers up to 7 bits wide, the address must be 
		 * 	placed at the beginning.  That will be accomplished in 
		 *		the .mif writing stage. 
		 *
		 */
		reader = initS230.getScanner();
		
		//ADD CONSTANTS FOR THE BOARD LOGIC OUTPUTS!
		MemoryAddress++;
		constantsS230.add(new Constant("BOARDSWITCH", 32768)); //value = 1000000000000000
		MemoryAddress++;
		constantsS230.add(new Constant("BOARDKEY", 16384)); //value = 0100000000000000
		MemoryAddress++;
		constantsS230.add(new Constant("BOARDHEX0", 8192)); //value = 0010000000000000
		MemoryAddress++;
		constantsS230.add(new Constant("BOARDLEDGREEN", 4096)); //value = 0001000000000000

		
		while(reader.hasNextLine()){
			//Get the line to read;
			String line = reader.nextLine();
			//This regex strips away all whitespace
			line = line.replaceAll("\\s+"," ");
			//kills comments too! I wish :(
			line = line.replace("--*", "");
			line = line.trim().toLowerCase();
			System.out.println(line);
			if(line.length()>0 && line.substring(0,1).equals(".")){ //DO NOT TAKE OUT THE SHORT CIRCUIT!
				//System.out.println("this line is valid: " + line);
				constantsS230.add(InitS230.getConstant(line));
				MemoryAddress++;
			}
		}			
		
		
		
		
		/*  THIS IS THE .S230 INSTRUCTION ARRAYLIST GENERATOR!
		 * 
		 * Takes in a .S230 File and puts the instructions
		 * into the instructions arraylist.  The purpose of
		 * doing this is so that our compiler can take the 
		 * data generated here and convert it to binary.
		 * 
		 */	
		reader = initS230.getScanner();
		while(reader.hasNextLine()){
			//Get the line to read;
			String line = reader.nextLine();
			//This regex strips away all whitespace
			line = line.replaceAll("\\s+"," ");
			line = line.replace("--*", "");
			line = line.trim().toLowerCase();
			System.out.println(line);
			if(InitS230.isInstruction(line)){
				//System.out.println("this line is valid: " + line);
				instructionsS230.add(InitS230.getInstruction(line));
				MemoryAddress++;
			} else if(InitS230.isLocation(line)){
				locationsS230.add(InitS230.getLocation(line, MemoryAddress+1));
				System.out.println(locationsS230.get(locationsS230.size()-1).toString());
			}
		}
		
		System.out.println("\nFin\n");
		

		

		
	
		/* THIS IS THE .mif FILE WRITER!
		 * 
		 * 	Outputs the compiled code into a memory initialization file. This
		 *  is the last thing the compiler needs to do and this will be updated
		 *  as more features are added to the compiler i.e Constants and Locations
		 * 
		 */
						
			PrintWriter writer = initMIF.getPrintWriter();
			
			writer.println("-- SMP ARM230Compiler generated Memory Initialization File (.mif)");
			writer.println("-- Works with the University Of Nebraska CSCE230 Class Processor");
			writer.println();
			writer.println("WIDTH=24;                    -- The size of memory in words");
			writer.println("DEPTH=1024;                  -- The size of data in bits");
			writer.println();
			writer.println("ADDRESS_RADIX = UNS;         -- The radix for address values");
			writer.println("DATA_RADIX = BIN;            -- The radix for data values");
			writer.println();
			writer.println("CONTENT BEGIN");
			writer.println(" \t0\t\t\t : \t000000000000000000000000; --Memory address : data");
			writer.println(" \t1\t\t\t : \t0110"+returnBinaryNumber(constantsS230.size(),20)+"; --SKIP CONSTANTS!");
   			
			// TODO ADD Constant locations for LED, HEX, SWITCH, AND PUSH BUTTONS!
			
			int memorySpace = 2;
   			for(int i = 0; i<constantsS230.size(); i++){
   				writer.println(" \t"+memorySpace+"\t\t\t : \t"+returnBinaryNumber(constantsS230.get(
   						i).getValue(), 24)+"; --Constant with value "+constantsS230.get(i).getValue());
   				memorySpace++;
   			}
   			
   			//LEAVE MIF WRITER!
   			/* LINKER!
   			 * 
   			 *  This is the linker. It takes the current address of the instruction
   			 *  and it subtracts it from the location to be linked to. Then it changes
   			 *  the location component of the instruction to an integer!
   			 * 
   			 */
   			for(int i = 0; i<instructionsS230.size(); i++){
   				instructionsS230.get(i).linker(locationsS230, i+memorySpace-1);
   				instructionsS230.get(i).constantLinker(constantsS230);
   				System.out.println(instructionsS230.get(i).getComponentsToString());
   				System.out.println(instructionsS230.get(i).toBinaryInstruction()+";\t"+
   						instructionsS230.get(i).generateMIFComment());
   			}
   			
			//BACK TO MIF WRITER!
			for(int i = 0; i<instructionsS230.size(); i++){
				writer.println(" \t"+memorySpace+"\t\t\t : \t"+instructionsS230.get(
						i).toBinaryInstruction() +";"
						+instructionsS230.get(i).generateMIFComment());
				memorySpace++;
			}
			writer.println(" \t["+memorySpace+"..1023]\t : \t000000000000000000000000; --Fill out rest of memory");
			writer.println("END;");
			
			
			writer.close();
	}
	
	
	//WORKS, DO NOT TOUCH KTHANKS.
	public static String returnBinaryNumber(int number, int stringLength){
		String binary = "";
		if(number>0){
			binary = Integer.toBinaryString(number);
			for(int i= (stringLength-binary.length()); i>0; i--){
				binary = "0"+binary;
			}
		} else if (number<0){
			int numberTemp = ((-1*number) - 1);
			String tempBinary = Integer.toBinaryString(numberTemp);
			tempBinary = "0"+tempBinary; //this is needed for numbers of power 2
			for(int i= (stringLength-tempBinary.length()); i>0; i--){
				binary = "1"+binary;
			}
			for(int i = 0; i<tempBinary.length(); i++){
				if(tempBinary.substring(i, i+1).equals("0")){
					binary = binary.concat("1");
				} else {
					binary = binary.concat("0");
				}
			}
		} else {
			for(int i=0; i<stringLength; i++){
				binary += "0";
			}
		}
		
		return binary;
	}

	public static boolean isWindows(){
			if(System.getProperty("os.name").toLowerCase().contains("win")){
				return true;
			}
			return false;
	}

}
