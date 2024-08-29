package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer extends Thread {

    private final Queue<Integer> queue;
    private final int stockLimit;
    private int dataSeed = 0;
    private final Random rand;

    public Producer(Queue<Integer> queue, int stockLimit) {
        this.queue = queue;
        this.stockLimit = stockLimit;
        this.rand = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (queue) {
                    dataSeed = dataSeed + rand.nextInt(100);
                    System.out.println("Producer added " + dataSeed);
                    queue.add(dataSeed);

                    queue.notifyAll();
                }

                Thread.sleep(100); 

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
