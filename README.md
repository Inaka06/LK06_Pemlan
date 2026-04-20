Project ini adalah Sistem Informasi Perpustakaan SMP berbasis Java console. Terdapat 9 class yang terbagi ke dalam 3 kelompok:

Package objects (Model Data)
* Buku       : merepresentasikan data buku dengan atribut kode, judul, dan jenisBuku. Dilengkapi getter/setter, serta dua method penting: toFileString() untuk                    mengkonversi objek ke format teks (dipisah |) agar bisa disimpan ke file, dan fromFileString() (static) untuk membuat objek dari baris teks file.
* Siswa      : merepresentasikan data siswa dengan atribut nis, nama, dan alamat. Pola yang sama: memiliki toFileString() dan fromFileString(). Validasi bahwa NIS                harus berupa angka dilakukan di service-nya.
* Pegawai    : merepresentasikan data pegawai/admin dengan atribut nip, nama, tanggalLahir, dan password. Password disimpan dalam bentuk plain text di file.
* Peminjaman : paling kompleks di antara model. Memiliki dua konstanta static integer: STATUS_BELUM_KEMBALI = 0 dan STATUS_SUDAH_KEMBALI = 1. Menyimpan                           kodeTransaksi, nis (siapa yang meminjam), kodeBuku, tanggalPinjam, tanggalKembali, dan status. Method fromFileString() menggunakan split("\\|", -1)                dengan limit -1 agar field kosong tetap terbaca.


Package service (Logika Bisnis)
* FileService       : kelas utilitas murni (semua method-nya static). Bertanggung jawab atas semua operasi I/O file: readLines() membaca file ke array String,                           writeLines() menulis ulang seluruh file (overwrite), appendLine() menambah satu baris di akhir, dan createIfNotExists() membuat file jika                          belum ada. Kapasitas dibatasi MAX_LINES = 1000.
* SiswaService      : mengelola CRUD data siswa. Menyimpan path ke siswa.txt. Method tambah() melakukan dua validasi: cek duplikat NIS dan validasi format NIS                           harus angka (matches("\\d+")). Method hapus() menggunakan teknik filter array — memindahkan elemen yang tidak dihapus ke array baru.
* BukuService       : mengelola CRUD data buku. Punya dua method pencarian: cariByJudul() dan cariByJenis(), keduanya case-insensitive menggunakan contains().                           Kapasitas maksimal MAX_BUKU = 1000.
* PegawaiService    : mengelola CRUD pegawai plus fitur login. Yang menarik, konstruktornya memanggil seedDefaultAdmin() yang otomatis membuat akun admin (NIP:                          10001, password: admin123) jika file kosong. Method hapus() punya proteksi: tidak bisa menghapus jika hanya tersisa 1 pegawai.
* PeminjamanService : paling kompleks. Menerima injeksi SiswaService dan BukuService di konstruktor. Method pinjam() melakukan 3 validasi sebelum menyimpan: siswa                       & buku harus ada, siswa tidak boleh pinjam lebih dari MAKS_PINJAM = 2 buku, dan buku tidak sedang dipinjam orang lain. Kode transaksi di-                          generate otomatis (TRX + jumlah baris + 1).
  

Class Main
Entry point program. Memegang instance semua service sebagai field static, dan mengatur seluruh alur UI berbasis menu console. Proses login dibatasi 3 kali percobaan. Terdapat menu untuk kelola Siswa, Buku, Pegawai, Transaksi, dan Laporan (statistik, buku belum kembali, dan laporan per jenis buku).


<img width="1148" height="643" alt="image" src="https://github.com/user-attachments/assets/64ea27ed-3db3-4d15-a44a-053cbb6ec880" />
