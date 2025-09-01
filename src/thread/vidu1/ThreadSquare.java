package thread.vidu1;

public class ThreadSquare extends Thread{
    SharedData sharedData;

    public ThreadSquare(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            synchronized (sharedData){
                try {
                    sharedData.notifyAll();
                    sharedData.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int rad = sharedData.getRad();
                rad *= rad;
                System.out.println("T2: " + rad);
            }

        }

        synchronized (sharedData){
            sharedData.notifyAll();
        }
    }
}
