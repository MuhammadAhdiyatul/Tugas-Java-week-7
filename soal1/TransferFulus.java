class Account {
    int balance = 150;
}

public class TransferFulus {

    public static void main(String[] args) throws InterruptedException {
        Account acc1 = new Account();
        Account acc2 = new Account();

        // Thread 1: Menjumlahkan/ transfer fulus dari acc1 ke acc2
        Thread t1 = new Thread(() -> {
            synchronized (acc1) { 
                // Mengunci (lock) objek acc1 agar tidak bisa diakses thread lain
                System.out.println("Thread 1: Mengunci acc1, bersiap transfer ke acc2...");
                
                try { 
                    Thread.sleep(100); 
                } catch (Exception e) {
                    // Exception (khususnya InterruptedException) diperlukan karena 
                    // saat thread sedang "tidur" (sleep), thread tersebut bisa saja 
                    // diinterupsi oleh proses lain. Try-catch menangani potensi error tersebut.
                } 
                
                synchronized (acc2) { 
                    // Mencoba mengunci objek acc2 untuk menyelesaikan transfer
                    System.out.println("Thread 1: Mengunci acc2, melakukan transfer...");
                    acc2.balance += acc1.balance;
                }
            }
        });

        // Thread 2: Menjumlahkan/ transfer fulus dari acc2 ke acc1
        Thread t2 = new Thread(() -> {
            synchronized (acc2) { 
                // Mengunci (lock) objek acc2 agar tidak bisa diakses thread lain
                System.out.println("Thread 2: Mengunci acc2, bersiap transfer ke acc1...");
                
                try { Thread.sleep(100); } catch (Exception e) {}
                
                synchronized (acc1) { 
                    // Mencoba mengunci objek acc1 untuk menyelesaikan transfer
                    System.out.println("Thread 2: Mengunci acc1, melakukan transfer...");
                    acc1.balance += acc2.balance;
                }
            }
        });

        t1.start();
        t2.start();
        
        // join() memastikan main thread menunggu t1 dan t2 selesai sebelum lanjut
        t1.join();
        t2.join();
        
        System.out.println("--- HASIL AKHIR ---");
        System.out.println("Saldo Akhir acc1: " + acc1.balance);
        System.out.println("Saldo Akhir acc2: " + acc2.balance);
    }
}