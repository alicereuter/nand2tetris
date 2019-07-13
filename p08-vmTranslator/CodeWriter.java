import java.io.*;

public class CodeWriter{
    public static int x =0;
    public String file  = "FILE";
    public PrintWriter out = null;

	
    public CodeWriter(String f){
	file = f;
	try {
	    out = new PrintWriter(new File(f));
	} catch (Exception e){
	    e.printStackTrace();
	}
    }

    //sets the filename
    public void setFileName(String s){
	file = s;
    }

    //writes arithmetic
    public void writeArithmetic(String s){
	out.write(writeArithmetic1(s) +"\n");
	out.flush();
    }
    //write arithmetic
    public String writeArithmetic1(String s){
	if(s.equals("add")){
	    String str = "@0\n" +
		"D=M\n"+
		"A=D-1\n" +
		"D=M\n"+
		"A=A-1\n"+
		"D=D+M\n"+
		"M=D\n"+
		"@0\n"+
		"M=M-1";
	    return str;
	}
	if(s.equals("sub")){
	    String str = "@0\n" +
		"D=M\n"+
		"A=D-1\n" +
		"D=M\n"+
		"A=A-1\n"+
		"D=M-D\n"+
		"M=D\n"+
		"@0\n"+
		"M=M-1";
	    return str;
	}
	if(s.equals("and")){
	    return "@0\nD=M\nA=D-1\nD=M\nA=A-1\nD=D&M\nM=D\n@0\nM=M-1";
	}
	if(s.equals("or")){
	    return "@0\nD=M\nA=D-1\nD=M\nA=A-1\nD=D|M\nM=D\n@0\nM=M-1";
	}
	
	if(s.equals("eq")){
	    x++;
	    String str = "@0\n";
	    str = str + "D=M\n";
	    str = str + "A=D-1\n";
	    str = str + "D=M\n";
	    str = str + "A=A-1\n";
	    str = str + "D=D-M\n";
	    str = str + "@false"+x +"\n";
	    str = str + "D;JEQ\n";
	    str = str + "@0\n";
	    str = str + "M=M-1\n";
	    str = str + "A=M-1\n";
	    str = str + "M=0\n";
	    str = str + "@end" +x + "\n";
	    str = str + "0;JMP\n";
	    str = str + "(false" + x+")\n";
	    str = str + "@0\n";
	    str = str + "M=M-1\n";
	    str = str + "A=M-1\n";
	    str = str + "M=-1\n";
	    str = str + "(end" + x+ ")\n";
       	    return str;
	}

	if(s.equals("lt")){
	     x++;
	    String str = "@0\n";
	    str = str + "D=M\n";
	    str = str + "A=D-1\n";
	    str = str + "D=M\n";
	    str = str + "A=A-1\n";
	    str = str + "D=D-M\n";
	    str = str + "@false"+x +"\n";
	    str = str + "D;JGT\n";
	    str = str + "@0\n";
	    str = str + "M=M-1\n";
	    str = str + "A=M-1\n";
	    str = str + "M=0\n";
	    str = str + "@end" +x + "\n";
	    str = str + "0;JMP\n";
	    str = str + "(false" + x+")\n";
	    str = str + "@0\n";
	    str = str + "M=M-1\n";
	    str = str + "A=M-1\n";
	    str = str + "M=-1\n";
	    str = str + "(end" + x+ ")\n";
       	    return str;
      	}
	
	if(s.equals("gt")){
	    x++;
	    String str = "@0\n";
	    str = str + "D=M\n";
	    str = str + "A=D-1\n";
	    str = str + "D=M\n";
	    str = str + "A=A-1\n";
	    str = str + "D=D-M\n";
	    str = str + "@false"+x +"\n";
	    str = str + "D;JLT\n";
	    str = str + "@0\n";
	    str = str + "M=M-1\n";
	    str = str + "A=M-1\n";
	    str = str + "M=0\n";
	    str = str + "@end" +x + "\n";
	    str = str + "0;JMP\n";
	    str = str + "(false" + x+")\n";
	    str = str + "@0\n";
	    str = str + "M=M-1\n";
	    str = str n+ "A=M-1\n";
	    str = str + "M=-1\n";
	    str = str + "(end" + x+ ")\n";
       	    return str;
	}
	if(s.equals("not")){
	    return "@0\nA=M-1\nM=!M\n";
	}
	if(s.equals("neg")){
	    return "@0\nA=M-1\nM=-M\n";
	}
	return "UNKOWN";
    }


