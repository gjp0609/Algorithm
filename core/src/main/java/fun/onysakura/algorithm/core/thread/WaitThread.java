package fun.onysakura.algorithm.core.thread;

public class WaitThread {

    static String s = "";

    public static void main(String[] args) throws Exception {
        WaitThread test = new WaitThread();
        new Thread(() -> {
            try {
                System.out.println(test.getS());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(1000);
        s = "aaa";
        System.out.println(s);
    }

    public synchronized String getS() throws Exception {
        while ("".equals(s)) {
            this.wait(100);
            System.out.println("sleep 100");
        }
        return s;
    }
}
