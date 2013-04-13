package code;

/*************************************** 
 ***************************************
 **  Created By Mathew Reny 4/9/2013   **
 ** contact me at mhreny@gmail.com	  **
 ***************************************
 ***************************************
 *
 *	Compiles assembly code for the CSCE 230 Processor into ARM instructions. 
 *	instructions should have spaces between different components.
 *	DO NOT use commas to separate the registers
 *	Use of PC, and RA (return address) is not supported. It is easy to implement though.
 *	
 *** HOW TO COMPILE: ***
 *	in the command line type 
 *	$> javac Compiler.java
 *	
 *** HOW TO USE: ***
 *  If you wanted to add r5 and r13 and save into r2 type the following
 *  $> java Compiler add r2 r5 r13
 *  
 *  If you want to use lw and put the value into r2. Mem address is in r5. offset=4
 *  $> java Compiler lw r2 "(4)r5"
 *  the quotations are needed here due to the linux command line not liking parenthesis
 *  
 *  
 *** HOW TO SET BIT: ***
 *  Simply add a -s at the end of the instruction.
 *  EXCEPT FOR CMP!
 *  $> java Compiler **INSTRUCTION** -s
 *  
 *  
 *** HOW TO USE CONDITIONS ***
 * Conditions follow the format 0000, 0001, ..., 1110.
 * $> java Compiler b 67 -c (4-bit condition)
 *
 *  Thanks for reading.
*/
public class Compiler{

             //As we move along in our project we should update this
			//So far these instructions are supported.  Feel free to add more.
             public enum Instruction {
                  add, and, or, xor, addi, lw, sw, jr, cmp, b, bal, j, jal, li, blt, beq, bne;
             }

		 
/*		 public String toBinaryStringL(int input, int totLength){
		 			boolean negative = false;	
					
					if(input < 0)
						negative = true;
						
					if(negative)
						input -= 1;
							
					String toReturn = Integer.toBinaryString(input));
					
					if(negative){
						string negativeToReturn = "";
						for(char ch: toReturn.toCharArray()){
							if(ch == '1')
								negativeToReturn = negativeToReturn.concat("0");
							else
								negativeToReturn = negativeToReturn.concat("1");
						}
					}
	            
					if(negative){
						for(int i = (totLength-toReturn.length()); i > 0; i--){
	              		toReturn = toReturn.concat("1");
	           		}
					} else {
						for(int i = (totLength-toReturn.length()); i > 0; i--){
	              		toReturn = toReturn.concat("0");
	           		}
					}
		 }
*/

