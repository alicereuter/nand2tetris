import java.io.File;
import java.util.*;

public class Parser {
    Scanner scan;
    String s = "";
    public Parser(String s){
	try {
	    scan = new Scanner(new File(s));
	 	}
	catch (Exception e){
	    e.printStackTrace();
	}
    }

    public Parser(File f){
	try {
	    scan = new Scanner(f);
	}catch (Exception e){
	    e.printStackTrace();
	}
    }
    
    public String readNextLine(){
	return scan.nextLine();
    }

    public String getName(){
	return s;
    }
    
    public void advance(){
	s =readNextLine();
    }

    public boolean hasMoreCommands(){
	return scan.hasNextLine();
    }
    //tries to parse arg1
    public String arg1(){
	String command = commandType();
	if(command.equals("C_ARTHIMETIC")){
	    return s; 
	}
	if(command.equals("UNKNOWN")){
	    return "UNKOWN";
	}
	return parseArg1(1,s);
    }

    
    // parser for push and pop memory segments
    private String parseArg1(int off,String s){
	return s.split(" ")[1];

    }
    
    //returns second argument and removes commments
    public int arg2(){
        String i = s.split(" ")[2];
	i = i.replaceAll("( |\n|\t)","");
	return Integer.parseInt(i);

    }
	    // Returns command type
    public String commandType(){
	String[] arth = {"add","sub","neg","eq","gt","lt","and","or","not"};

	if(s.length() > 4){
	    if(s.substring(0,3).equals("pop")){
		return "C_POP";
	    }
	    if(s.substring(0,4).equals("push")){
		return "C_PUSH";
	    }
	}
	if(s.length() >= 4){
	    if(s.substring(0,5).equals("label")){
		return "C_LABEL";
	    }
	}
	if(s.length() > 6){
	    if(s.substring(0,7).equals("if-goto")){
		return "C_IF";
	    }
	}
       if(s.length() > 5){
	    if(s.substring(0,4).equals("goto")){
		return "C_GOTO";
	    }
	}
       if(s.length() >9){
	   if(s.substring(0,8).equals("function")){
	       return "C_FUNCTION";
	   }
       }
       if(s.length() > 5){
	   if(s.substring(0,4).equals("call")){
	       return "C_CALL";
	   }
       }
       if(s.length() > 5){
	   if(s.substring(0,6).equals("return")){
	       return "C_RETURN";
	   }
       }
       	System.out.println(s);
	if(s.length () > 2){
	for(int i = 0; i< arth.length; i++){
		//comparision bs arthmitec index
	    int index = arth[i].length();
	    if(arth[i].substring(0,index).equals(s.substring(0,index))){
		    s =arth[i].substring(0,arth[i].length());
		    return "C_ARTHIMETIC";
		}
	}
	}

 	
	return "Unkown";
    }
}
