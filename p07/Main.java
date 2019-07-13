import java.io.*;

public class Main {
    public static void main(String [] args){
	//strips trailing .vm from file name
	String fileStem = args[0].substring(0,args[0].length() -3);
	//checks if is dir
	if(file(args[0])){
	    //creates parser
	    Parser f = new Parser(args[0]);
	    //writes to filestem .asm
	    CodeWriter c = new CodeWriter(fileStem+".asm");
	    //has more commmands
	    while(f.hasMoreCommands()){
		//advances the parser to the next command
		f.advance();
		String type = f.commandType();
		//handles arithmetic
		if(type.equals("C_ARTHIMETIC")){
		    c.writeArithmetic(f.getName());
		}
		else if(type.equals("C_POP") || type.equals("C_PUSH")){
		    //finds memory location
		    String arg1 = f.arg1();
		    //finds index
		    int i = f.arg2();
		    c.writePushPop(f.commandType(),arg1,i);
		}
	    }
	}else{
	    //find files that end with .vm cutorsy of https://stackoverflow.com/questions/1384947/java-find-txt-files-in-specified-folder
	    File[] vmfiles = new File(args[0]+"/").listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String filename)
		    { return filename.endsWith(".vm"); }
		} );
	    //split input path
	    String[] out = args[0].split("/");
	    //determine index of directory name
	    String stem = out[out.length -1];
	    // create code writer with right output path
	    CodeWriter c = new CodeWriter(args[0] + stem +".asm");
	    //iterate thorugh files
	    for(File f: vmfiles){
		// construct parser for each file
		Parser p = new Parser(f);
		//run parser
		while(p.hasMoreCommands()){
		    //advances parser
			p.advance();
			String type = p.commandType();
			if(type.equals("C_ARTHIMETIC")){
			    c.writeArithmetic(f.getName());
			}
			//handles push and pop instruction
			else if(type.equals("C_POP") || type.equals("C_PUSH")){
			    String arg1 = p.arg1();
			    int i = p.arg2();
			    c.writePushPop(p.commandType(),arg1,i);
			}
		}
	    }
	}
    }
    //checks if a file is a directory
    public static Boolean file(String s){
	File f = new File(s);
	return f.isFile();
    }
}
