CREATE DATABASE IF NOT EXISTS umkm_serba_10000
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE umkm_serba_10000;

CREATE TABLE IF NOT EXISTS bahan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    satuan VARCHAR(30) NOT NULL DEFAULT 'unit',
    harga DECIMAL(14,2) NOT NULL,
    CONSTRAINT chk_bahan_harga CHECK (harga >= 0),
    UNIQUE KEY uq_bahan_nama (nama)
);

CREATE TABLE IF NOT EXISTS produk (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    harga DECIMAL(14,2) NOT NULL DEFAULT 10000,
    CONSTRAINT chk_produk_harga CHECK (harga >= 0),
    UNIQUE KEY uq_produk_nama (nama)
);

CREATE TABLE IF NOT EXISTS produk_bahan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produk_id INT NOT NULL,
    bahan_id INT NOT NULL,
    jumlah_pemakaian DECIMAL(12,3) NOT NULL DEFAULT 1,
    CONSTRAINT chk_produk_bahan_jumlah CHECK (jumlah_pemakaian > 0),
    CONSTRAINT uq_produk_bahan UNIQUE (produk_id, bahan_id),
    CONSTRAINT fk_produk_bahan_produk FOREIGN KEY (produk_id)
        REFERENCES produk(id) ON DELETE CASCADE,
    CONSTRAINT fk_produk_bahan_bahan FOREIGN KEY (bahan_id)
        REFERENCES bahan(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS transaksi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tanggal DATE NOT NULL,
    produk_id INT NOT NULL,
    jumlah INT NOT NULL,
    harga_jual_satuan DECIMAL(14,2) NOT NULL,
    modal_satuan DECIMAL(14,2) NOT NULL DEFAULT 0,
    keterangan VARCHAR(255) NOT NULL DEFAULT '',
    CONSTRAINT chk_transaksi_jumlah CHECK (jumlah > 0),
    CONSTRAINT uq_transaksi_tanggal_produk UNIQUE (tanggal, produk_id),
    CONSTRAINT fk_transaksi_produk FOREIGN KEY (produk_id)
        REFERENCES produk(id) ON DELETE RESTRICT,
    INDEX idx_transaksi_tanggal (tanggal)
);
