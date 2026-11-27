package com.pengadaan.salesorder.service;

import com.pengadaan.salesorder.model.ItemBarang;
import com.pengadaan.salesorder.model.Pesanan;
import com.pengadaan.salesorder.util.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Generator Lembar Kerja Excel (.xlsx) resmi menggunakan Apache POI.
 */
public class ExcelGenerator {

    public static void generateExcel(Pesanan pesanan, File fileTarget) throws Exception {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(fileTarget)) {

            Sheet sheet = workbook.createSheet("Sales Order " + pesanan.getNomorPesanan());

            // Fonts & Styles
            Font fontTitle = workbook.createFont();
            fontTitle.setBold(true);
            fontTitle.setFontHeightInPoints((short) 14);

            CellStyle styleTitle = workbook.createCellStyle();
            styleTitle.setFont(fontTitle);

            Font fontBold = workbook.createFont();
            fontBold.setBold(true);

            CellStyle styleHeader = workbook.createCellStyle();
            styleHeader.setFont(fontBold);
            styleHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleHeader.setBorderBottom(BorderStyle.THIN);
            styleHeader.setBorderTop(BorderStyle.THIN);
            styleHeader.setBorderLeft(BorderStyle.THIN);
            styleHeader.setBorderRight(BorderStyle.THIN);
            styleHeader.setAlignment(HorizontalAlignment.CENTER);

            CellStyle styleCurrency = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            styleCurrency.setDataFormat(format.getFormat("Rp #,##0"));
            styleCurrency.setBorderBottom(BorderStyle.THIN);
            styleCurrency.setBorderTop(BorderStyle.THIN);
            styleCurrency.setBorderLeft(BorderStyle.THIN);
            styleCurrency.setBorderRight(BorderStyle.THIN);

            CellStyle styleBorder = workbook.createCellStyle();
            styleBorder.setBorderBottom(BorderStyle.THIN);
            styleBorder.setBorderTop(BorderStyle.THIN);
            styleBorder.setBorderLeft(BorderStyle.THIN);
            styleBorder.setBorderRight(BorderStyle.THIN);

            // 1. Judul & Info
            Row row0 = sheet.createRow(0);
            Cell cell0 = row0.createCell(0);
            cell0.setCellValue(DokumenService.NAMA_PERUSAHAAN);
            cell0.setCellStyle(styleTitle);

            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("DOKUMEN SALES ORDER DAN RINCIAN PENGADAAN");

            Row row3 = sheet.createRow(3);
            row3.createCell(0).setCellValue("Nomor Pesanan:");
            row3.createCell(1).setCellValue(pesanan.getNomorPesanan());
            row3.createCell(3).setCellValue("Nama Sekolah:");
            row3.createCell(4).setCellValue(pesanan.getSekolah().getNamaSekolah());

            Row row4 = sheet.createRow(4);
            row4.createCell(0).setCellValue("Tanggal:");
            row4.createCell(1).setCellValue(DateUtil.formatKeIndonesia(pesanan.getTanggalPesanan()));
            row4.createCell(3).setCellValue("Penerima/Kepsek:");
            row4.createCell(4).setCellValue(pesanan.getSekolah().getNamaPenerima());

            Row row5 = sheet.createRow(5);
            row5.createCell(0).setCellValue("Status Pengiriman:");
            row5.createCell(1).setCellValue(pesanan.getStatusPengiriman().getLabel());

            // 2. Header Tabel Item
            Row rowTable = sheet.createRow(7);
            String[] headers = {"No", "Nama Barang / Spesifikasi", "Kuantitas", "Satuan", "Harga Satuan (Rp)", "Jumlah (Rp)"};
            for (int i = 0; i < headers.length; i++) {
                Cell c = rowTable.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(styleHeader);
            }

            // 3. Isi Item
            int rowIndex = 8;
            int no = 1;
            for (ItemBarang item : pesanan.getItems()) {
                Row row = sheet.createRow(rowIndex++);
                Cell cNo = row.createCell(0);
                cNo.setCellValue(no++);
                cNo.setCellStyle(styleBorder);

                Cell cNama = row.createCell(1);
                cNama.setCellValue(item.getNamaBarang());
                cNama.setCellStyle(styleBorder);

                Cell cQty = row.createCell(2);
                cQty.setCellValue(item.getQty());
                cQty.setCellStyle(styleBorder);

                Cell cSatuan = row.createCell(3);
                cSatuan.setCellValue(item.getSatuan());
                cSatuan.setCellStyle(styleBorder);

                Cell cHarga = row.createCell(4);
                cHarga.setCellValue(item.getHargaSatuan());
                cHarga.setCellStyle(styleCurrency);

                Cell cJumlah = row.createCell(5);
                cJumlah.setCellValue(item.getJumlah());
                cJumlah.setCellStyle(styleCurrency);
            }

            // 4. Kalkulasi Subtotal, PPN, Total
            Row rowSub = sheet.createRow(rowIndex++);
            rowSub.createCell(4).setCellValue("Subtotal:");
            Cell cSub = rowSub.createCell(5);
            cSub.setCellValue(pesanan.getSubtotal());
            cSub.setCellStyle(styleCurrency);

            Row rowPpn = sheet.createRow(rowIndex++);
            rowPpn.createCell(4).setCellValue("PPN (" + pesanan.getTarifPpnPersen() + "%):");
            Cell cPpn = rowPpn.createCell(5);
            cPpn.setCellValue(pesanan.getNominalPpn());
            cPpn.setCellStyle(styleCurrency);

            Row rowPph = sheet.createRow(rowIndex++);
            rowPph.createCell(4).setCellValue("PPh (" + pesanan.getTarifPphPersen() + "%):");
            Cell cPph = rowPph.createCell(5);
            cPph.setCellValue(pesanan.getNominalPph());
            cPph.setCellStyle(styleCurrency);

            Row rowTot = sheet.createRow(rowIndex++);
            rowTot.createCell(4).setCellValue("TOTAL:");
            Cell cTot = rowTot.createCell(5);
            cTot.setCellValue(pesanan.getTotal());
            cTot.setCellStyle(styleCurrency);

            Row rowTerbilang = sheet.createRow(rowIndex + 1);
            rowTerbilang.createCell(0).setCellValue("Terbilang: # " + pesanan.getTerbilang() + " #");

            // Auto-size kolom
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(fos);
        }
    }
}