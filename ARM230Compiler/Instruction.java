package ARM230Compiler;

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
	
	public String toS230Comment(){
		return "";
	}
	
	public String toS230(){
		return "THIS ISN'T IMPLEMENTED!";
	}
	
}
