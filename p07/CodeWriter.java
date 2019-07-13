import java.io.*;

public class CodeWriter{
    //counter so that jumps and vars have unique variables
    public static int x =0;
    //string that holds the file name
    public String file  = "";
    //file writing devices
    private PrintWriter out;
    //constructor
    public CodeWriter(String f){
	//initialize file
	file = f;
	try {
	    out = new PrintWriter(new File(f));
	} catch (Exception e){
	    e.printStackTrace();
	}
	//set the stack pointer
	out.write("@256\n");
	out.write("D=A\n");
	out.write("@0\n");
	out.write("M=D\n");
    }
    //IO wrapper for pure writeAr
    public void writeArithmetic(String s){
	out.write(writeAr(s) +"\n");
	out.flush();
    }
    public String writeAr(String s){
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
	    //subtracts
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
	    	    //computes and on top two stack
	    return "@0\nD=M\nA=D-1\nD=M\nA=A-1\nD=D&M\nM=D\n@0\nM=M-1";
	}
	if(s.equals("or")){
	    //computes or on top two stack
	    return "@0\nD=M\nA=D-1\nD=M\nA=A-1\nD=D|M\nM=D\n@0\nM=M-1";
	}
	
	if(s.equals("eq")){
	    //increments label counter
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
	    //computes less than by using labels jumping if less then and setting -1 or setting 0 depending on value
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
	    //increment label pointer
	    x++;
	    //greater than by conditional jumping
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
	    str = str + "A=M-1\n";
	    str = str + "M=-1\n";
	    str = str + "(end" + x+ ")\n";
       	    return str;
	}
	if(s.equals("not")){
	    //nots current stack position-1
	    return "@0\nA=M-1\nM=!M\n";
	}
	if(s.equals("neg")){
	    //takes negative of sp-1
	    return "@0\nA=M-1\nM=-M\n";
	}
	//for unkown calls
	return "UNKOWN";
    }


    // io wrapper for writePushPop
    public void writePushPop(String q,String s,int in){
    	out.write(writeP(q,s,in) +"\n");
	out.flush();
    }
    
    //translates push pops
    public String writeP(String q,String arg1, int in){
	if(q.equals("C_POP")){
	    //decrements sp
	    if(arg1.equals("constant")){
		return "@0\nM=M-1";
	    }
	    //writes to local  + offset
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
	    // writes to arg + offset
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
	    //writes to this + offset
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
	    //writes to that + offset
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
	    //writes to pointer + either 0 or 1 for this vs that
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
	    //writes to temp vars after 5
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
	    //creates new vars using file name + var index
	    if(arg1.equals("static")){
		String repl = file.replace("/","");
		String x = "@SP\nA=M-1\nD=M\n@" + repl + "." + in + "\nM=D\n@0\nM=M-1";
		return x;
	    }
	}
	if(q.equals("C_PUSH")){
	    //pushes constant to stack 
	    if(arg1.equals("constant")){
		return "@" + in+"\nD=A\n@0\nA=M\nM=D\n@0\nM=M+1";
	    }
	    //reads from local mem and push to stack
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
	    // reads from arg + offset push to stack
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
	    //reads from this + offseet push to stack
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
	    //reads from that + offset push to stack
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
	    // pushes from pointer 0:this 1:that
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
	    //pushes from temp 
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
	    //pushes from static var
	    if(arg1.equals("static")){
		String x = "@" +file.replace("/","") +"." +in+"\n";
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
