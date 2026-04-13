package soal3;

import java.util.Scanner;

class SharedData {
    private long totalAkhir = 0;

    public synchronized void tambahkanKeTotalAkhir(long hasilParsial) {
        totalAkhir += hasilParsial;
    }

    public long getTotalAkhir() {
        return totalAkhir;
    }
}

public class PenjumlahanParalel {
    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        
        System.out.print("Masukkan Jumlah Thread : ");
        int jumlahThread = input.nextInt();
        System.out.print("Masukkan Angka Akhir: ");
        int angkaAkhir = input.nextInt();

        SharedData dataGlobal = new SharedData();
        Thread[] arrayThread = new Thread[jumlahThread];
        
        // TAMBAHAN: Array untuk menyimpan teks hasil parsial agar bisa dicetak berurutan nanti
        String[] logHasil = new String[jumlahThread];

        int rentang = angkaAkhir / jumlahThread;
        int sisaBagi = angkaAkhir % jumlahThread;
        int batasBawahAktif = 1;

        for (int i = 0; i < jumlahThread; i++) {
            final int batasBawah = batasBawahAktif;
            final int batasAtas = (i == jumlahThread - 1) ? (batasBawahAktif + rentang - 1 + sisaBagi) : (batasBawahAktif + rentang - 1);
            final int idThread = i + 1;
            final int indexArray = i; // Index untuk array logHasil

            arrayThread[i] = new Thread(() -> {
                long hasilParsial = 0; 
                for (int j = batasBawah; j <= batasAtas; j++) {
                    hasilParsial += j;
                }
                
                // TAMBAHAN: Simpan teks ke dalam array, JANGAN langsung di-print di sini
                logHasil[indexArray] = "Thread " + idThread + ": Menjumlahkan " + batasBawah + " - " + batasAtas + " | Hasil Parsial = " + hasilParsial;
                
                dataGlobal.tambahkanKeTotalAkhir(hasilParsial);
            });

            batasBawahAktif = batasAtas + 1; 
            arrayThread[i].start();
        }

        for (int i = 0; i < jumlahThread; i++) {
            arrayThread[i].join();
        }

        // TAMBAHAN: Cetak hasil parsial secara berurutan setelah semua thread selesai bekerja
        for (String log : logHasil) {
            System.out.println(log);
        }

        System.out.println("--- HASIL AKHIR ---");
        System.out.println("Total Penjumlahan Seluruh Thread: " + dataGlobal.getTotalAkhir());
        
        input.close();
    }
}