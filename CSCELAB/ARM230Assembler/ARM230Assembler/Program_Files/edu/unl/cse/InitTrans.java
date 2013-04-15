package edu.unl.cse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import edu.unl.cse.instructions.Instruction;

public class InitTrans {
	
	private ArrayList<String> argList = new ArrayList<String>();
	private HashSet<String> validAlteraInstructions = new HashSet<String>();
	private HashSet<String> supportedAlteraInstructions = new HashSet<String>();
	//****************************************************************************
	//constructor	
	//****************************************************************************
	public InitTrans(String[] args){
		for(int i = 0; i<args.length; i++){
			String arg = args[i];
			this.argList.add(arg);
			//System.out.println("added "+arg);
		}
		
		for (AlteraInstruction a : AlteraInstruction.values()) {
		      validAlteraInstructions.add(a.name());
		}
		
		for (SupportedAlteraInstruction s: SupportedAlteraInstruction.values()){
			supportedAlteraInstructions.add(s.name());
		}
		
	}
	
	
	//this is needed for the compiler.
	public ArrayList<String> getLines(){
		ArrayList<String> toReturn = new ArrayList<String>();
		Scanner reader = this.getScanner();
		while(reader.hasNextLine()){
			String line = reader.nextLine();
			if(line.contains("--")){
				toReturn.add(line.substring(line.indexOf("--")));
				line = line.substring(0, line.indexOf("--"));	
//				System.out.println(line+"REMOVED COMMENT");
			}
			line = line.replaceAll("\\s+"," ");
			
			//this is needed for the following code;  LOCATION: addi r2 r4 9;
			String instrLast = null;
			if(InitTrans.isLocation(line)){
				if(!line.substring(line.indexOf(":")).trim().equals("")){
					instrLast = line.substring(0,line.indexOf(":")+1).trim();
					line = line.substring(line.indexOf(":")+1);
				}
			}
			
			line = line.trim();
			
			//check to make sure the line isn't blank.
			if(!line.equals("")){
				toReturn.add(line);
			} 
			
			if(instrLast != null){
				toReturn.add(instrLast);
			}
		}
		return toReturn;
	}
	
	
	//*****************************************************************************
	//Scanner method
	//*****************************************************************************
	//Get the scanner object. Chose to make this not static 
	//because it is a global thing that needs to be reset often.
	public Scanner getScanner(){
	  
		String filename = "";
		for(int i = 0; i<argList.size(); i++ ){
			if(argList.get(i).equals("-f")){
				filename = argList.get(i+1);
				//DIFFERENT FROM OTHER INIT FILE!
				filename = filename.trim().replace(".s230", ".S230");
			}
		}
		String dirName = System.getProperty("user.dir");
		if(Compiler.isWindows()){ //IF THE OPPERATING SYSTEM IS WINDOWS
			System.out.println(System.getProperty("user.dir") + " ... " + filename);
			dirName = dirName.replace("\\Program_Files\\source","\\").trim();
			dirName = dirName.replace("\\Program_Files\\classes","\\").trim();
			dirName = dirName.concat("\\InputOutputFolder\\");
			System.out.println(dirName+filename);
		} else {
			System.out.println(System.getProperty("user.dir") + " ... " + filename);
			dirName = dirName.replace("/Program_Files/source","/").trim();
			dirName = dirName.replace("/Program_Files/classes","/").trim();
			dirName = dirName.replace("/Program_Files","/").trim();
			dirName = dirName.concat("/InputOutputFolder/");
		}	
		File file = new File(dirName, filename);
		try {
			return new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	//*****************************************************************
	//PRINT WRITER METHOD
	//*****************************************************************
	public PrintWriter getPrintWriter(){
	  	String filename = "";
		//CHECKS FOR THE FOLLOWING FLAGS
		//-f filename specified. Should always be present
		//-o* output specified.		
		for(int i = 0; i<argList.size(); i++ ){
			if(argList.get(i).equals("-f")){
				filename = argList.get(i+1);
			}
		}
//		System.out.println(System.getProperty("user.dir") + " ... " + filename);
		String dirName = System.getProperty("user.dir");
		if(Compiler.isWindows()){
			dirName = dirName.replace("\\Program_Files\\source","\\").trim();
			dirName = dirName.replace("\\Program_Files\\classes","\\").trim();
			dirName = dirName.concat("\\InputOutputFolder\\");
		} else {
			dirName = dirName.replace("/Program_Files/source","/").trim();
			dirName = dirName.replace("/Program_Files/classes","/").trim();
			dirName = dirName.concat("/InputOutputFolder/");	
		}
		//			System.out.println(dirName+filename);
		File file = new File(dirName, filename.substring(0,filename.indexOf("."))+"GENERATED.S230");
		try{
			return new PrintWriter(file);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
	  return null;
	}
	
	
	public static boolean isConstant(String line){
		if(line.substring(0,1).equals(".")){
			return true;
		}
		return false;
	}
	
	public static boolean isComment(String line){
		if(line.contains("--"))
			return true;
		return false;
	}
	
	
	private enum AlteraInstruction{
		call, jmpi, ldbu, addi, stb, br, ldb, cmpgei, ldhu, andi, sth, bge, ldh, cmplti, initda, ori, stw, blt, ldw, cmpnei, flushda, xori, bne, 
		cmpeqi, ldbuio, muli, stbio, beq, ldbio, cmpgeui, ldhuio, andhi, sthio, bgeu, ldhio, cmpltui, custom, initd, orhi, stwio, bltu, ldwio,
		rdprs, flushd, xorhi, eret, roli, rol, flushp, ret, nor, mulxuu, cmpge, bret, ror, flushi, jmp, cmplt, slli, sll, wrprs, or, mulxsu, wrctl,
		cmpne, srli, srl, nextpc, callr, cmpeq, divu, div, rdctl, mul, cmpgeu, initi, trap, cmpltu, add, sync, sub, srai, sra, and, xor, mulxss;
	}
	
	//NOT STATIC BECAUSE I WANT THE HASHSET FUNCTIONALITY!;
	public boolean isInstruction(String line){
		if(line.indexOf(" ") == -1){
			return false;			
		}
		if(validAlteraInstructions.contains(line.substring(0,line.indexOf(" ")))){
			return true;
		}
		return false;
	}
	
	private ArrayList<String> sanitizeInstruction(String line){
		
		//GET NAME!
		ArrayList<String> toReturn = new ArrayList<String>();
		line = line.trim().toLowerCase();
		toReturn.add(line.substring(0,line.indexOf(" ")));
		line = line.substring(line.indexOf(" ")).trim();
		//GET VARIABLES AND REGISTERS
		while(line.indexOf(",") != -1){
			line = line.trim();
			toReturn.add(line.substring(0,line.indexOf(",")));
			line = line.substring(line.indexOf(",")+1).trim();
		}
		toReturn.add(line.trim());
		System.out.print("Sanitizing data :(");
		for(String s: toReturn){
			System.out.print("____"+s+"___");
		}
		System.out.print(")\n");
		return toReturn;
	}
	// TODO FILL THIS OUT!
	public ArrayList<Instruction> getTranslation(String line){
		//	add, and, or, xor, addi,
		//	ldw, stw, jmp, cmp, br, bal, 
		//	blt, beq, bne, sub, sll;
		ArrayList<Instruction> toReturn = new ArrayList<Instruction>();
		ArrayList<String> components = sanitizeInstruction(line);
		SupportedAlteraInstruction instrSwitch = SupportedAlteraInstruction.valueOf(components.get(0));
		components.remove(0);
		switch(instrSwitch){
			case add:
					Instruction add = new Instruction("add");
					for(String c: components){
						add.setNextComponent(c);
					}
					toReturn.add(add);
					break;
			case and:
					Instruction and = new Instruction("and");
					for(String c: components){
						and.setNextComponent(c);
					}
					toReturn.add(and);
					break;
			case or:
					Instruction or = new Instruction("or");
					for(String c: components){
						or.setNextComponent(c);
					}
					toReturn.add(or);
					break;
			case xor:
					Instruction xor = new Instruction("xor");
					for(String c: components){
						xor.setNextComponent(c);
					}
					toReturn.add(xor);
					break;
			case addi:
					Instruction addi = new Instruction("addi");
					for(String c: components){
						addi.setNextComponent(c);
					}
					toReturn.add(addi);
					break;
			case ldw:
					Instruction lw = new Instruction("lw");
					for(String c: components){
						lw.setNextComponent(c);
					}
					toReturn.add(lw);
					break;
			case stw:
					Instruction sw = new Instruction("sw");
					for(String c: components){
						sw.setNextComponent(c);
					}
					toReturn.add(sw);
					break;
			case jmpi:
					Instruction jmpi = new Instruction("j");
					for(String c: components){
						jmpi.setNextComponent(c);
					}
					toReturn.add(jmpi);
					break;
			case jmp:
					Instruction jr = new Instruction("jr");
					for(String c: components){
						jr.setNextComponent(c);
					}
					toReturn.add(jr);
					break;
			case br:
					Instruction b = new Instruction("b");
					for(String c: components){
						b.setNextComponent(c);
					}
					toReturn.add(b);
					break;
			case blt: 
					Instruction blt = new Instruction("blt");
					for(String c: components){
						blt.setNextComponent(c);
					}
					toReturn.add(blt);
					break;
			case beq: 
					Instruction beq = new Instruction("beq");
					for(String c: components){
						beq.setNextComponent(c);
					}
					toReturn.add(beq);
					break;
			case bne:
					Instruction bne = new Instruction("bne");
					for(String c: components){
						bne.setNextComponent(c);
					}
					toReturn.add(bne);
					break;
			case sub:
					Instruction sub = new Instruction("sub");
					for(String c: components){
						sub.setNextComponent(c);
					}
					toReturn.add(sub);
					break;
			case sll:
					Instruction sll = new Instruction("sll");
					for(String c: components){
						sll.setNextComponent(c);
					}
					toReturn.add(sll);
					break;
			case call:
					Instruction bal = new Instruction("bal");
					for(String c: components){
						bal.setNextComponent(c);
					}
					toReturn.add(bal);
					break;
			case ldbuio:
					//GO TO THE NEXT PLACE!
			case ldbu:
					Instruction ldbu = new Instruction("lw");
					for(String c: components){
						ldbu.setNextComponent(c);
					}
					toReturn.add(ldbu);
					//cmp rLDBU r0
					Instruction cmpldbu = new Instruction("cmp");
					cmpldbu.setNextComponent(components.get(0));
					cmpldbu.setNextComponent("r0");
					toReturn.add(cmpldbu);
					Instruction sub1ldbu = new Instruction("sub");
					sub1ldbu.setNextComponent("r13"); //THE TEMPORARY REGISTER
					sub1ldbu.setNextComponent("r0");
					sub1ldbu.setNextComponent(components.get(0));
					sub1ldbu.setNextComponent("(COND:0100)"); // negative
					toReturn.add(sub1ldbu);
					Instruction cmpldbu2 = new Instruction("cmp");
					cmpldbu2.setNextComponent(components.get(0));
					cmpldbu2.setNextComponent("r0");
					toReturn.add(cmpldbu2);
					Instruction sub2ldbu = new Instruction("sub");
					sub2ldbu.setNextComponent("r13"); //THE TEMPORARY REGISTER
					sub2ldbu.setNextComponent("r0");
					sub2ldbu.setNextComponent(components.get(0));
					sub2ldbu.setNextComponent("(COND:0100)"); // negative
					toReturn.add(sub2ldbu);
					break;
					
		}
		return toReturn;
		
	}
	
	
	public static String toS230(Instruction instr){
		String toReturn = instr.getName();
		for(String c: instr.getComponents()){
			toReturn = toReturn.concat(" "+c);
		}
		return toReturn;
	}


	public static boolean isLocation(String line) {
		if(line.indexOf(":")==-1){
			return false;
		} else if(line.indexOf(":")==line.length()-1){
			return true;
		} else if (line.indexOf(":")==-1){
			return false;
		} else if (line.substring(line.indexOf(" ")-1,line.indexOf(" ")).equals(":")){
			return true;
		}
		return false;
	}


	private enum SupportedAlteraInstruction {
		add, and, or, xor, addi,
		ldw, stw, jmp, br, blt, beq, bne, sub, sll, call, jmpi, ldbu, ldbuio;
	}
	
	public boolean isSupportedInstruction(String line) {
		if(line.indexOf(" ") == -1){
			return false;			
		}
		if(supportedAlteraInstructions.contains(line.substring(0,line.indexOf(" ")))){
			return true;
		}
		return false;
	}
	
	
	
}
