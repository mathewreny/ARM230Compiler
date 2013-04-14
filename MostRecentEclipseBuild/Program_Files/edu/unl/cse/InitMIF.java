package edu.unl.cse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class InitMIF {

	private ArrayList<String> argList = new ArrayList<String>();
	
	public InitMIF(String[] args){
		for(int i = 0; i<args.length; i++){
			String arg = args[i];
			this.argList.add(arg);
			//System.out.println("added "+arg);
		}
	}
	
	public PrintWriter getPrintWriter(){
	  if(Compiler.isWindows()){
		String filename = "";
		for(int i = 0; i<argList.size(); i++ ){
			if(argList.get(i).equals("-f")){
				filename = argList.get(i+1);
				//DIFFERENT FROM OTHER INIT FILE!
				filename = filename.trim().replace(".s230", ".mif");
				filename = filename.trim().replace(".S230", ".mif");
			}
		}
		System.out.println(System.getProperty("user.dir") + " ... " + filename);
		String dirName = System.getProperty("user.dir");
		dirName = dirName.replace("\\Program_Files\\source","\\").trim();
		dirName = dirName.replace("\\Program_Files\\classes","\\").trim();
		dirName = dirName.concat("\\InputOutputFolder\\");
		System.out.println(dirName+filename);
		File file = new File(dirName, filename);
		try{
			return new PrintWriter(file);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
	  } else {
		String filename = "";
		for(int i = 0; i<argList.size(); i++ ){
			if(argList.get(i).equals("-f")){
				filename = argList.get(i+1);
				//DIFFERENT FROM OTHER INIT FILE!
				filename = filename.trim().replace(".s230", ".mif");
				filename = filename.trim().replace(".S230", ".mif");
				filename = "MemoryInitialization" + filename;
			}
		}
		System.out.println(System.getProperty("user.dir") + " ... " + filename);
		String dirName = System.getProperty("user.dir");
		dirName = dirName.replace("/Program_Files/source","/").trim();
		dirName = dirName.replace("/Program_Files/classes","/").trim();
		dirName = dirName.concat("/InputOutputFolder/");
		System.out.println(dirName+filename);
		File file = new File(dirName, filename);
		try{
			return new PrintWriter(file);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
	  }
	  return null;
	}
}