package com.unl.cse;
/* 
	This file was created by
	Mathew Reny
	Mhreny@gmail.com
	
	Email me if you have any questions.
*/

//import unl.cse.instructions;
//import inits.InitS230;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class Compiler {

//There are functions at the bottom...
	
	public static void main(String[] args){

 
 		//***********************************
		//this is the initialization stage. *
		//***********************************
		
			
//The words "//REPLaCEARGSHC" and "//REPLaCEARGSCL"
//will be changed to "String[]" depending on whether
//you chose windows or linux when you installed. This
//will remove the comments and make the code compilable.
//THIS IS DONE BY THE INSTALLER! DO NOT CHANGE THIS CODE!
		//REPLACEARGSHC[] argsHC = {"-f","assembly.s230"};  //DO NOT MANUALLY CHANGE THIS LINE
		//REPLACEARGSCL[] argsCL = args; 						 //DO NOT MANUALLY CHANGE THIS LINE
		String[] argsHC = {"-f","assembly.s230"};	//REMOVE THIS LINE ONCE READY FOR INSTALLATION!

//NOTE! THE FOLLOWING COMMENTED CODE SHOUDLD BE PLACED INTO THE JAVA FILE
//WHEN IT IS READY TO BE PACKAGED INTO AN INSTALATION! 
		//REPLaCEINITS230	Used to distinguish between InitS230WIN and InitS230
		//REPLaCEINITARGS	used to distinguish between hard coding and command line
		//REPLaCEINITS230 initS230 = new REPLaCEINITS230(REPLaCEINITARGS);
		InitS230WIN initS230 = new InitS230WIN(argsHC); //REPLACE THIS WHEN READY TO PACKAGE FINAL PRODUCT
		
//NOTE! THE FOLLOWING COMMENTED CODE SHOUDLD BE PLACED INTO THE JAVA FILE
//WHEN IT IS READY TO BE PACKAGED INTO AN INSTALATION! 
		//REPLaCEINITMIF 	Used to distinguish between InitMIFWIN and InitMIF
		//REPLaCEINITARGS   Used to distinguish between hard coding and command line
		//REPLaCEINITMIF initMIF = new REPLaACINITMIF(REPLaCEINITARGS);
		InitMIFWIN initMIF = new InitMIFWIN(argsHC); //REPLACE THIS WHEN READY TO PACKAGE FINAL PRODUCT
		
		
		//CREATE THE SCANNER OBJECT		
		Scanner reader;
		//This is needed to generate valid locations! It is set to # to offset for 
		//the jump at the beginning of the code.  The jump is needed to skip
		//the constants that the program creates in memory.
		int MemoryAddress = 1;
		
		
		ArrayList<Location> locationsS230 = new ArrayList<Location>();
		ArrayList<Instruction> instructionsS230 = new ArrayList<Instruction>();
		ArrayList<Constant> constantsS230 = new ArrayList<Constant>();
		
		//@TODO: Create this next!
		
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
			//kills comments too! I wish :(
			line = line.replace("--*", "");
			line = line.trim().toLowerCase();
			System.out.println(line);
			if(line.substring(0,1).equals(".")){
				//System.out.println("this line is valid: " + line);
				constantsS230.add(InstS230.getConstant());
				MemoryAddress++;
			}
		}		


		//SHOULD CHANGE TO constants.size() once the arraylist is created;  
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
		

		
		/* LINKER!
		 * 
		 *  This is the linker. It takes the current address of the instruction
		 *  and it subtracts it from the location to be linked to. Then it changes
		 *  the location component of the instruction to an integer!
		 * 
		 */
		
		for(int i = 0; i<instructionsS230.size(); i++){
			instructionsS230.get(i).linker(locationsS230, i);
			System.out.println(instructionsS230.get(i).toBinaryInstruction()+";\t"+
					instructionsS230.get(i).generateMIFComment());
		}
		
	
		/* THIS IS THE .mif FILE WRITER!
		 * 
		 * 	Outputs the compiled code into a memory initialization file. This
		 *  is the last thing the compiler needs to do and this will be updated
		 *  as more features are added to the compiler i.e Constants and Locations
		 * 
		 */
						
			PrintWriter writer = initMIF.getPrintWriter();
			
			writer.println("-- ARM230Compiler generated Memory Initialization File (.mif)");
			writer.println();
			writer.println("WIDTH=24;                    -- The size of memory in words");
			writer.println("DEPTH=1024;                  -- The size of data in bits");
			writer.println();
			writer.println("ADDRESS_RADIX = UNS;         -- The radix for address values");
			writer.println("DATA_RADIX = BIN;            -- The radix for data values");
			writer.println();
			writer.println("CONTENT BEGIN");
			writer.println(" \t0\t\t\t : \t000000000000000000000000; --Memory address : data");
			
            
			int memorySpace = 1;
			for(int i = 0; i<instructionsS230.size(); i++){
				writer.println(" \t"+memorySpace+"\t\t\t : \t"+instructionsS230.get(
						i).toBinaryInstruction() +";"
						+instructionsS230.get(i).generateMIFComment());
				memorySpace++;
			}
			writer.println(" \t["+memorySpace+"..1023]\t : \t000000000000000000000000; --Fill out rest of memory");
			writer.println("END:");
			
			
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


}
