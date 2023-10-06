public class App {
    public static void main(String args[]) {
        String[] sa = { "a", "X", "+", "." };
        for (String s : sa) {
            Runnable ps = new PrintChar(s, 200);
            Thread ts = new Thread(ps, s);
            ts.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        } // end for each character
    } // end main
} // end class TaskThreadDemo

class PrintChar implements Runnable {
    String ch;
    int times;

    public PrintChar(String c, int n) {
        ch = c;
        times = n;
    } // end constructor

    public void run() {
        
        for (int i = 0; i < times; i++) {
            System.out.print(ch);
        } // end for loop
    } // end method run
} // end class PrintChar