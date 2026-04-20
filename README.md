# 📚 Sistem Informasi Perpustakaan (CLI - Java)

Project ini adalah aplikasi **Sistem Informasi Perpustakaan** yang dibuat menggunakan Java.
Aplikasi ini memungkinkan pengelolaan data siswa, buku, pegawai, serta transaksi peminjaman dan pengembalian buku.

---

## ✨ Fitur Utama

### 🔐 Login System

* Login menggunakan **NIP & password**
* Maksimal 3 kali percobaan login

### 👨‍🎓 Kelola Data Siswa

* Tampilkan semua siswa
* Tambah siswa
* Edit data siswa
* Hapus siswa
* Cari siswa berdasarkan nama

### 📖 Kelola Data Buku

* Tampilkan semua buku + status (Dipinjam / Tersedia)
* Tambah buku
* Edit buku
* Hapus buku
* Cari buku berdasarkan:

  * Judul
  * Jenis/Kategori

### 👨‍💼 Kelola Data Pegawai

* Tampilkan pegawai
* Tambah pegawai
* Hapus pegawai

### 🔄 Transaksi

* Peminjaman buku
* Pengembalian buku
* Lihat semua transaksi

### 📊 Laporan

* Statistik perpustakaan:

  * Total siswa
  * Total buku
  * Total transaksi
* Buku yang belum dikembalikan
* Jumlah buku per kategori

---

## 🗂️ Struktur Project

```
LK06_Pemlan-main/
│
├── Codes/
│   └── src/
│       ├── Main.java
│       ├── objects/
│       │   ├── Buku.java
│       │   ├── Pegawai.java
│       │   ├── Peminjaman.java
│       │   └── Siswa.java
│       └── service/
│           ├── BukuService.java
│           ├── FileService.java
│           ├── PegawaiService.java
│           ├── PeminjamanService.java
│           └── SiswaService.java
│
└── data/
    ├── buku.txt
    ├── pegawai.txt
    ├── peminjaman.txt
    └── siswa.txt
```

---

## 💾 Sistem Penyimpanan

Data disimpan dalam bentuk file `.txt` di folder `data/`.

Format umum:

* **Buku** → `Kode|Judul|Jenis`
* **Siswa** → `NIS|Nama|Alamat`
* **Pegawai** → `NIP|Nama|TglLahir|Password`
* **Peminjaman** → `KodeTransaksi|NIS|KodeBuku|TglPinjam|TglKembali|Status`

---

## ▶️ Cara Menjalankan

1. Buka project di IDE (IntelliJ / VS Code / NetBeans)
2. Pastikan folder `data/` ada
3. Jalankan file:

```
Main.java
```

---

