package thread.vidu2;

import java.util.Random;

public class Thread1 extends Thread{
    SharedData sharedData;

    public Thread1(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Random random = new Random();

        while (sharedData.checkAvaiable()){
            synchronized (sharedData){
                int rad = random.nextInt(100) + 1;
                sharedData.setRad(rad);
                sharedData.plus(rad);

                System.out.println("T1: " + rad);
                if(rad % 3 == 0){
                    sharedData.setIndex(2);
                }else{
                    sharedData.setIndex(3);
                }


                sharedData.notifyAll();
                try {
                    sharedData.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        System.out.println("Stop T1");
        synchronized (sharedData){
            sharedData.notifyAll();
        }
    }
}
