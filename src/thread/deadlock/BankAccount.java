package thread.deadlock;

public class BankAccount implements Runnable{
    User owner;

    public User getOwner(){
        return owner;
    }

    public void setOwner(User owner){
        this.owner = owner;
    }

    @Override
    public void run() {
        deposit();
    }

    public synchronized void deposit(){
        System.out.println(Thread.currentThread().getName() + ": BankAccount");
        System.out.println(Thread.currentThread().getName() + ": Waiting for user...");
        owner.sendNotify();
    }
}
