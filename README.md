# README

Proyek ini membutuhkan beberapa berkas konfigurasi lokal agar dapat berjalan dengan benar (Firebase Admin SDK, Google Services, dan kunci Google Maps). Ikuti langkah-langkah di bawah.

## Prasyarat
- Java 21 (JDK 21)
- Kotlin 2.2
- Android SDK 36 (Target SDK 36)
- Android Studio / IntelliJ IDEA terbaru
- Akses ke Google Cloud Console dan Firebase Console

## Berkas yang Harus Ditambahkan

1) Tambahkan kredensial Firebase Admin SDK di root proyek:
- File: `serviceAccountKey.json`
- Letakkan di folder root proyek (sejajar dengan settings/build file utama)

2) Tambahkan kunci Google Maps di root proyek:
- Buat file `secrets.properties` di root proyek
- Isi dengan:
```properties
MAPS_API_KEY=API_KEY_DARI_GOOGLE_CLOUD_CONSOLE
```

Ganti nilai setelah tanda sama dengan dengan kunci API Maps milik Anda (jangan gunakan tanda kutip).

3) Tambahkan konfigurasi Firebase untuk aplikasi Android:
- File `google-services.json` untuk module customer
  - Letakkan di: `/customer-app/google-services.json`
- File `google-services.json` untuk module merchant
  - Letakkan di: `/merchant-app/google-services.json`

Catatan:
- Kedua module membutuhkan file `google-services.json` masing-masing (biasanya berasal dari dua app di Firebase Console dengan applicationId yang berbeda).
- Pastikan `serviceAccountKey.json` dan `google-services.json` sesuai dengan project Firebase yang sama.

## Struktur Direktori (ilustrasi)
```shell script
.
├─ serviceAccountKey.json
├─ secrets.properties
├─ customer-app/
│  └─ google-services.json
└─ merchant-app/
   └─ google-services.json
```


## Langkah Setup Cepat
1) Dapatkan `serviceAccountKey.json` dari Firebase Console (Project Settings → Service accounts → Generate new private key), lalu simpan di root proyek.
2) Dapatkan `google-services.json` untuk masing-masing aplikasi Android di Firebase Console (Project Settings → Your apps), lalu simpan pada folder module terkait:
  - `/customer-app/google-services.json`
  - `/merchant-app/google-services.json`
3) Buat `secrets.properties` di root, isi dengan:
```properties
MAPS_API_KEY=API_KEY_DARI_GOOGLE_CLOUD_CONSOLE
```

4) Pastikan Google Maps API diaktifkan di Google Cloud Console untuk kunci API tersebut.
5) Sinkronkan project di IDE agar dependensi dan variabel rahasia terdeteksi.

## Menjalankan Proyek
- Buka proyek di Android Studio/IntelliJ IDEA.
- Sync Gradle.
- Pilih module target dan jalankan pada emulator/perangkat.

## Keamanan
- Jangan commit `serviceAccountKey.json`, `google-services.json`, dan `secrets.properties` ke VCS publik.
- Tambahkan pola berikut ke `.gitignore` jika diperlukan:
```.gitignore (gitignore)
serviceAccountKey.json
secrets.properties
**/google-services.json
```


## Troubleshooting
- Error kredensial Firebase: pastikan `serviceAccountKey.json` berada di root dan valid.
- Error Google Services: pastikan setiap module memiliki `google-services.json` yang sesuai applicationId-nya.
- Error Maps API key: pastikan `MAPS_API_KEY` benar, API yang diperlukan aktif, dan pembatasan API (API restrictions) tidak memblokir penggunaan pada aplikasi ini.

Jika Anda membutuhkan contoh integrasi lebih lanjut (misalnya cara membaca `MAPS_API_KEY` dari `secrets.properties` di Gradle/Manifest), beri tahu saya modul dan setup Gradle yang digunakan.