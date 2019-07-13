import java.io.File;
import java.util.*;

public class Parser {
    //scanner
    Scanner scan;
    //string that holds current command
    String s = "";
    public Parser(String s){
	try {
	    //initializes scanner
	    scan = new Scanner(new File(s));
	 	}
	catch (Exception e){
	    e.printStackTrace();
	}
    }

    public Parser(File f){
	try {
	    //initialisez scanner
	    scan = new Scanner(f);
	} catch (Exception e){
	}
    }
    //returns nextline
    public String readNextLine(){
	return scan.nextLine();
    }
    
    //returns current command parser is on
    public String getName(){
	return s;
    }

    //move parser on to next line
    public void advance(){
	s =readNextLine();
    }

    //returns false if no more commands
    public boolean hasMoreCommands(){
	return scan.hasNextLine();
    }

    //returns first argument of command
    public String arg1(){
	String command = commandType();
	//drops front half of arg based off type
	if(command.equals("C_POP")){
	    return parseArg1(4,s);
	}
	//drops front half of arg based off type 
	if(command.equals("C_PUSH")){
	    return parseArg1(5,s);
	}
	//parses type of command
	if(command.equals("C_ARTHIMETIC")){
	    return s; 
	}
	
	return "UNKown";

    }

    
    // parser for push and pop memory segments
    private String parseArg1(int off,String s){
	return s.split(" ")[1];

    }
    //returns num for push pop instructions
    public int arg2(){
	//parses second arg
        String i = s.split(" ")[2];
	//converts to int
	return Integer.parseInt(i);

    }
	    // Returns command type
    public String commandType(){
	//arrays of command types
	String[] arth = {"add","sub","neg","eq","gt","lt","and","or","not"};
	//checks if push or pop
	if(s.length() > 4){
	    if(s.substring(0,3).equals("pop")){
		return "C_POP";
	    }
	    if(s.substring(0,4).equals("push")){
		return "C_PUSH";
	    }
	}
	//iterates through different arithmetic types
	for(int i = 0; i< arth.length; i++){
		//comparision bs arthmitec index
		if(arth[i].substring(0,arth[i].length()).equals(s)){
		    return "C_ARTHIMETIC";
		}
	}
	
	return "Unkown";
    }
}
