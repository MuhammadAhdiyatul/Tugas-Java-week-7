package soal2;

class Resto {
    // Shared Resource (Sumber Daya Bersama): 
    // Variabel ini diakses oleh 3 Thread sekaligus.
    private int chickenStock = 100;

    // Masalah Utama: Method ini TIDAK SYNCHRONIZED (tidak disinkronisasi).
    // Akibatnya, Kasir-A, Kasir-B, dan Kasir-C bisa masuk ke method ini di detik yang sama persis.
    public void serveCustomer(String cashierName) { 
        // Critical Section (Bagian Kritis):
        if (chickenStock > 0) {
            // Skenario Error:
            // Anggap stok sisa 1. Kasir-A masuk, dicek 1 > 0 (True).
            // Tiba-tiba Kasir-B masuk, dicek 1 > 0 (True).
            
            try { 
                // Simulasi jeda waktu (misal mesin kasir memproses data).
                // Saat Kasir-A sedang "tidur" di sini, Kasir-B dan C bisa menyalip 
                // masuk karena tidak ada penguncian (lock).
                Thread.sleep(10); 
            } catch (InterruptedException e) {}

            // Operasi ini sangat rawan (Non-Atomic Operation).
            // chickenStock-- sebenarnya adalah 3 langkah komputer:
            // 1. Baca nilai saat ini (misal 10).
            // 2. Kurangi 1 (jadi 9).
            // 3. Simpan kembali nilai 9 ke chickenStock.
            // Jika Kasir A dan B membaca nilai 10 secara bersamaan, 
            // keduanya akan menyimpan nilai 9. Stok yang harusnya berkurang 2, malah berkurang 1.
            chickenStock--;

            System.out.println(cashierName + " berhasil menjual 1 ayam. Sisa stok: " + chickenStock);
        } else {
            System.out.println(cashierName + " gagal: Stok Habis!");
        }
    }

    public int getRemainingStock() {
        return chickenStock;
    }
}

public class RestoSimulasi {
    public static void main(String[] args) throws InterruptedException { Resto ayamJuicyLuicyGallagher = new Resto();
    Runnable task = () -> {
    for (int i = 0; i < 40; i++) { ayamJuicyLuicyGallagher.serveCustomer(Thread.currentThread().getName());
     }
    };

    Thread kasir1 = new Thread(task, "Kasir-A"); Thread kasir2 = new Thread(task, "Kasir-B"); Thread kasir3 = new Thread(task, "Kasir-C");
    kasir1.start(); kasir2.start(); kasir3.start();
    kasir1.join(); kasir2.join(); kasir3.join();
    System.out.println("--- HASIL AKHIR STOK: " + ayamJuicyLuicyGallagher.getRemainingStock() + " ---");
    }
    
}
