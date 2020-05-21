package personal.popy.natives;

//import org.apache.jasper.compiler.AntCompiler;

public class Bootstrap {

    static {
        System.loadLibrary("JavaNatives");
    }


    public native Object start();

    public native char[] getChars(String value);

}
