package mk.ukim.finki.wayzi.service.domain;

public interface QRCodeService {
    String generateQRCodeBase64(String text, int width, int height);
}