    //wrapper for push and pop
    public void writePushPop(String q,String s,int in){
    	out.write(writePushPop1(q,s,in) +"\n");
	out.flush();
    }

    
    //bootstrap call sys init
    public void writeInit(){
	String write = "//sys init\n" +
	    "@256\n" +
	    "D=A\n" +
	    "@SP\n" +
	    "M=D\n";
	out.write(write);
	out.flush();
	writeCall("Sys.init",0);
    }

    //writes labels
    public void writeLabel(String label){
	String write = "//label " + label + "\n"
	    + "(" + label + ")\n";
	out.write(write);
	out.flush();
    }

    //writeGoTo label
    public void writeGoTo(String label){
	String write = "//write go to " + label + "\n" +
      	    "@" + label + "\n" +
	    "0;JMP\n";
	out.write(write);
	out.flush();

    }

    //jumps to end if false else jumps to label
    public void writeIf(String label){
		x++;
	String write = "//write if go to " + label + "\n" +
	    "@SP\n" +
	    "M=M-1\n" +
	    "A=M\n" +
	    "D=M\n" +
	    "@goto" + x + "\n" +
	    "D;JEQ\n" +
	    "@" + label + "\n" +
	    "0;JMP\n" +
	    "(goto"+ x+ ")\n\n";
	out.write(write);
	out.flush();

    }

    //callsa a function
    public void writeCall(String fName, int nArgs){
	x++;
	String s ="//call " +fName + " " + nArgs +"\n" +
	    //push ret addre
	    "@retaddr" +x +"\n" +
	    "D=A\n" +
	    "@SP\n" +
	    "A=M\n" +
	    "M=D\n" +
	    "@SP\n" +
	    "M=M+1\n" +
	    //push lcl
	    "@LCL\n" +
	    "D=M\n" +
	    "@SP\n" +
	    "A=M\n" +
	    "M=D\n" +
	    "@SP\n" +
	    "M=M+1\n" +
	    //push arg
	    "@ARG\n" +
	    "D=M\n" +
	    "@SP\n" +
	    "A=M\n" +
	    "M=D\n" +
	    "@SP\n" +
	    "M=M+1\n" +

	    //push this
	    "@THIS\n" +
	    "D=M\n" +
	    "@SP\n" +
	    "A=M\n" +
	    "M=D\n" +
	    "@SP\n" +
	    "M=M+1\n" +

	    //push that
	    "@THAT\n" +
	    "D=M\n" +
	    "@SP\n" +
	    "A=M\n" +
	    "M=D\n" +
	    "@SP\n" +
	    "M=M+1\n" +

	    //arg = sp -n -5
	    "@SP\n" +
	    "D=M\n" +
	    "@" + nArgs + "\n" +
	    "D=D-A\n" +
	    "@5\n" +
	    "D=D-A\n" +
	    "@ARG\n" +
	    "M=D\n" +

	    //LCL = SP
	    "@SP\n" +
	    "D=M\n" +
	    "@LCL\n" +
	    "M=D\n";
	out.write(s);
	out.flush();
	writeGoTo(fName);
	String label = "(retaddr" + x + ")\n";
	out.write(label);
	out.flush();
        
    }

