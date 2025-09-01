package thread.vidu2;

public class SharedData {
    private int rad;
    private int total;
    private int index;

    public  SharedData(){
        this.total = 0;
        this.index = 1;
    }

    public int getRad() {
        return rad;
    }

    public void setRad(int rad) {
        this.rad = rad;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public synchronized void plus(int value){
        total += value;
    }

    public synchronized boolean checkAvaiable(){
        return total < 100;
    }
}
