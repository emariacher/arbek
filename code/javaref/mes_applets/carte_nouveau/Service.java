public class Service implements Include {

    static public void trace(String strace) {
        if(traceEnabled) System.out.println(strace);
    }


    static public void trace(int enable, String strace) {
        switch(enable) {
            case 1: System.out.println(strace); break;
            case 2: System.out.print(strace); break;
           default: if(traceEnabled) System.out.println(strace); break;
        }
    }

}
