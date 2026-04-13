class Account {
    int balance = 150;
}

public class TransferFulus {
    public static void main(String[] args) throws InterruptedException {
        Account acc1 = new Account();
        Account acc2 = new Account();

        
        Thread t1 = new Thread(() -> {
            synchronized (acc1) { // Thread 1 mengunci acc1 terlebih dahulu
                System.out.println("Thread 1: Mengunci acc1, bersiap transfer...");
                try { Thread.sleep(100); } catch (Exception e) {} 
                
                synchronized (acc2) { 
                    System.out.println("Thread 1: Mengunci acc2, melakukan transfer dari acc1 ke acc2");
                    acc2.balance += acc1.balance;
                }
            }
        });

       
        Thread t2 = new Thread(() -> {
            synchronized (acc1) { 
                System.out.println("Thread 2: Mengunci acc1, menunggu giliran...");
                try { Thread.sleep(100); } catch (Exception e) {}
                
                synchronized (acc2) { // Setelah mendapat acc1, baru Thread 2 mengunci acc2
                    System.out.println("Thread 2: Mengunci acc2, melakukan transfer dari acc2 ke acc1");
                    acc1.balance += acc2.balance;
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("--- HASIL AKHIR ---");
        System.out.println("Saldo Akhir acc1: " + acc1.balance);
        System.out.println("Saldo Akhir acc2: " + acc2.balance);
    }
}