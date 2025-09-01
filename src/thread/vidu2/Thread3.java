package thread.vidu2;

public class Thread3 extends Thread{
    SharedData sharedData;

    public Thread3(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public void run() {
        while (sharedData.checkAvaiable()){
            synchronized (sharedData){
                sharedData.notifyAll();
                try {
                    while (sharedData.getIndex() != 3 && sharedData.checkAvaiable()){
                        sharedData.wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int rad = sharedData.getRad();
                if(rad % 2 == 0){
                    if(rad % 4 == 0){
                        System.out.println(rad + " chia chia hết cho 4");
                    }else {
                        System.out.println(rad + " không chia hết cho 4");
                    }
                }else{
                    System.out.println("Số lẻ");
                }
                sharedData.setIndex(1);
            }
        }

        System.out.println("Stop T3");
        synchronized (sharedData){
            sharedData.notifyAll();
        }
    }
}
