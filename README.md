# Chronic Wound Dataset Application
Chronic Wound Dataset merupakan aplikasi berbasis Android yang berfungsi untuk mengumpulkan citra groundtruth dari tepi dan warna luka luka kronik. Pengumpulan citra groundtruth ini dapat bermanfaat untuk keperluan riset dan penelitian terkait luka kronik.

# Product Backlog
| NO |	STORY	| USER ROLE	| STORY LEVEL	| FEATURES LEVEL | SPRINT NO. |
| -- | ------ | --------- | ----------- | -------------- | ---------- |
| 1	| Saya dapat membuat akun	| User	| Front-End	| 1	| 1 |
| 2	| Saya dapat masuk ke akun yang sudah saya buat |	User | Front-End	| 1 |	1 |
| 3	| Pemanggilan rest POST untuk menyimpan data user setelah melakukan pembuatan akun	| Sistem | 	Back-End	| 1 |	1 |
| 4	| Pemanggilan rest GET  untuk keperluan masuk dan keluar akun |	Sistem	| Back-End	| 1	 | 1 |
| 5	| Saya dapat keluar dari akun saya	| User	| Front-End	| 1	| 2 |
| 6	| Saya dapat melihat detail profil dari akun yang telah saya buat	| User	| Front-End |	1	| 2 |
| 7	| Saya dapat mengubah foto profil dari akun yang telah saya buat	| User |	Front-End	| 1	| 2 |
| 8	| Saya dapat melakukan anotasi warna luka kronik |	User	| Front-End	| 2 |	3 |
| 9	| Saya dapat mengunggah foto luka kronik melalui kamera maupun galeri |	User	| Front-End	| 2	| 3 |
| 10	| Saya dapat menyimpan hasil anotasi warna luka kronik	| User	| Front-End	| 2	| 3 |
| 11	| Pemanggilan rest POST untuk menyimpan secara otomatis foto luka yang telah diunggah ke database	| Sistem	| Back-End	| 1	| 3 |
| 12 |	Saya dapat melakukan anotasi tepi luka kronik	| User	| Front-End	| 2 |	4 |
| 13 | Saya dapat menyimpan hasil anotasi tepi luka kronik |	User	| Front-End	| 1 |	4 |
| 14	| Pemanggilan rest POST untuk menyimpan secara otomatis hasil anotasi warna luka yang telah disimpan ke database | Sistem | Back-End |	2 |	4
| 15	| Pemanggilan rest POST untuk menyimpan secara otomatis hasil anotasi tepi luka yang telah disimpan ke database	| Sistem	| Back-End | 1 | 	4 |
| 16	| Saya dapat melihat kembali hasil anotasi warna luka kronik milik saya pribadi	| User |	Front-End |	2 |	5 |
| 17	| Saya dapat melihat kembali hasil anotasi tepi luka kronik milik saya pribadi	| User |	Front-End |	1 |	5 |
| 18	| Pemanggilan rest GET untuk menampilkan hasil anotasi warna luka pribadi milik user	| Sistem |	Back-End |	1 |	5 |
| 19	| Pemanggilan rest GET untuk menampilkan hasil anotasi tepi luka pribadi milik user	| Sistem |	Back-End |	1 | 5 |
| 20 |	Saya dapat melihat & mengunduh dataset publik citra groundtruth warna luka kronik |	User | Front-End |	2 |	6 |
| 21 |	Saya dapat melihat & mengunduh dataset publik citra groundtruth tepi luka kronik | User |	Front-End |	2	| 6 |
| 22	| Pemanggilan rest GET untuk akses unduh dan menampilkan dataset publik citra groundtruth warna luka kronik |	Sistem	| Back-End	| 2	| 6 |
| 23	| Pemanggilan rest GET untuk akses unduh dan menampilkan dataset publik citra groundtruth tepi luka kronik	| Sistem |	Back-End |	1	| 6 |
| 24	| Saya dapat melihat & mengunduh dataset publik foto luka kronik	 | User |	Front-End	| 1	| 7 |
| 25	| Pemanggilan rest GET untuk akses unduh dan menampilkan dataset publik foto luka kronik	| Sistem	| Back-End	| 1	| 7 |
| 26	| Mencatat log activity user	| Sistem	| Back-End	| 1	| 7 |

keterangan:<br>
2 = high priority <br>
1 = low priority
