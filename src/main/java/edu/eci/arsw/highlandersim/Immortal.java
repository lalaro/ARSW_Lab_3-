package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private int health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    private boolean alive = true;

    private boolean paused = false;

    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue=defaultDamageValue;
    }

    public void run() {

        while (true) {
            Immortal im;


            synchronized (this) {
                
                if(paused){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }   
                

                synchronized(immortalsPopulation){
                
                    int myIndex = immortalsPopulation.indexOf(this);

                    int nextFighterIndex = r.nextInt(immortalsPopulation.size());
                    //avoid self-fight
                    if (nextFighterIndex == myIndex) {
                        nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
                    }

                    im = immortalsPopulation.get(nextFighterIndex);
                    if(this.getAlive() && nextFighterIndex != myIndex){
                        
                        try {
                            this.fight(im);
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
                
            }
            

        }

    }

    public void fight(Immortal i2) {

        if (i2.getHealth() > 0) {
            i2.changeHealth(i2.getHealth() - defaultDamageValue);
            this.health += defaultDamageValue;
            updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");
            if(i2.getHealth()<= 0){
                updateCallback.processReport(this + " says: I killed " + i2 + " !\n");
                
                i2.dead();
                immortalsPopulation.remove(i2);
                
                try {
                    i2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               
            }
        } else {
            i2.dead();
            updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
            
        }

    }

    public void changeHealth(int v) {
        health = v;
    }

    public void dead(){
        this.alive = false;
    }

    public boolean getAlive(){
        return alive;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

    public void pauseImmortal() {
        synchronized (this) {
            paused = true;
        }
    }

    public void resumeImmortal() {
        synchronized (this) {
            paused = false;
            notify();
        }
    }
}
