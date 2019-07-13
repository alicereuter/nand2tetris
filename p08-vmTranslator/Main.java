import java.io.File;
import java.io.FilenameFilter;


public class Main {
    public static void main(String [] args){
	if(isFile(args[0])){
		Parser f = new Parser(args[0]);
		String file = args[0].substring(0,args[0].length() -3);
		CodeWriter c = new CodeWriter(file+".asm");
		//initializes code
		c.writeInit();
		parseFile(f,c);
	}else{
	    //get output asm file path
	    String[] splits = args[0].split("/");
	    String folderName = splits[splits.length -1];   
	    CodeWriter c = new CodeWriter(args[0] + folderName + ".asm");
	    //initializes code
	    c.writeInit();
	    //get vm files
	    File[] vms = files(args[0]);
	    for(File f:vms){
		//sets name
		String file =f.getName();
		c.setFileName(file);
		Parser p = new Parser(f);
		parseFile(p,c);
	    }
	}
		
    }
    // matches type with writer function
    public static void parseFile(Parser p, CodeWriter c){
	while(p.hasMoreCommands()){
	    p.advance();
	    String type = p.commandType();
	    System.out.println(type);
	    if(type.equals("C_ARTHIMETIC")){
		c.writeArithmetic(p.getName());
	    }
	    
	    else if(type.equals("C_POP") || type.equals("C_PUSH")){
		String arg1 = p.arg1();
		int i = p.arg2();
		c.writePushPop(p.commandType(),arg1,i);
	    }
	    if(type.equals("C_LABEL")){
		String arg1 = p.arg1();
		c.writeLabel(arg1);
	    }
	    if(type.equals("C_IF")){
		String arg1 = p.arg1();
		c.writeIf(arg1);
	    }
	    if(type.equals("C_GOTO")){
		String arg1 = p.arg1();
		c.writeGoTo(arg1);
	    }
	    if(type.equals("C_FUNCTION")){
		String arg1 = p.arg1();
		int i = p.arg2();
		c.writeFunction(arg1,i);
	    }
	    if(type.equals("C_RETURN")){
		c.writeReturn();
	    }
	    if(type.equals("C_CALL")){
		String arg1 = p.arg1();
		int i = p.arg2();
		c.writeCall(arg1,i);
	    }
	}
    }
    //checks if file exists
    public static boolean isFile(String s){
	File f = null;
	f = new File(s);
	return f.isFile();
    }


    //curetsesy of https://stackoverflow.com/questions/1384947/java-find-txt-files-in-specified-folder
    public static File[] files(String s){
	File dir = new File(s);

        return dir.listFiles(new FilenameFilter() { 
                 public boolean accept(File dir, String filename)
                      { return filename.endsWith(".vm"); }
        } );
    }
}