    //writes returns code
    public void writeReturn(){
	String ret = "//function return \n" +
	    //frame
	    "@LCL\n" +
	    "D=M\n" +
	    "@R13\n" +
	    "M=D\n" +
	    //return
	    "@5\n" +
	    "D=A\n" +
	    "@R13\n" +
	    "D=M-D\n" +
	    "A=D\n" +
	    "D=M\n" +
	    "@R14\n" +
	    "M=D\n" +
	    
	    // * ARg = Pop()
	    "@SP\n" +
	    "A=M-1\n" +
	    "D=M\n" +
	    "@ARG\n" +
	    "A=M\n" +
	    "M=D\n" +
	    // SP
	    "@ARG\n" +
	    "D=M\n" +
	    "D=D+1\n" +
	    "@SP\n" +
	    "M=D\n" +

	    //That
	   "@R13\n" +
	   "D=M\n" +
	   "@1\n" +
	   "D=D-A\n" +
	   "A=D\n" +
	   "D=M\n" +
	   "@THAT\n" +
	    "M=D\n" +

	    //this
	    "@R13\n"+
	    "D=M\n" +
	    "@2\n" +
	    "D=D-A\n" +
	    "A=D\n"+
	    "D=M\n" +
	    "@THIS\n" +
	    "M=D\n"+

	    //ARG
	    "@R13\n" +
	    "D=M\n" +
	    "@3\n" +
	    "D=D-A\n" +
	    "A=D\n" +
	    "D=M\n" +
	    "@ARG\n" +
	    "M=D\n" +

	    //LCL
	    "@R13\n" +
	    "D=M\n" +
	    "@4\n" +
	    "D=D-A\n" +
	    "A=D\n" +
	    "D=M\n" +
	    "@LCL\n" +
	    "M=D\n" +
	    "@R14\n" +
	    "A=M\n" +
	    "0;JMP\n";
	out.write(ret);
	out.flush();
    }

    //writes function and initializes local
    public void writeFunction(String fName, int nLocals){
	String s = "//fucntion " + fName +" " + nLocals + "\n" +
	    "(" + fName + ")\n";
	for(int i =0; i< nLocals; i++){
	    s = s + "@0\n" +
		"D=A\n" +
		"@0\n" +
		"A=M\n" +
		"M=D\n" +
		"@0\n" +
		"M=M+1\n";
	}
	out.write(s);
	out.flush();
	    
    }
    

   
    
