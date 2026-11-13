# 🏫 Sistem Sales Order Pengadaan Sekolah (Dana BOS / APBD)

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Apache NetBeans](https://img.shields.io/badge/Apache%20NetBeans-Maven-1B6AC6?style=for-the-badge&logo=apachenetbeanside&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-XAMPP-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM%206.4-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-DAO%20JdbcTemplate-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

> **Aplikasi Desktop ERP Sales Order Pengadaan Barang & Jasa Sekolah**  
> **Penyedia:** CV. AXL SARANA MANDIRI  
> **Direktur / Developer:** **Axl Regan Yohande**

---

## 📌 Ringkasan Proyek
Aplikasi desktop berbasis **Java Swing & Maven** untuk mengelola transaksi **Sales Order Pengadaan Sekolah (Dana BOS)**. Proyek ini dikembangkan secara bertahap dari **Pertemuan 1 hingga Pertemuan 5**, dimulai dari konsep **OOP dasar**, perancangan **GUI Swing**, integrasi **Database MySQL (JDBC)**, implementasi **ORM (Hibernate)**, hingga **Spring DAO (`JdbcTemplate`)**.

---

## 🗓️ Log Perkembangan Proyek (Pertemuan 1 – Pertemuan 5)

| Pertemuan | Materi Utama | Implementasi yang Dikerjakan | File Terkait |
| :---: | :--- | :--- | :--- |
| **1** | **Pemrograman Berorientasi Objek (OOP) & Maven** | Perancangan struktur proyek Maven (`pom.xml`), penerapan 3 pilar OOP: **Encapsulation** (getter/setter & validasi), **Inheritance** (`Transaksi` ➔ `Pesanan` & `Pengeluaran`), dan **Polymorphism** (overriding & overloading). | `pom.xml`, `Pesanan.java`, `Transaksi.java` |
| **2** | **Java Swing GUI Builder (NetBeans)** | Pembuatan antarmuka desktop interaktif: halaman **Login Form** menggunakan NetBeans Design GUI Builder dan **Dashboard Sales Order** lengkap dengan form input bebas sekolah/barang, auto-kalkulasi Subtotal, PPN 11%, Grand Total, serta Cetak Invoice. | `LoginForm.java`, `LoginForm.form`, `Main.java` |
| **3** | **Database MySQL (XAMPP) & JDBC** | Pembuatan database `db_pengadaan_sekolah` (tabel `users` & `pesanan`), koneksi JDBC menggunakan `DriverManager`, serta fitur CRUD real-time (Simpan, Tampil di `JTable`, Refresh, dan Hapus data langsung ke MySQL). | `KoneksiDB.java`, `database_pengadaan_sekolah.sql` |
| **4** | **Object-Relational Mapping (Hibernate ORM)** | Menambahkan dependency `hibernate-core`, konfigurasi XML koneksi Hibernate ke MySQL, pembuatan `SessionFactory` helper, serta mapping class `Pesanan` menggunakan anotasi JPA (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`). | `hibernate.cfg.xml`, `HibernateUtil.java`, `Pesanan.java` |
| **5** | **Spring Framework & Spring DAO (`JdbcTemplate`)** | Menambahkan dependency `spring-context` & `spring-jdbc`, konfigurasi **IoC Container & Dependency Injection** (`dataSource`, `jdbcTemplate`, `pesananSpringDao`), serta implementasi operasi CRUD berbasis `JdbcTemplate` & `RowMapper`. | `applicationContext.xml`, `PesananSpringDao.java` |

---

## ✨ Fitur Utama Aplikasi
- 🔐 **Autentikasi Login:** Validasi username & password terhubung ke tabel `users` di MySQL XAMPP.
- 📝 **Input Sales Order Fleksibel:** Nama sekolah dan nama barang bisa **dipilih dari katalog** maupun **diketik bebas** secara kustom.
- 🧮 **Kalkulasi Pajak Otomatis:** Menghitung **Subtotal**, **PPN 11%**, dan **Grand Total** secara real-time.
- 🗄️ **Multi-Persistence Support:** Mendukung penyimpanan **JDBC Native**, **Hibernate ORM**, dan **Spring DAO (`JdbcTemplate`)**.
- 🧾 **Cetak Bukti Invoice:** Menampilkan pratinjau nota/invoice resmi siap cetak atas nama *CV. AXL SARANA MANDIRI*.

---

## 🛠️ Teknologi & Dependency (`pom.xml`)
- **Java Development Kit (JDK):** Java 17+
- **IDE:** Apache NetBeans IDE
- **Database:** MySQL / MariaDB (XAMPP)
- **Libraries (Maven):**
  - `mysql-connector-j` (`8.3.0`) — Driver MySQL
  - `flatlaf` (`3.4`) — Look & Feel Modern Swing
  - `hibernate-core` (`6.4.4.Final`) — ORM / JPA
  - `spring-context` & `spring-jdbc` (`6.1.6`) — Spring IoC & Spring DAO

---

## 🚀 Cara Menjalankan Proyek di NetBeans
1. **Jalankan XAMPP:** Buka XAMPP Control Panel lalu klik **Start** pada **Apache** dan **MySQL**.
2. **Import Database:**
   - Buka `http://localhost/phpmyadmin`
   - Buat database baru bernama `db_pengadaan_sekolah`
   - Import file `database_pengadaan_sekolah.sql`
3. **Buka Proyek di NetBeans:**
   - Pastikan nama folder proyek **tidak mengandung spasi** (contoh: `spring-dao-cobamvc`).
   - Klik **File ➔ Open Project...** ➔ pilih folder proyek yang berisi `pom.xml`.
   - Klik kanan nama proyek ➔ **Clean and Build** untuk mengunduh semua dependency Maven.
4. **Jalankan Program:**
   - **Tampilan GUI (Login ➔ Dashboard):** Klik kanan `LoginForm.java` ➔ **Run File** (`Shift + F6`)  
     *(Akun default: Username `admin` | Password `password`)*
   - **Uji Spring DAO (Konsol):** Klik kanan `PesananSpringDao.java` ➔ **Run File** (`Shift + F6`)
