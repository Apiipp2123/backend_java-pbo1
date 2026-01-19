CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(10) NOT NULL,
    nama_lengkap VARCHAR(100),
    alamat TEXT,
    telepon VARCHAR(20)
);

CREATE TABLE `kendaraan` (
    `id_kendaraan` bigint(20) NOT NULL AUTO_INCREMENT,
    `jenis` enum('mobil', 'motor') NOT NULL,
    `merk` varchar(50) NOT NULL,
    `model` varchar(50) NOT NULL,
    `tahun` int(11) DEFAULT NULL,
    `no_plat` varchar(20) DEFAULT NULL,
    `status` enum(
        'tersedia',
        'disewa',
        'dibeli'
    ) DEFAULT 'tersedia',
    `harga_sewa_per_hari` decimal(10, 2) DEFAULT NULL,
    `harga_beli` decimal(10, 2) DEFAULT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`id_kendaraan`),
    UNIQUE KEY `no_plat` (`no_plat`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `pelanggan` (
    `id_pelanggan` bigint(20) NOT NULL AUTO_INCREMENT,
    `nama` varchar(100) NOT NULL,
    `alamat` text DEFAULT NULL,
    `telepon` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id_pelanggan`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

 CREATE TABLE penjualan (                                       
     id bigint(20) NOT NULL AUTO_INCREMENT,                     
     id_kendaraan bigint(20) NOT NULL,                          
     id_user INT NOT NULL,  -- <--- INI YG BARU                 
     tgl_penjualan date NOT NULL,                               
     harga double NOT NULL,                                     
     PRIMARY KEY (id),                                          
     KEY id_kendaraan (id_kendaraan),                           
     KEY id_user (id_user),                                     
 FOREIGN KEY (id_kendaraan) REFERENCES kendaraan (id_kendaraan),
 FOREIGN KEY (id_user) REFERENCES users (id)                    
 );                                                             

 CREATE TABLE sewa (                                                                          
     id_sewa bigint(20) NOT NULL AUTO_INCREMENT,                                              
     id_kendaraan bigint(20) NOT NULL,                                                        
     id_user INT NOT NULL,           
     tgl_sewa date NOT NULL,                                                                  
     tgl_pengembalian date NOT NULL,                                                          
     ttl_harga decimal(10, 2) NOT NULL,                                                       
     stts enum('aktif','selesai','dibatalkan') DEFAULT 'aktif',denda DECIMAL(10, 2) DEFAULT 0,
 PRIMARY KEY (id_sewa),                                                                       
     KEY id_kendaraan (id_kendaraan),                                                         
     KEY id_user (id_user),                                                                   
 FOREIGN KEY (id_kendaraan) REFERENCES kendaraan (id_kendaraan),                              
 FOREIGN KEY (id_user) REFERENCES users (id)                                                  
 );                                                                                           

-- Data Dummy
INSERT INTO users (username, email, password, role, nama_lengkap, alamat, telepon) 
VALUES ('admin', 'admin@rental.com', '123', 'ADMIN', 'Super Administrator', 'Kantor Pusat', '08123456789');
INSERT INTO users (username, email, password, role, nama_lengkap, alamat, telepon) 
VALUES ('budi', 'budi@gmail.com', '123', 'USER', 'Budi Santoso', 'Jl. Sudirman No 1', '08567891234');