    public String writePushPop1(String q,String arg1, int in){
	if(q.equals("C_POP")){
	    if(arg1.equals("constant")){
		return "@0\nM=M-1";
	    }
	    if(arg1.equals("local")){
		String x = "@LCL\n" +
		    "D=M\n"+
		    "@" + in + "\n" +
		    "D=D+A\n" +
		    "@R13\n" +
		    "M=D\n" +
		    "@SP\n" +
		    "A=M-1\n" +
		    "D=M\n" +
		    "@R13\n" +
		    "A=M\n" +
		    "M=D\n" +
		    "@0\n" +
		    "M=M-1";
		return x;
	    }
	    if(arg1.equals("argument")){
		String x = "@ARG\n" +
		    "D=M\n"+
		    "@" + in + "\n" +
		    "D=D+A\n" +
		    "@R13\n" +
		    "M=D\n" +
		    "@SP\n" +
		    "A=M-1\n" +
		    "D=M\n" +
		    "@R13\n" +
		    "A=M\n" +
		    "M=D\n" +
		    "@0\n" +
		    "M=M-1";
		return x;
	    }
	    if(arg1.equals("this")){
		String x = "@THIS\n" +
		    "D=M\n"+
		    "@" + in + "\n" +
		    "D=D+A\n" +
		    "@R13\n" +
		    "M=D\n" +
		    "@SP\n" +
		    "A=M-1\n" +
		    "D=M\n" +
		    "@R13\n" +
		    "A=M\n" +
		    "M=D\n" +
		    "@0\n" +
		    "M=M-1";
		return x;
	    }
	    if(arg1.equals("that")){
		String x = "@THAT\n" +
		    "D=M\n" +
		    "@" + in + "\n" +
		    "D=D+A\n" +
		    "@var\n"+
		    "M=D\n"+
		    "@SP\n"+
		    "A=M-1\n" +
		    "D=M\n"+
		    "@var\n"+
		    "A=M\n"+
		    "M=D\n"+
		    "@0\n" +
		    "M=M-1";
		return x;
	    }
	    if(arg1.equals("pointer")){
		String x = "@3\n" +
		    "D=A\n"+
		    "@" + in + "\n"+
		    "D=D+A\n" +
		    "@R13\n" +
		    "M=D\n"+
		    "@SP\n" +
		    "A=M-1\n" +
		    "D=M\n" + 
		    "@R13\n" +
		    "A=M\n" +
		    "M=D\n" +
		    "@0\n" +
		    "M=M-1";
		return x;
	    }
	    if(arg1.equals("temp")){
		String x = "@5\n"+
		    "D=A\n" +
		    "@" + in + "\n"  +
		    "D=D+A\n" +
		    "@R13\n" +
		    "M=D\n" +
		    "@SP\n" +
		    "A=M-1\n" +
		    "D=M\n" +
		    "@R13\n" + 
		    "A=M\n" + 
		    "M=D\n" + 
		    "@0\n" +
		    "M=M-1";
		return x;
	    }
	    if(arg1.equals("static")){
		String x = "@SP\nA=M-1\nD=M\n@" + file + "." + in + "\nM=D\n@0\nM=M-1";
		return x;
	    }
	}
	if(q.equals("C_PUSH")){
	    if(arg1.equals("constant")){
		return "@" + in+"\nD=A\n@0\nA=M\nM=D\n@0\nM=M+1";
	    }
	    if(arg1.equals("local")){
		String x = "@" +in +"\n";
		x=x+"D=A\n";
		x=x+"@LCL\n";
		x=x+"A=M+D\n";
		x=x+"D=M\n";
		x=x+"@0\n";
		x=x+"A=M\n";
		x=x+"M=D\n";
		x=x+"@0\n";
		x=x+"M=M+1\n";
		return x;
	    }
	    if(arg1.equals("argument")){
		String x = "@" +in + "\n";
		x=x+"D=A\n";
		x=x+"@ARG\n";
		x=x+"A=M+D\n";
		x=x+"D=M\n";
		x=x+"@0\n";
		x=x+"A=M\n";
		x=x+"M=D\n";
		x=x+"@0\n";
		x=x+"M=M+1\n";
		return x;
	    }
	    if(arg1.equals("this")){
		String x = "@" +in + "\n";
		x=x+"D=A\n";
		x=x+"@THIS\n";
		x=x+"A=M+D\n";
		x=x+"D=M\n";
		x=x+"@0\n";
		x=x+"A=M\n";
		x=x+"M=D\n";
		x=x+"@0\n";
		x=x+"M=M+1\n";
		return x;
	    }
	    if(arg1.equals("that")){
		String x = "@" +in + "\n";
		x=x+"D=A\n";
		x=x+"@THAT\n";
		x=x+"A=M+D\n";
		x=x+"D=M\n";
		x=x+"@0\n";
		x=x+"A=M\n";
		x=x+"M=D\n";
		x=x+"@0\n";
		x=x+"M=M+1\n";
		return x;
	    } 
	    if(arg1.equals("pointer")){
		String x = "@" +in + "\n";
		x=x+"D=A\n";
		x=x+"@3\n";
		x=x+"A=A+D\n";
		x=x+"D=M\n";
		x=x+"@0\n";
		x=x+"A=M\n";
		x=x+"M=D\n";
		x=x+"@0\n";
		x=x+"M=M+1\n";
		return x;
	    }
	    if(arg1.equals("temp")){
		String x = "@" +in + "\n";
		x=x+"D=A\n";
		x=x+"@5\n";
		x=x+"A=A+D\n";
		x=x+"D=M\n";
		x=x+"@0\n";
		x=x+"A=M\n";
		x=x+"M=D\n";
		x=x+"@0\n";
		x=x+"M=M+1\n";
		return x;
	    }
	    if(arg1.equals("static")){
		String x = "@" +file +"." +in+"\n";
		x=x+"D=M\n";
		x=x+"@0\n";
		x=x+"A=M\n";
		x=x+"M=D\n";
		x=x+"@0\n";
		x=x+"M=M+1\n";
		return x;
	    }
	    return "UNKNOWN";
	}
	return "UNKOWN";
    }

    
}
