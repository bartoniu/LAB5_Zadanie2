import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Zadanie2 {
    private static String[] strings = {"aaaa", "bb", "cccccccccccccc", "dddddd"};

    static List<threadCreator> queue = new CopyOnWriteArrayList<>();
    static Object lock = new Object();
    public static void main(String[] args) {

        for (String s : strings) {
            new threadCreator(s,lock).start();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (queue.size() != 0) {
            for (threadCreator t : queue) {
                synchronized (t) {
                    if (t.getState().equals(Thread.State.WAITING)) {
                        synchronized (t.lock) {
                            t.lock.notify();
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else break;
                }
            }
        }

    }

    private static class threadCreator extends Thread {
        String tobewritten;
        protected Object lock ;

        public threadCreator(String tobewritten , Object lock) {
            this.tobewritten = tobewritten;
            this.lock = lock;
        }

        @Override
        public void run() {
            queue.add(this);
            for (char c : tobewritten.toCharArray()) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(c);
                synchronized (lock){
                    lock.notify();
                }
            }
            queue.remove(this);

        }
    }

}
