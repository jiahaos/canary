package com.jaf.tools.qr;

import com.beust.jcommander.internal.Lists;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class QRCode {

    static String CHARSET = "UTF-8"; // or "ISO-8859-1"

    public static void createQRCode(String qrCodeData, String filePath, int qrCodeheight, int qrCodewidth) throws IOException, WriterException {
        createQRCode(qrCodeData, filePath, getDefaultHintMap(), qrCodeheight, qrCodewidth);
    }

    public static byte[] createQRCode(String qrCodeData, int qrCodeheight, int qrCodewidth) throws IOException, WriterException {
        return createQRCode(qrCodeData, getDefaultHintMap(), qrCodeheight, qrCodewidth);
    }

    public static void createQRCode(String qrCodeData, String filePath, Map hintMap, int qrCodeheight, int qrCodewidth)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter()
                .encode(new String(qrCodeData.getBytes(CHARSET), CHARSET),
                        BarcodeFormat.QR_CODE,
                        qrCodewidth,
                        qrCodeheight,
                        hintMap);
        MatrixToImageWriter.writeToPath(matrix, filePath.substring(filePath
                .lastIndexOf('.') + 1), new File(filePath).toPath());
    }

    public static byte[] createQRCode(String qrCodeData, Map hintMap, int qrCodeheight, int qrCodewidth)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter()
                .encode(new String(qrCodeData.getBytes(CHARSET), CHARSET),
                        BarcodeFormat.QR_CODE,
                        qrCodewidth,
                        qrCodeheight,
                        hintMap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(
                MatrixToImageWriter.toBufferedImage(matrix),
                "jpg",
                ImageIO.createImageOutputStream(byteArrayOutputStream));
        return byteArrayOutputStream.toByteArray();
    }

    public static String readQRCode(String filePath) throws IOException, NotFoundException {
        return readQRCode(filePath, getDefaultHintMap());
    }

    public static String readQRCode(byte[] bytes) throws IOException, NotFoundException {
        return readQRCode(bytes, getDefaultHintMap());
    }

    public static String readQRCode(byte[] bytes, int borderSize) throws IOException, NotFoundException {
        return readQRCode(bytes, getDefaultHintMap(), borderSize);
    }

    public static String readQRCode(String filePath, Map hintMap) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(filePath));
        return readQRCode(bufferedImage, hintMap);
    }


    public static String readQRCode(byte[] bytes, Map hintMap) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        return readQRCode(bufferedImage, hintMap);
    }

    public static String readQRCode(byte[] bytes, Map hintMap, int borderSize) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        BufferedImage fixedImage = fixedImage(bufferedImage, borderSize);
        return readQRCode(fixedImage, hintMap);
    }

    private static String readQRCode(BufferedImage bufferedImage, Map hintMap) throws IOException, NotFoundException {
        // ImageIO.write(bufferedImage, "png", new File("/Users/hsk/Pictures/demo1" + System.currentTimeMillis() + ".png"));
        // ImageIO.write(fixedImage, "png", new File("/Users/hsk/Pictures/demo2" + System.currentTimeMillis() + ".png"));
        BinaryBitmap binaryBitmap = new BinaryBitmap(
                new HybridBinarizer(
                        new BufferedImageLuminanceSource(
                                bufferedImage
                        )
                )
        );
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
        return qrCodeResult.getText();
    }

    public static BufferedImage fixedImage(BufferedImage prevImage, int borderSize) throws IOException {
        int width = prevImage.getWidth();
        int height = prevImage.getHeight();
        if (width == height) {
            return prevImage;
        }
        CropImageFilter cropFilter = new CropImageFilter(borderSize, borderSize, width - 2 * borderSize, width - 2 * borderSize);
        Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(prevImage.getSource(), cropFilter));
        BufferedImage fixedImage = new BufferedImage(width - 2 * borderSize, width - 2 * borderSize, BufferedImage.TYPE_INT_RGB);
        Graphics g = fixedImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return fixedImage;
    }

    private static Map getDefaultHintMap() {
        Map hintMap = new HashMap();
//        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//        hintMap.put(DecodeHintType.PURE_BARCODE, true);
        hintMap.put(DecodeHintType.PURE_BARCODE, true);
        hintMap.put(DecodeHintType.POSSIBLE_FORMATS, Lists.newArrayList(BarcodeFormat.QR_CODE));
        return hintMap;
    }
}
