package edu.eci.arst.concprg.prodcons;

import java.util.Queue;

public class Consumer extends Thread {

    private final Queue<Integer> queue;

    public Consumer(Queue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        queue.wait();
                    }
                
                    int elem = ((java.util.concurrent.BlockingQueue<Integer>) queue).take();
                    System.out.println("Consumer consumes " + elem);

                    queue.notifyAll();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
