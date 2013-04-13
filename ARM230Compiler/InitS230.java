package ARM230Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class InitS230 {

	private ArrayList<String> argList = new ArrayList<String>();
	
	//constructor
	public InitS230(String[] args){
		for(int i = 0; i<args.length; i++){
			String arg = args[i];
			this.argList.add(arg);
			//System.out.println("added "+arg);
		}
		
	}
	
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
		System.out.println(System.getProperty("user.dir") + " ... " + filename);
		File file = new File(System.getProperty("user.dir")+"\\ARM230Compiler\\InputFolder\\", filename);
		Scanner toReturn = null;
		try {
			toReturn = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	}
	
	
	
	
	//check to see if valid instruction
	public static boolean isInstruction(String line){
		String[] validInstructions = {"add", "and", "or", "xor", "addi",
				"lw", "sw", "jr", "cmp", "b", "bal", "j", "jal", "li", 
				"blt", "beq", "bne"};
		if(line.trim().indexOf(" ") == -1)
			return false;
		String inst2bedet = line.trim().substring(0, line.indexOf(" "));
		for(int i = 0; i<validInstructions.length; i++){
				if(validInstructions[i].equals(inst2bedet)){
					return true;
				}
		}
		return false;
	}
	
	
	//This is needed for the getInstruction command.
	private static ArrayList<String> sanatizeDataInstr(String lineInput){
		String line = lineInput;
		line = line.trim();
		//System.out.println("Line starts out as:("+line+")");
		ArrayList<String> toReturn = new ArrayList<String>();
		
		while(line.length()>0){
			if(line.indexOf(" ") != -1){
				toReturn.add(line.trim().substring(0, line.indexOf(" ")).trim().toLowerCase());
				line = line.substring(line.indexOf(" "), line.length()).trim();
			} else {
				toReturn.add(line.trim().toLowerCase());
				line = "";
			}
			//System.out.println("Line now equals:(" + line +")");
		}
		
		return toReturn;
	}
	
	//This is needed to create the instructions in getInstruction
	private static String getAdReg(String compon){
		compon = compon.substring(compon.indexOf("(")+1,compon.indexOf(")"));
		return compon;
	}
	
	//This is also needed to create the instructions in getInstruction
	private static String getAdImmd(String compon){
		compon = compon.substring(0,compon.indexOf("("));
		return compon;
	}
	
	//get the instruction
	private static enum InstructionsS230{
		add, and, or, xor, addi,
		lw, sw, jr, cmp, b, bal, j, jal, li, 
		blt, beq, bne;
	}
	
	
	public static Instruction getInstruction(String line){
		ArrayList<String> comps = new ArrayList<String>();
		comps = sanatizeDataInstr(line);
		
		Instruction toReturn = new Instruction();
		InstructionsS230 instr = InstructionsS230.valueOf(comps.get(0));
		//now this is a long switch lol;
		switch(instr) {
			case add:
					toReturn = new ADD(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					toReturn.setNextComponent(comps.get(2));
					toReturn.setNextComponent(comps.get(3));
					break;
			case and:
					toReturn = new AND(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					toReturn.setNextComponent(comps.get(2));
					toReturn.setNextComponent(comps.get(3));
					break;
			case or:
					toReturn = new OR(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					toReturn.setNextComponent(comps.get(2));
					toReturn.setNextComponent(comps.get(3));
					break;
			case xor:
					toReturn = new XOR(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					toReturn.setNextComponent(comps.get(2));
					toReturn.setNextComponent(comps.get(3));
					break;
			case addi:
					toReturn = new ADDI(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					toReturn.setNextComponent(comps.get(2));
					toReturn.setNextComponent(comps.get(3));
					break;
			case lw:
					toReturn = new LW(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					toReturn.setNextComponent(getAdReg(comps.get(2)));
					toReturn.setNextComponent(getAdImmd(comps.get(2)));
					break;
			case sw:
					toReturn = new SW(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					toReturn.setNextComponent(getAdReg(comps.get(2)));
					toReturn.setNextComponent(getAdImmd(comps.get(2)));
					break;
			case jr:
					toReturn = new JR(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					break;
			case cmp:
					toReturn = new CMP(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					toReturn.setNextComponent(comps.get(2));
					break;
			case b:
					toReturn = new B(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					break;
			case bal:
					toReturn = new BAL(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					break;
			case j:
					toReturn = new J(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					break;
			case jal:
					toReturn = new JAL(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					break;
			case li:
					toReturn = new LI(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					toReturn.setNextComponent(comps.get(2));
					break;
			case blt:
					toReturn = new BLT(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					break;
			case beq: 
					toReturn = new BEQ(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					break;
			case bne:
					toReturn = new BNE(comps.get(0));
					toReturn.setNextComponent(comps.get(1));
					break;
		}
		
		if(toReturn.getName().equals("")){
			throw new RuntimeException("getInstruction("+line+") was not found!\n"
					+"Instruction name is:("+comps.get(0)+").");
		}
		
		//only returns if the instruction was found
		return toReturn;
	}
	
}
