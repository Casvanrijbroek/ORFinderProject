package blastConnetor;

import ORFinderApp.Query;

public class blastThread extends Thread {
    private Thread threadOcc;
    private Query query;

    blastThread(Query query) {
        this.query = query;
    }

    public void run() {
        try {
            // Let the thread sleep for a while.
            // To shit here
            Thread.sleep(50);

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void start() {
        if (threadOcc == null) {
            threadOcc = new Thread(this);
            threadOcc.start();
        }
    }
}
