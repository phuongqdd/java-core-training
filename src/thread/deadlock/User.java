package thread.deadlock;

public class User implements Runnable{
    BankAccount bankAccount;

    public BankAccount getBankAccount(){
        return this.bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount){
        this.bankAccount = bankAccount;
    }

    @Override
    public void run() {
        sendNotify();
    }

    public synchronized void sendNotify(){
        System.out.println(Thread.currentThread().getName() + ": User");
        System.out.println(Thread.currentThread().getName() + ": Waiting for bankAccount...");
        bankAccount.deposit();
    }
}
