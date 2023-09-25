package diningphilosophers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChopStick {

    private static int stickCount = 0;
    private boolean iAmFree = true;
    private final int myNumber;
    private final Lock verrou = new ReentrantLock();
    private final Condition dispo = verrou.newCondition();
    public ChopStick() {
        myNumber = ++stickCount;
    }

    public boolean tryTake() throws InterruptedException {
        verrou.lock();
        try{
            while (!iAmFree){
                dispo.await();
            }
            iAmFree = false;
            // Pas utile de faire notifyAll ici, personne n'attend qu'elle soit occupée
            return true; // Succès
        }finally {
            verrou.unlock();
        }
    }

    public void release() {
        verrou.lock();
        try {
            iAmFree = true;
            dispo.signalAll();
            System.out.println("Stick " + myNumber + " Released");
        } finally {
            verrou.unlock();
        }
    }

    @Override
    public String toString() {
        return "Stick#" + myNumber;
    }
}
