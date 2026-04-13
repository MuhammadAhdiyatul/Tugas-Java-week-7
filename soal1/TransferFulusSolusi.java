// SOLUSI: Menambahkan atribut 'id' unik pada Account sebagai acuan urutan.
class Account {
    int id;
    int balance = 150;

    public Account(int id) {
        this.id = id;
    }
}

public class TransferFulusSolusi {

    // SOLUSI: Membuat method helper khusus untuk memproses transfer agar urutan penguncian konsisten.
    public static void transfer(Account sumber, Account tujuan) throws InterruptedException {
        // Menentukan mana yang akan dikunci pertama dan kedua berdasarkan ID.
        // Objek dengan ID lebih kecil SELALU dikunci pertama kali.
        Account lock1 = (sumber.id < tujuan.id) ? sumber : tujuan;
        Account lock2 = (sumber.id < tujuan.id) ? tujuan : sumber;

        // Proses penguncian sekarang terurut dan aman dari deadlock.
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName() + " berhasil mengunci akun ID: " + lock1.id);
            
            Thread.sleep(100); // Simulasi jeda / proses sistem
            
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " berhasil mengunci akun ID: " + lock2.id + " dan melakukan transfer.");
                tujuan.balance += sumber.balance;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Memberikan ID unik pada masing-masing objek Account
        Account acc1 = new Account(1); 
        Account acc2 = new Account(2); 

        // Thread 1: Transfer dari acc1 ke acc2
        Thread t1 = new Thread(() -> {
            try {
                transfer(acc1, acc2);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }, "Thread-1");

        // Thread 2: Transfer dari acc2 ke acc1
        Thread t2 = new Thread(() -> {
            try {
                transfer(acc2, acc1);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }, "Thread-2");

        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        // Baris ini sekarang PASTI akan dieksekusi karena tidak ada deadlock.
        System.out.println("--- HASIL AKHIR ---");
        System.out.println("Saldo Akhir acc1: " + acc1.balance);
        System.out.println("Saldo Akhir acc2: " + acc2.balance);
    }
}