package thread.vidu1;

public class SinhSoNgauNhien {
    public static void main(String[] args) {
        SharedData sharedData = new SharedData();

        ThreadRandom threadRandom = new ThreadRandom(sharedData);
        ThreadSquare threadSquare = new ThreadSquare(sharedData);

        threadRandom.start();
        threadSquare.start();
    }
}
