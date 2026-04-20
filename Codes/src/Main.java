import objects.*;
import service.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String DATA_DIR = "data";

    private static SiswaService      siswaService;
    private static BukuService       bukuService;
    private static PegawaiService    pegawaiService;
    private static PeminjamanService peminjamanService;

    private static Scanner scanner = new Scanner(System.in);
    private static Pegawai pegawaiLogin = null;

    public static void main(String[] args) {
        FileService.ensureDataDir(DATA_DIR);

        // Inisialisasi service
        pegawaiService    = new PegawaiService(DATA_DIR);
        siswaService      = new SiswaService(DATA_DIR);
        bukuService       = new BukuService(DATA_DIR);
        peminjamanService = new PeminjamanService(DATA_DIR, siswaService, bukuService);

        printHeader();

        if (!prosesLogin()) {
            System.out.println("Program dihentikan.");
            return;
        }

        menuUtama();
    }

    //print formats(semoga termasuk kreativitas wkwkw)
    private static void printHeader() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║          SISTEM INFORMASI PERPUSTAKAAN SMP               ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    private static void printGaris() {
        System.out.println("────────────────────────────────────────────────────────────────────────────");
    }

    private static void printSub(String judul) {
        System.out.println("\n  [ " + judul.toUpperCase() + " ]");
        printGaris();
    }

    private static String input(String prompt) {
        System.out.print("  " + prompt);
        return scanner.nextLine().trim();
    }

    private static int inputInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(input(prompt));
            } catch (NumberFormatException e) {
                System.out.println("  [!] Masukkan angka yang valid.");
            }
        }
    }

    private static void tekanEnterUntukLanjut() {                  //masih buggy entah kenapa
        System.out.print("\n  Tekan Enter untuk melanjutkan...");
        scanner.nextLine();
    }

    //menu login

    private static boolean prosesLogin() {
        System.out.println("  Silahkan login untuk mengakses sistem.\n");
        int percobaan = 0;
        while (percobaan < 3) {
            String nip      = input("NIP      : ");
            String password = input("Password : ");
            try {
                pegawaiLogin = pegawaiService.login(nip, password);
                if (pegawaiLogin != null) {
                    System.out.println("\n  [OK] Login berhasil! Selamat datang, " + pegawaiLogin.getNama());
                    return true;
                }
                percobaan++;
                System.out.println("  [!] NIP atau password salah. Sisa percobaan: " + (3 - percobaan));
            } catch (IOException e) {
                System.err.println("  [ERROR] Gagal akses file: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    //main menu kalau login berhasil

    private static void menuUtama() {
        while (true) {
            System.out.println("\n  MENU UTAMA  (User: " + pegawaiLogin.getNama() + ")");
            printGaris();
            System.out.println("  1. Kelola Data Siswa");
            System.out.println("  2. Kelola Data Buku");
            System.out.println("  3. Kelola Data Pegawai");
            System.out.println("  4. Transaksi Peminjaman");
            System.out.println("  5. Laporan");
            System.out.println("  0. Keluar");
            printGaris();

            int pilihan = inputInt("Pilihan : ");
            switch (pilihan) {
                case 1: menuSiswa(); break;
                case 2: menuBuku(); break;
                case 3: menuPegawai(); break;
                case 4: menuTransaksi(); break;
                case 5: menuLaporan(); break;
                case 0: return;
                default: System.out.println("  [!] Pilihan tidak valid.");
            }
        }
    }

    //kelola siswa

    private static void menuSiswa() {
        while (true) {
            printSub("Kelola Data Siswa");
            System.out.println("  1. Tampilkan Semua\n  2. Tambah\n  3. Edit\n  4. Hapus\n  5. Cari\n  0. Kembali");
            int p = inputInt("Pilihan: ");
            try {
                switch (p) {
                    case 1: tampilSemuaSiswa(); break;
                    case 2: tambahSiswa(); break;
                    case 3: editSiswa(); break;
                    case 4: hapusSiswa(); break;
                    case 5: cariSiswa(); break;
                    case 0: return;
                }
            } catch (IOException e) { System.out.println("  [ERROR] " + e.getMessage()); }
        }
    }

    private static void tampilSemuaSiswa() throws IOException {
        Siswa[] list = siswaService.getAll();
        if (list.length == 0) {
            System.out.println("  (Belum ada data siswa.)");
        } else {
            System.out.printf("  %-5s %-12s %-25s %s%n", "No.", "NIS", "Nama", "Alamat");
            printGaris();
            for (int i = 0; i < list.length; i++) {
                System.out.printf("  %-5d %-12s %-25s %s%n", (i+1), list[i].getNis(), list[i].getNama(), list[i].getAlamat());
            }
        }
        tekanEnterUntukLanjut();
    }

    private static void tambahSiswa() throws IOException {
        String nis = input("NIS : ");
        String nama = input("Nama : ");
        String alamat = input("Alamat : ");
        if (siswaService.tambah(new Siswa(nis, nama, alamat))) {
            System.out.println("  [OK] Berhasil ditambahkan.");
        }
        tekanEnterUntukLanjut();
    }

    private static void editSiswa() throws IOException {
        String nis = input("Masukkan NIS: ");
        Siswa lama = siswaService.findByNis(nis);
        if (lama != null) {
            String nama = input("Nama baru [" + lama.getNama() + "]: ");
            String alamat = input("Alamat baru [" + lama.getAlamat() + "]: ");
            if (nama.isEmpty()) nama = lama.getNama();
            if (alamat.isEmpty()) alamat = lama.getAlamat();
            siswaService.edit(nis, new Siswa(nis, nama, alamat));
            System.out.println("  [OK] Berhasil diperbarui.");
        } else {
            System.out.println("  [!] Data tidak ditemukan.");
        }
        tekanEnterUntukLanjut();
    }

    private static void hapusSiswa() throws IOException {
        String nis = input("Masukkan NIS: ");
        if (siswaService.hapus(nis)) System.out.println("  [OK] Berhasil dihapus.");
        else System.out.println("  [!] Gagal hapus.");
        tekanEnterUntukLanjut();
    }

    private static void cariSiswa() throws IOException {
        String keyword = input("Masukkan nama: ");
        Siswa[] hasil = siswaService.cariByNama(keyword);
        for (int i = 0; i < hasil.length; i++) {
            System.out.println("  " + (i+1) + ". " + hasil[i].getNama() + " [" + hasil[i].getNis() + "]");
        }
        tekanEnterUntukLanjut();
    }

    //kelola buku

    private static void menuBuku() {
        while (true) {
            printSub("Kelola Data Buku");
            System.out.println("  1. Tampilkan Semua\n  2. Tambah\n  3. Cari Buku\n  4. Edit\n  5. Hapus\n  0. Kembali");
            int p = inputInt("Pilihan: ");
            try {
                switch (p) {
                    case 1: tampilSemuaBuku(); break;
                    case 2: tambahBuku();      break;
                    case 3: cariBuku();        break;
                    case 4: editBuku();        break;
                    case 5: hapusBuku();       break;
                    case 0: return;
                    default: System.out.println("  [!] Pilihan tidak valid.");
                }
            } catch (IOException e) { System.out.println("  [ERROR] " + e.getMessage()); }
        }
    }

    private static void cariBuku() throws IOException {
        printSub("Cari Buku");
        System.out.println("  1. Cari berdasarkan Judul");
        System.out.println("  2. Cari berdasarkan Kategori (Jenis)");
        int pil = inputInt("Pilihan: ");

        Buku[] hasil = new Buku[0];
        if (pil == 1) {
            String judul = input("Masukkan Judul: ");
            hasil = bukuService.cariByJudul(judul);
        } else if (pil == 2) {
            String jenis = input("Masukkan Kategori: ");
            hasil = bukuService.cariByJenis(jenis); // Pastikan method ini ada di BukuService
        } else {
            System.out.println("  [!] Pilihan tidak valid.");
            return;
        }

        if (hasil.length == 0) {
            System.out.println("  [!] Buku tidak ditemukan.");
        } else {
            System.out.printf("\n  %-5s %-10s %-30s %-15s%n", "No.", "Kode", "Judul", "Kategori");
            printGaris();
            for (int i = 0; i < hasil.length; i++) {
                System.out.printf("  %-5d %-10s %-30s %-15s%n",
                        (i + 1), hasil[i].getKode(), hasil[i].getJudul(), hasil[i].getJenisBuku());
            }
        }
        tekanEnterUntukLanjut();
    }

    private static void tampilSemuaBuku() throws IOException {
        Buku[] list = bukuService.getAll();
        System.out.printf("  %-5s %-10s %-30s %-15s %s%n", "No.", "Kode", "Judul", "Kategori" ,"Status");
        printGaris();
        for (int i = 0; i < list.length; i++) {
            String status = peminjamanService.isBukuDipinjam(list[i].getKode()) ? "Dipinjam" : "Tersedia";
            System.out.printf("  %-5s %-10s %-30s %-15s %s%n", (i+1), list[i].getKode(), list[i].getJudul(),list[i].getJenisBuku() , status);
        }
        tekanEnterUntukLanjut();
    }

    private static void tambahBuku() throws IOException {
        String kode = input("Kode : ");
        String judul = input("Judul : ");
        String jenis = input("Jenis : ");
        if (bukuService.tambah(new Buku(kode, judul, jenis))) System.out.println("  [OK] Buku ditambah.");
        tekanEnterUntukLanjut();
    }

    private static void editBuku() throws IOException {
        String kode = input("Kode Buku: ");
        Buku lama = bukuService.findByKode(kode);
        if (lama != null) {
            String judul = input("Judul baru: ");
            String jenis = input("Jenis baru: ");
            bukuService.edit(kode, new Buku(kode, judul, jenis));
            System.out.println("  [OK] Berhasil edit.");
        }
        tekanEnterUntukLanjut();
    }

    private static void hapusBuku() throws IOException {
        String kode = input("Kode Buku: ");
        if (bukuService.hapus(kode)) System.out.println("  [OK] Berhasil hapus.");
        tekanEnterUntukLanjut();
    }

    //kelola pegawai

    private static void menuPegawai() {
        while (true) {
            printSub("Kelola Pegawai");
            System.out.println("  1. Tampilkan\n  2. Tambah\n  3. Hapus\n  0. Kembali");
            int p = inputInt("Pilihan: ");
            try {
                if (p == 1) tampilSemuaPegawai();
                else if (p == 2) tambahPegawai();
                else if (p == 3) hapusPegawai();
                else if (p == 0) return;
            } catch (IOException e) { }
        }
    }

    private static void tampilSemuaPegawai() throws IOException {
        Pegawai[] list = pegawaiService.getAll();
        for (int i = 0; i < list.length; i++) {
            System.out.println("  " + (i+1) + ". " + list[i].getNama() + " (" + list[i].getNip() + ")");
        }
        tekanEnterUntukLanjut();
    }

    private static void tambahPegawai() throws IOException {
        String nip = input("NIP: ");
        String nama = input("Nama: ");
        String tgl = input("Tgl Lahir: ");
        String pass = input("Pass: ");
        pegawaiService.tambah(new Pegawai(nip, nama, tgl, pass));
        tekanEnterUntukLanjut();
    }

    private static void hapusPegawai() throws IOException {
        String nip = input("NIP: ");
        if (pegawaiService.hapus(nip)) System.out.println("  [OK] Dihapus.");
        tekanEnterUntukLanjut();
    }

    //kelola transaksi

    private static void menuTransaksi() {
        while (true) {
            printSub("Transaksi");
            System.out.println("  1. Pinjam Buku\n  2. Kembali Buku\n  3. Tampil Semua\n  0. Kembali");
            int p = inputInt("Pilihan: ");
            try {
                if (p == 1) pinjamBuku();
                else if (p == 2) kembalikanBuku();
                else if (p == 3) tampilSemuaTransaksi();
                else if (p == 0) return;
            } catch (IOException e) { }
        }
    }
    private static void pinjamBuku() throws IOException {
        printSub("Peminjaman Buku");
        String nis  = input("NIS Siswa       : ");
        String kode = input("Kode Buku       : ");
        String tgl  = input("Tanggal Pinjam  (dd-mm-yyyy): ");
        String tempo = input("Tanggal Kembali (dd-mm-yyyy): ");

        System.out.println();
        if (peminjamanService.pinjam(nis, kode, tgl, tempo)) {
            System.out.println("  [OK] Peminjaman berhasil dicatat.");
        } else {
            System.out.println("  [!] Gagal: Siswa/Buku tidak ada atau buku sedang dipinjam.");
        }
        tekanEnterUntukLanjut();
    }

    private static void kembalikanBuku() throws IOException {
        printSub("Pengembalian Buku");
        String trx  = input("Kode Transaksi  : ");
        String tglK = input("Tanggal Kembali (dd-mm-yyyy): ");

        System.out.println();
        if (peminjamanService.kembalikan(trx, tglK)) {
            System.out.println("  [OK] Buku telah berhasil dikembalikan.");
        } else {
            System.out.println("  [!] Kode transaksi tidak ditemukan.");
        }
        tekanEnterUntukLanjut();
    }

    private static void tampilSemuaTransaksi() throws IOException {
        printSub("Daftar Semua Transaksi");
        Peminjaman[] list = peminjamanService.getAll();

        if (list.length == 0) {
            System.out.println("  (Belum ada transaksi.)");
        } else {
            System.out.printf("  %-10s %-10s %-10s %-12s %s%n", "Kode", "NIS", "Buku", "Tgl Pinjam", "Status");
            printGaris();
            for (int i = 0; i < list.length; i++) {
                String statusStr = (list[i].getStatus() == 1) ? "Kembali" : "Dipinjam";
                System.out.printf("  %-10s %-10s %-10s %-12s %s%n",
                        list[i].getKodeTransaksi(),
                        list[i].getNis(),
                        list[i].getKodeBuku(),
                        list[i].getTanggalPinjam(),
                        statusStr);
            }
        }
        tekanEnterUntukLanjut();
    }

    //laporan

    private static void menuLaporan() {
        while (true) {
            printSub("Laporan");
            System.out.println("  1. Statistik\n  2. Buku Belum Kembali\n  3. Per Jenis Buku\n  0. Kembali");
            int p = inputInt("Pil: ");
            try {
                if (p == 1) laporanStatistik();
                else if (p == 2) laporanBelumKembali();
                else if (p == 3) laporanPerJenisBuku();
                else if (p == 0) return;
            } catch (IOException e) {}
        }
    }

    private static void laporanStatistik() throws IOException {
        printSub("Statistik Perpustakaan");

        // Mengambil array dan langsung menghitung panjangnya (.length)
        int totalSiswa = siswaService.getAll().length;
        int totalBuku  = bukuService.getAll().length;
        int[] stat     = peminjamanService.getStatistik();

        System.out.printf("  %-25s : %d%n", "Total Siswa", totalSiswa);
        System.out.printf("  %-25s : %d%n", "Total Buku", totalBuku);
        printGaris();
        System.out.printf("  %-25s : %d%n", "Total Transaksi", stat[0]);
        System.out.printf("  %-25s : %d%n", "Sedang Dipinjam", stat[1]);
        System.out.printf("  %-25s : %d%n", "Sudah Kembali", stat[2]);

        tekanEnterUntukLanjut();
    }

    private static void laporanBelumKembali() throws IOException {
        printSub("Laporan: Buku Belum Kembali");
        Peminjaman[] list = peminjamanService.getBelumDikembalikan();

        if (list.length == 0) {
            System.out.println("  Semua buku sudah dikembalikan.");
        } else {
            System.out.printf("  %-10s %-12s %-10s %-12s%n", "Kode TRX", "Peminjam", "Kode Buku", "Jatuh Tempo");
            printGaris();
            for (int i = 0; i < list.length; i++) {
                System.out.printf("  %-10s %-12s %-10s %-12s%n",
                        list[i].getKodeTransaksi(),
                        list[i].getNis(),
                        list[i].getKodeBuku(),
                        list[i].getTanggalKembali()); // Ini adalah tgl jatuh tempo di model
            }
        }
        System.out.println("\n  Total: " + list.length + " transaksi aktif.");
        tekanEnterUntukLanjut();
    }

    private static void laporanPerJenisBuku() throws IOException {
        Buku[] semua = bukuService.getAll();
        String[] jenisUnik = new String[semua.length];
        int[] hitung = new int[semua.length];
        int totalJenis = 0;

        for (int i = 0; i < semua.length; i++) {
            String j = semua[i].getJenisBuku();
            boolean ada = false;
            for (int k = 0; k < totalJenis; k++) {
                if (jenisUnik[k].equalsIgnoreCase(j)) {
                    hitung[k]++;
                    ada = true;
                    break;
                }
            }
            if (!ada) {
                jenisUnik[totalJenis] = j;
                hitung[totalJenis] = 1;
                totalJenis++;
            }
        }

        System.out.printf("  %-20s %s%n", "Jenis", "Jumlah");
        printGaris();
        for (int i = 0; i < totalJenis; i++) {
            System.out.printf("  %-20s %d%n", jenisUnik[i], hitung[i]);
        }
        tekanEnterUntukLanjut();
    }
}