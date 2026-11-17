USE umkm_serba_10000;

-- Jalankan satu kali setelah membuat cadangan database versi sebelumnya.
ALTER TABLE bahan
    ADD COLUMN satuan VARCHAR(30) NOT NULL DEFAULT 'unit';

ALTER TABLE produk_bahan
    ADD COLUMN jumlah_pemakaian DECIMAL(12,3) NOT NULL DEFAULT 1;

ALTER TABLE transaksi
    ADD COLUMN harga_jual_satuan DECIMAL(14,2) NULL,
    ADD COLUMN modal_satuan DECIMAL(14,2) NULL;

-- Data lama memakai harga produk dan bahan yang saat ini tersimpan.
UPDATE transaksi t
JOIN produk p ON p.id = t.produk_id
SET t.harga_jual_satuan = p.harga;

UPDATE transaksi t
LEFT JOIN (
    SELECT pb.produk_id, SUM(pb.jumlah_pemakaian * b.harga) AS modal
    FROM produk_bahan pb
    JOIN bahan b ON b.id = pb.bahan_id
    GROUP BY pb.produk_id
) biaya ON biaya.produk_id = t.produk_id
SET t.modal_satuan = COALESCE(biaya.modal, 0);

-- Satukan transaksi produk/tanggal yang sebelumnya tersimpan sebagai baris terpisah.
CREATE TEMPORARY TABLE transaksi_ringkas AS
SELECT MIN(id) AS id_utama, tanggal, produk_id,
       SUM(jumlah) AS jumlah_total,
       MAX(harga_jual_satuan) AS harga_satuan,
       MAX(modal_satuan) AS modal_satuan,
       GROUP_CONCAT(DISTINCT NULLIF(keterangan, '') SEPARATOR ' | ') AS keterangan
FROM transaksi
GROUP BY tanggal, produk_id;

UPDATE transaksi t
JOIN transaksi_ringkas r ON r.id_utama = t.id
SET t.jumlah = r.jumlah_total,
    t.harga_jual_satuan = r.harga_satuan,
    t.modal_satuan = r.modal_satuan,
    t.keterangan = COALESCE(r.keterangan, '');

DELETE t FROM transaksi t
LEFT JOIN transaksi_ringkas r ON r.id_utama = t.id
WHERE r.id_utama IS NULL;

DROP TEMPORARY TABLE transaksi_ringkas;

ALTER TABLE transaksi
    MODIFY harga_jual_satuan DECIMAL(14,2) NOT NULL,
    MODIFY modal_satuan DECIMAL(14,2) NOT NULL DEFAULT 0,
    ADD CONSTRAINT uq_transaksi_tanggal_produk UNIQUE (tanggal, produk_id);
