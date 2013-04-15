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

import edu.unl.cse.instructions.ADDI;
import edu.unl.cse.instructions.Instruction;
import edu.unl.cse.instructions.LI;
import edu.unl.cse.instructions.SI;


public class Compiler {

//There is at least function at the bottom...
	
	public static void main(String[] args){

		//PROGRAM INITIALIZATION STAGE.
		boolean runS230Trans = true; //ALWAYS SET TO TRUE;
		ArrayList<String> argsGlobal = new ArrayList<String>();
		if(isWindows()){  //might decide to change this to always.
			Scanner argsSetup = new Scanner(System.in);
			System.out.print("Enter the filename to be assembled");
			String filename = argsSetup.next();
			
			argsGlobal.add("-f");
			argsGlobal.add(filename.trim());
			if(filename.contains(".S")){
				runS230Trans = false;
				argsGlobal.add("-S");
			} else {
				System.out.println("S230 output file: "+filename.substring(0,filename.indexOf("."))+"GENERATED.S230");
			}
				System.out.println("Mif output file: MemoryInitialization"+filename.substring(0,1).toUpperCase()+filename.substring(1,filename.indexOf("."))+".mif");
			argsSetup.close();
		}
		
		if(args.length > 0){
			for(String s: args){
				if(s.equals("-S"))
					runS230Trans = false; 	//ALWAYS SET TO FALSE;
			}
		}

			
//****************TRANSLATOR*********************************************************************//		
if (runS230Trans){	/**BEGIN TRANSLATION OF .s230 CODE TO .S230 CODE AND OUTPUT CONTENTS. */

		String[] argsHC = argsGlobal.toArray(new String[argsGlobal.size()]);
		
		InitTrans initTrans = new InitTrans(argsHC);
		ArrayList<String> lines = initTrans.getLines();
		PrintWriter writer = initTrans.getPrintWriter();
		int lineNumber = 1;  //For error debugging.  KEEP
		//******************************************************************************
		for(String line: lines){
			//Check to see if part of .text;

			
			//Instruction
			if(initTrans.isInstruction(line)){
					if(initTrans.isSupportedInstruction(line)){
						System.out.println("Supported instruction. Add line to Instructions\n\t"+line);
						ArrayList<Instruction> instructions = initTrans.getTranslation(line);
						for(Instruction instr: instructions){
							writer.println(InitTrans.toS230(instr));
						}
					} else {
						System.out.println("Instruction: "+line.substring(0,line.indexOf(" "))+" is not supported. Add it please");
					}
			} 
			//CONSTANT
			else if (InitTrans.isConstant(line)){
					System.out.println("Constant: "+line);
					if(line.replaceAll("/s+","").contains(".text")){
						System.out.println(".text IS NOT SUPPORTED. Please remove all text");
					} else if (line.contains(".equ")){  //constant that is supported
						writer.println("."+line.substring(5,line.indexOf(","))+"("+line.substring(line.indexOf(",")+1).trim()+")");
					}
					
			} 
			//COMMENT
			else if (InitTrans.isComment(line)){
					System.out.println("Comment: "+line);
					writer.println(line);
			} 
			//LOCATION
			else if (InitTrans.isLocation(line)){
					System.out.println("Location: "+line);
					writer.println(line);
			} 
			//ERROR
			else {
				System.out.println("Error on line "+lineNumber+". Instruction may not be supported yet!\n"+
						"\tYour Code: "+line);
			}
			lineNumber++;
		}
		//*******************************************************************************
		writer.close();
			
}	/**END TRANSLATION OF .s230 CODE CONTENTS SHOULD BE OUTPUTTED */
		
		
		
		
		
		
 


//******************************COMPILER*************************************************************//
//It takes valid S230 Specification code and outputs it to a .mif file.
//VALID S230 CODE EXAMPLES ARE FOUND IN THE README!
if (true){	/** BEGIN COMPILATION */
	
 		//*******************************************************
		//this is the initialization stage for the .S230 files. *
		//*******************************************************	
		String[] argsHC = argsGlobal.toArray(new String[argsGlobal.size()]);
		
		InitS230 initS230 = new InitS230(argsHC);		
		InitMIF initMIF = new InitMIF(argsHC); 
		
		 
		//CREATE THE SCANNER OBJECT		
		Scanner reader;
		//This is needed to generate valid locations! It is set to # to offset for 
		//the jump at the beginning of the code.  The jump is needed to skip
		//the constants that the program creates in memory.
		int MemoryAddress = 1;		//SET TO 1 because the j (constants.size()) hard coded in the .mif generator.
		
		
		//These arraylists are used throughout the program;
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
		constantsS230.add(new Constant("BOARDSWITCH", 32768)); //value = 1000000000000000
		constantsS230.add(new Constant("BOARDKEY", 16384)); //value = 0100000000000000
		constantsS230.add(new Constant("BOARDHEX0", 8192)); //value = 0010000000000000
		constantsS230.add(new Constant("BOARDLEDGREEN", 4096)); //value = 0001000000000000

		
		while(reader.hasNextLine()){
			//Get the line to read;
			String line = reader.nextLine();
			//This regex strips away all whitespace
			line = line.replaceAll("\\s+"," ");
			//kills comments too! I wish :(
			line = line.replace("--*", "");
			line = line.trim().toLowerCase();
//			System.out.println(line);
			if(line.length()>0 && line.substring(0,1).equals(".")){ //DO NOT TAKE OUT THE SHORT CIRCUIT!
				//System.out.println("this line is valid: " + line);
				Constant toAdd = InitS230.getConstant(line);
				if(!constantsS230.contains(toAdd)){  //If the constant is not here
					constantsS230.add(toAdd);				
				} else {  //warn the user that he doesn't need to include this constant.
					System.out.println("WARNING: Constant "+toAdd.getName()+" is declared more than once.");
				}

			}
		}			
		
		
		
		
		/*  THIS IS THE .S230 INSTRUCTION AND LOCATION ARRAYLIST GENERATOR!
		 * 
		 * Takes in a .S230 File and puts the instructions
		 * into the instructions arraylist.  The purpose of
		 * doing this is so that our compiler can take the 
		 * data generated here and convert it to binary.
		 * 
		 */	
		reader = initS230.getScanner();
		while(reader.hasNext()){
			//Get the line to read;
			String line = reader.nextLine();
			//This regex strips away all whitespace
			line = line.replaceAll("\\s+"," ");
			line = line.trim().toLowerCase();
			System.out.println(line);
//			System.out.println(line);
			if(InitS230.isInstruction(line)){
				//System.out.println("this line is valid: " + line);
//				System.out.println(line);
				Instruction instructionToAdd = InitS230.getInstruction(line);
				if(instructionToAdd instanceof LI || instructionToAdd instanceof ADDI || instructionToAdd instanceof SI){
					instructionToAdd.loadConstants(constantsS230);
				}
				instructionsS230.add(instructionToAdd);

//				System.out.println(instructionToAdd.toString());
				MemoryAddress++;		
			} else if(InitS230.isLocation(line)){
				locationsS230.add(InitS230.getLocation(line, MemoryAddress));
//				System.out.println(locationsS230.get(locationsS230.size()-1).toString());
			} 
		}
		
		System.out.println("\nFin\n");
		

		

		
//*******************************MIF FILE WRITER.	
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
			writer.println("%");
			writer.println("\tThe following is a table of locations and their addresses");
			writer.println("\t\t| Name           | Address     |");
			for(Location l: locationsS230){
				String toWrite = "\t\t| "+l.getName();
				for(int i=0; i<15-l.getName().length();i++){
					toWrite = toWrite.concat(" ");
				}
				Integer integer = l.getAddress();
				toWrite = toWrite+"| "+integer.toString();
				for(int i=0; i<12-integer.toString().length(); i++){
					toWrite = toWrite.concat(" ");
				}
				toWrite = toWrite.concat("|");
				writer.println(toWrite);
			}
			writer.println();
			writer.println("\tThe following is a table of constants and their values");
			writer.println("\t\t| Name           | Value       |");
			for(Constant c: constantsS230){
				String toWrite = "\t\t| "+c.getName();
				for(int i=0; i<15-c.getName().length();i++){
					toWrite = toWrite.concat(" ");
				}
				Integer integer = c.getValue();
				toWrite = toWrite+"| "+integer.toString();
				for(int i=0; i<12-integer.toString().length(); i++){
					toWrite = toWrite.concat(" ");
				}
				toWrite = toWrite.concat("|");
				writer.println(toWrite);
			}
			writer.println("%");
			writer.println();
			writer.println("CONTENT BEGIN");
			writer.println("          0  :  000000000000000000000000; --Memory address : data");
   			
			/* NOT ACTUALLY NEEDED but here as an example.			
			writer.println(" \t1\t\t\t : \t0110"+returnBinaryNumber(constantsS230.size(),20)+"; --SKIP CONSTANTS!");
			int memorySpace = 2;
   			for(int i = 0; i<constantsS230.size(); i++){
   				writer.println(" \t"+memorySpace+"\t\t\t : \t"+returnBinaryNumber(constantsS230.get(
   						i).getValue(), 24)+"; --Constant "+constantsS230.get(i).getName()+
   						" with value "+constantsS230.get(i).getValue());
   				memorySpace++;
   			}
   			*/
   			//LEAVE MIF WRITER!
   			/* LINKER!
   			 * 
   			 *  This is the linker. It takes the current address of the instruction
   			 *  and it subtracts it from the location to be linked to. Then it changes
   			 *  the location component of the instruction to an integer!
   			 * 
   			 */
			int memorySpace = 1;
   			for(int i = 0; i<instructionsS230.size(); i++){
   				instructionsS230.get(i).linker(locationsS230, i+memorySpace-1);
   				instructionsS230.get(i).constantLinker(constantsS230);
//   				System.out.println(instructionsS230.get(i).getComponentsToString());
//   				System.out.println(instructionsS230.get(i).toBinaryInstruction()+";\t"+
//   						instructionsS230.get(i).generateMIFComment());
   			}
   			
			//BACK TO MIF WRITER!
			for(int i = 0; i<instructionsS230.size(); i++){
				String toWrite = "";
				Integer memSpaceOffset = memorySpace;
				for(int spacei = 0; spacei<11-memSpaceOffset.toString().length();spacei++){
					toWrite = toWrite.concat(" ");
				}
				toWrite = toWrite.concat(memorySpace+"  :  ");
				toWrite = toWrite.concat(instructionsS230.get(i).toBinaryInstruction()+";");
				toWrite = toWrite.concat(instructionsS230.get(i).generateMIFComment());
				writer.println(toWrite);
				memorySpace++;
			}
			Integer finalmem = memorySpace;
			String finaltowrite = "";
			for(int i = 0; i<3-finalmem.toString().length(); i++){
				finaltowrite = finaltowrite.concat(" ");
			}
			writer.println(finaltowrite+"["+memorySpace+"..1023]  :  000000000000000000000000; --Fill out rest of memory");
			writer.println("END;");
			
			
			writer.close();
			
} /** END COMPILATION */
//THE ABOVE CODE WORKS BUT HAS NOT BEEN VERIFIED 100%
	
	
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