        public static void main(String[] args){

                //Make input compatible with Enum valueOf
                String instrS = args[0].toLowerCase();
                //Allows the string to be converted into an Enum
                Instruction instr = Instruction.valueOf(instrS);

                //These are used in the switch below. 
                String toReturn = "";
                String RA, RB, RD, IMMD, RAreg = "";
                
                //Used switch because it is so much nicer to look at. Seriously.
                switch (instr) {
                
                //If it is here... IT WORKS (according to my testing)!
                case add:
                        //Op Code
                        toReturn = toReturn.concat("1010");
                        //Condition
                        toReturn = toReturn.concat("0000");
                        //RA
                        RA = args[2].toLowerCase().substring(1);
                        RA = Integer.toBinaryString(Integer.parseInt(RA));
                        //RA = String.format("%04d", RA);
                        if(RA.length()==3)
                                toReturn = toReturn.concat("0");
                        else if (RA.length()==2)
                                toReturn = toReturn.concat("00");
                        else if (RA.length()==1)
                                toReturn = toReturn.concat("000");

                        toReturn = toReturn.concat(RA);


                         //RB
                        RB = args[3].toLowerCase().substring(1);
                        RB = Integer.toBinaryString(Integer.parseInt(RB));
                        //RA = String.format("%04d", RA);
                        if(RB.length()==3)
                                toReturn = toReturn.concat("0");
                        else if (RB.length()==2)
                                toReturn = toReturn.concat("00");
                        else if (RB.length()==1)
                                toReturn = toReturn.concat("000");

                        toReturn = toReturn.concat(RB);


                         //RD
                        RD = args[1].toLowerCase().substring(1);
                        RD = Integer.toBinaryString(Integer.parseInt(RD));
                        //RA = String.format("%04d", RA);
                        if(RD.length()==3)
                                toReturn = toReturn.concat("0");
                        else if (RD.length()==2)
                                toReturn = toReturn.concat("00");
                        else if (RD.length()==1)
                                toReturn = toReturn.concat("000");

                        toReturn = toReturn.concat(RD);

                        toReturn = toReturn.concat("100");

                        if(args.length == 5){
                                if(args[4].equals("-s"))
                                        toReturn = toReturn.concat("1");
                        } else
                                toReturn = toReturn.concat("0");


                        System.out.println(toReturn);
                        break;
                case and:
	                    //Op Code
	                    toReturn = toReturn.concat("1010");
	                    //Condition
	                    toReturn = toReturn.concat("0000");
	                    //RA
	                    RA = args[2].toLowerCase().substring(1);
	                    RA = Integer.toBinaryString(Integer.parseInt(RA));
	                    //RA = String.format("%04d", RA);
	                    if(RA.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RA.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RA.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RA);
	
	
	                     //RB
	                    RB = args[3].toLowerCase().substring(1);
	                    RB = Integer.toBinaryString(Integer.parseInt(RB));
	                    //RA = String.format("%04d", RA);
	                    if(RB.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RB.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RB.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RB);
	
	
	                     //RD
	                    RD = args[1].toLowerCase().substring(1);
	                    RD = Integer.toBinaryString(Integer.parseInt(RD));
	                    //RA = String.format("%04d", RA);
	                    if(RD.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RD.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RD.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RD);
	
	                    toReturn = toReturn.concat("010");
	
	                    if(args.length == 5){
	                            if(args[4].equals("-s"))
	                                    toReturn = toReturn.concat("1");
	                    } else
	                            toReturn = toReturn.concat("0");
	
	
	                    System.out.println(toReturn);
                      break;
                
                case or:
	                    //Op Code
	                    toReturn = toReturn.concat("1010");
	                    //Condition
	                    toReturn = toReturn.concat("0000");
	                    //RA
	                    RA = args[2].toLowerCase().substring(1);
	                    RA = Integer.toBinaryString(Integer.parseInt(RA));
	                    //RA = String.format("%04d", RA);
	                    if(RA.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RA.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RA.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RA);
	
	
	                     //RB
	                    RB = args[3].toLowerCase().substring(1);
	                    RB = Integer.toBinaryString(Integer.parseInt(RB));
	                    //RA = String.format("%04d", RA);
	                    if(RB.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RB.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RB.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RB);
	
	
	                     //RD
	                    RD = args[1].toLowerCase().substring(1);
	                    RD = Integer.toBinaryString(Integer.parseInt(RD));
	                    //RA = String.format("%04d", RA);
	                    if(RD.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RD.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RD.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RD);
	
	                    toReturn = toReturn.concat("011");
	
	                    if(args.length == 5){
	                            if(args[4].equals("-s"))
	                                    toReturn = toReturn.concat("1");
	                    } else
	                            toReturn = toReturn.concat("0");
	
	
	                    System.out.println(toReturn);
	                    break;
                case xor:
	                    //Op Code
	                    toReturn = toReturn.concat("1010");
	                    //Condition
	                    toReturn = toReturn.concat("0000");
	                    //RA
	                    RA = args[2].toLowerCase().substring(1);
	                    RA = Integer.toBinaryString(Integer.parseInt(RA));
	                    //RA = String.format("%04d", RA);
	                    if(RA.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RA.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RA.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RA);
	
	
	                     //RB
	                    RB = args[3].toLowerCase().substring(1);
	                    RB = Integer.toBinaryString(Integer.parseInt(RB));
	                    //RA = String.format("%04d", RA);
	                    if(RB.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RB.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RB.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RB);
	
	
	                     //RD
	                    RD = args[1].toLowerCase().substring(1);
	                    RD = Integer.toBinaryString(Integer.parseInt(RD));
	                    //RA = String.format("%04d", RA);
	                    if(RD.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RD.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RD.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RD);
	
	                    toReturn = toReturn.concat("001");
	
	                    if(args.length == 5){
	                            if(args[4].equals("-s"))
	                                    toReturn = toReturn.concat("1");
	                    } else
	                            toReturn = toReturn.concat("0");
	
	
	                    System.out.println(toReturn);
	                    break;
                case addi:
	                  	//Op Code
	                    toReturn = toReturn.concat("1100");
	                    //Condition
	                    toReturn = toReturn.concat("1110");
	                    
	                    //RA
	                    RA = args[2].toLowerCase().substring(1);
	                    RA = Integer.toBinaryString(Integer.parseInt(RA));
	                    //RA = String.format("%04d", RA);
	                    if(RA.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RA.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RA.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RA);
	
	                    //RD
	                    RD = args[1].toLowerCase().substring(1);
	                    RD = Integer.toBinaryString(Integer.parseInt(RD));
	                    //RA = String.format("%04d", RA);
	                    if(RD.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RD.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RD.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RD);
	                    
	                    //IMMD
	                    IMMD = Integer.toBinaryString(Integer.parseInt(args[3]));
	                    for(int i = (7-IMMD.length()); i > 0; i--){
	                    	toReturn = toReturn.concat("0");
	                    }
	                    toReturn = toReturn.concat(IMMD);
	                    
	                    if(args.length == 5){
	                    	if(args[4].equals("-s"))
	                                toReturn = toReturn.concat("1");
	                    } else
	                  	toReturn = toReturn.concat("0");
	
	                    System.out.println(toReturn);
	                  	break;
                case cmp:
	                	  //Op Code
	                    toReturn = toReturn.concat("1110");
	                    //Condition
	                    toReturn = toReturn.concat("1110");
	                    
	                    //RA
	                    RA = args[1].toLowerCase().substring(1);
	                    RA = Integer.toBinaryString(Integer.parseInt(RA));
	                    //RA = String.format("%04d", RA);
	                    if(RA.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RA.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RA.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RA);
	                    
	                    //RB
	                    RB = args[2].toLowerCase().substring(1);
	                    RB = Integer.toBinaryString(Integer.parseInt(RB));
	                    //RA = String.format("%04d", RA);
	                    if(RB.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RB.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RB.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RB);
	                    
	                    toReturn = toReturn.concat("00000001");
	
	                    System.out.println(toReturn);
	                    break;
                case lw: 
	                  	//Op Code
	                    toReturn = toReturn.concat("1000");
	                    //Condition
	                    toReturn = toReturn.concat("1110");
	                    
	                    //RA
	                    RA = args[2].toLowerCase();
	                    RAreg= RA.substring(1+RA.indexOf('r'));
	                    RAreg = Integer.toBinaryString(Integer.parseInt(RAreg));
	                    //RA = String.format("%04d", RA);
	                    if(RAreg.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RAreg.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RAreg.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RAreg);
	                    
	                    //RD
	                    RD = args[1].toLowerCase().substring(1);
	                    RD = Integer.toBinaryString(Integer.parseInt(RD));
	                    //RA = String.format("%04d", RA);
	                    if(RD.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RD.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RD.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RD);
	                    
	                    IMMD = RA.substring(1, RA.indexOf(')'));
	                    IMMD = Integer.toBinaryString(Integer.parseInt(IMMD));
	                    for(int i = (7-IMMD.length()); i > 0; i--){
	                    	toReturn = toReturn.concat("0");
	                    }
	                    toReturn = toReturn.concat(IMMD);
	                  
	                    if(args.length == 4){
	                    	if(args[3].equals("-s"))
	                                toReturn = toReturn.concat("1");
	                    } else
	                  	toReturn = toReturn.concat("0");
	
	                    System.out.println(toReturn);
	                	  break;
                case sw:
	                	  //Op Code
	                    toReturn = toReturn.concat("1001");
	                    //Condition
	                    toReturn = toReturn.concat("1110");
	                    
	                    //RA
	                    RA = args[2].toLowerCase();
	                    RAreg= RA.substring(1+RA.indexOf('r'));
	                    RAreg = Integer.toBinaryString(Integer.parseInt(RAreg));
	                    //RA = String.format("%04d", RA);
	                    if(RAreg.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RAreg.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RAreg.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RAreg);
	                    
	                    //RD
	                    RD = args[1].toLowerCase().substring(1);
	                    RD = Integer.toBinaryString(Integer.parseInt(RD));
	                    //RA = String.format("%04d", RA);
	                    if(RD.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RD.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RD.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RD);
	                    
	                    IMMD = RA.substring(1, RA.indexOf(')'));
	                    IMMD = Integer.toBinaryString(Integer.parseInt(IMMD));
	                    for(int i = (7-IMMD.length()); i > 0; i--){
	                    	toReturn = toReturn.concat("0");
	                    }
	                    toReturn = toReturn.concat(IMMD);
	                  
	                    if(args.length == 4){
	                    	if(args[3].equals("-s"))
	                                toReturn = toReturn.concat("1");
	                    } else
	                	  toReturn = toReturn.concat("0");
	
	                    System.out.println(toReturn);
	                	break;
                case jr: 
                		  toReturn = toReturn.concat("1111000000000000");
                		
                		  //RD
	                    RD = args[1].toLowerCase().substring(1);
	                    RD = Integer.toBinaryString(Integer.parseInt(RD));
	                    //RA = String.format("%04d", RA);
	                    if(RD.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RD.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RD.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RD + "0000");
	                    System.out.println(toReturn);
	                    break;
                case b:	
                		  toReturn = toReturn.concat("0000");
                		  if (args.length == 4){
                			  if (args[2].equals("-c"))
                				  toReturn = toReturn.concat(args[3]);
                		  } else
                			toReturn = toReturn.concat("1110");
                		
                		
                		  IMMD = Integer.toBinaryString(Integer.parseInt(args[1]));
	                    for(int i = (16-IMMD.length()); i > 0; i--){
	                    	toReturn = toReturn.concat("0");
	                    }
	                    toReturn = toReturn.concat(IMMD);
	                    System.out.println(toReturn);
	                    break;
					case blt:	
                		  toReturn = toReturn.concat("0000");
                		  toReturn = toReturn.concat("1101");
                		
                		
                		  IMMD = Integer.toBinaryString(Integer.parseInt(args[1]));
	                    for(int i = (16-IMMD.length()); i > 0; i--){
	                    	toReturn = toReturn.concat("0");
	                    }
	                    toReturn = toReturn.concat(IMMD);
	                    System.out.println(toReturn);
	                    break;
					case beq:	
                		  toReturn = toReturn.concat("0000");
                		  toReturn = toReturn.concat("0000");
                		
                		
                		  IMMD = Integer.toBinaryString(Integer.parseInt(args[1]));
	                    for(int i = (16-IMMD.length()); i > 0; i--){
	                    	toReturn = toReturn.concat("0");
	                    }
	                    toReturn = toReturn.concat(IMMD);
	                    System.out.println(toReturn);
	                    break;
					 case bne:	
                		  toReturn = toReturn.concat("0000");
                		  toReturn = toReturn.concat("0001");
                		
                		
                		  IMMD = Integer.toBinaryString(Integer.parseInt(args[1]));
	                    for(int i = (16-IMMD.length()); i > 0; i--){
	                    	toReturn = toReturn.concat("0");
	                    }
	                    toReturn = toReturn.concat(IMMD);
	                    System.out.println(toReturn);
	                    break;
                case bal:
	                		
	                	toReturn = toReturn.concat("0100");
	                	
                		  if (args.length == 4){
                			  if (args[2].equals("-c"))
                				  toReturn = toReturn.concat(args[3]);
                		  } else
                			toReturn = toReturn.concat("1110");
	            		
	            		    IMMD = Integer.toBinaryString(Integer.parseInt(args[1]));
	                    for(int i = (16-IMMD.length()); i > 0; i--){
	                    	toReturn = toReturn.concat("0");
	                    }
	                    toReturn = toReturn.concat(IMMD);
	                    System.out.println(toReturn);
	                    break;    
                case j:
                		  toReturn = toReturn.concat("0010");
                		
                		  IMMD = Integer.toBinaryString(Integer.parseInt(args[1]));
	                    for(int i = (20-IMMD.length()); i > 0; i--){
	                    	toReturn = toReturn.concat("0");
	                    }
	                    toReturn = toReturn.concat(IMMD);
	                    System.out.println(toReturn);
	                    break; 
                case jal:
	                	  toReturn = toReturn.concat("0110");
	            		
	            		
	            		    IMMD = Integer.toBinaryString(Integer.parseInt(args[1]));
	                    for(int i = (20-IMMD.length()); i > 0; i--){
	                    	toReturn = toReturn.concat("0");
	                    }
	                    toReturn = toReturn.concat(IMMD);
	                    System.out.println(toReturn);
	                    break; 
                case li: 
	                	  toReturn = toReturn.concat("0011");
	            		
	                	//RA
	                    RA = args[1].toLowerCase().substring(1);
	                    RA = Integer.toBinaryString(Integer.parseInt(RA));
	                    //RA = String.format("%04d", RA);
	                    if(RA.length()==3)
	                            toReturn = toReturn.concat("0");
	                    else if (RA.length()==2)
	                            toReturn = toReturn.concat("00");
	                    else if (RA.length()==1)
	                            toReturn = toReturn.concat("000");
	
	                    toReturn = toReturn.concat(RA);
	                    
	            		    IMMD = Integer.toBinaryString(Integer.parseInt(args[2]));
	                    for(int i = (16-IMMD.length()); i > 0; i--){
	                    	toReturn = toReturn.concat("0");
	                    }
	                    toReturn = toReturn.concat(IMMD);
	                    System.out.println(toReturn);
	                    break;	
                }
        }
}