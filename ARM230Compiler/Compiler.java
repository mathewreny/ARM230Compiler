/* 
	This file was created by
	Mathew Reny
	Mhreny@gmail.com
	
	Email me if you have any questions.
*/



package ARM230Compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Compiler {

	public static void main(String[] args){

/** NEED TO START SIMPLE AND ADD EXCEPTIONS AS I MOVE ALONG
 * 
 * 	Order of building:
 * 		
 * Part	a) Create working compiler for .S230 files.
 * 
 *  	1) Take input from a .S230 file
 * 
 * 		2) Convert the input into sequential instructions that
 * 			must be copied and pasted into the .mif file
 * 			manually.  
 * 
 * 		3) 
 * 
 * 		3) Create a working .mif generator.
 * 		
 * 
 * 
 * 		)
 * 		
 */
		//this is the initialization stage.
		String[] argsHC = {"-f","test.S230"}; 
		//Init init = new Init(argsHC);	
		Scanner reader;
		
		
		
		
		/*  THIS IS THE .S230 INSTRUCTION ARRAYLIST GENERATOR!
		 * 
		 * Takes in a .S230 File and puts the instructions
		 * into the instructions arraylist.  The purpose of
		 * doing this is so that our compiler can take the 
		 * data generated here and convert it to binary.
		 * 
		 */
		ArrayList<Instruction> instructionsS230 = new ArrayList<Instruction>();
		
		InitS230 initS230 = new InitS230(args);
		reader = initS230.getScanner();
		
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
			System.out.println(I.getComponentsToString());
		}
		
	
	}
}
