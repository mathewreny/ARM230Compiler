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
		String[] argsHC = {"-f","test.s230"};	//REMOVE THIS LINE ONCE READY FOR INSTALLATION!
		
		
		
		Scanner reader;
		
		
		
		
		
		
		
		
		/*  THIS IS THE .S230 INSTRUCTION ARRAYLIST GENERATOR!
		 * 
		 * Takes in a .S230 File and puts the instructions
		 * into the instructions arraylist.  The purpose of
		 * doing this is so that our compiler can take the 
		 * data generated here and convert it to binary.
		 * 
		 */
		
		
//NOTE! THE FOLLOWING COMMENTED CODE SHOUDLD BE PLACED INTO THE JAVA FILE
//WHEN IT IS READY TO BE PACKAGED INTO AN INSTALATION! 
		//REPLaCEINITS230
		//REPLaCEINITARGS
		//REPLaCEINITS230 initS230 = new REPLaceINITS230(REPLaCEINITARGS);
		InitS230WIN initS230 = new InitS230WIN(argsHC); //REPLACE THIS WHEN READY TO PACKAGE FINAL PRODUCT
		
		reader = initS230.getScanner();
				

		
		ArrayList<Instruction> instructionsS230 = new ArrayList<Instruction>();

		while(reader.hasNextLine()){
			//Get the line to read;
			String line = reader.nextLine();
			//This regex strips away all whitespace
			line = line.replaceAll("\\s+"," ");
			line = line.trim();
			System.out.println(line);
			if(InitS230.isInstruction(line)){
				//System.out.println("this line is valid: " + line);
				instructionsS230.add(InitS230.getInstruction(line));
			} else {
				//System.out.println("this line is not an Instruction: (" + line + ")");
			}
		}
		
		System.out.println("\nFin\n");
		
		for(Instruction I: instructionsS230){
			//System.out.println(I.getComponentsToString());
			System.out.println(I.toBinaryInstruction());
		}
		
		
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
		
		
		/* THIS IS THE .mif FILE WRITER!
		 * 
		 * 	Outputs the compiled code into a memory initialization file. This
		 *  is the last thing the compiler needs to do and this will be updated
		 *  as more features are added to the compiler i.e Constants and Locations
		 * 
		 */
		
//NOTE! THE FOLLOWING COMMENTED CODE SHOUDLD BE PLACED INTO THE JAVA FILE
//WHEN IT IS READY TO BE PACKAGED INTO AN INSTALATION! 
			//REPLaCEINITMIF 	Used to distinguish between InitMIFWIN and InitMIF
			//REPLaCEINITARGS   Used to distinguish between hard coding and command line
			//REPLaCEINITMIF initMIF = new REPLaACINITMIF(REPLaCEINITARGS);
			InitMIFWIN initMIF = new InitMIFWIN(argsHC); //REPLACE THIS WHEN READY TO PACKAGE FINAL PRODUCT
				
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
			writer.println(" \t0\t : 000000000000000000000000;  --Beginning whitespace needed");
			
			
			int memorySpace = 1;
			int instructionMemorySpace = instructionsS230.size()+memorySpace-1;
			for(int i = memorySpace; i<instructionMemorySpace; i++){
				writer.println(" \t"+i+"\t\t : "+instructionsS230.get(
						instructionMemorySpace-i).toBinaryInstruction() +";");
				memorySpace++;
			}
			writer.println(" \t["+memorySpace+"..1023]\t : 000000000000000000000000;  --NEED THIS LINE");
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
