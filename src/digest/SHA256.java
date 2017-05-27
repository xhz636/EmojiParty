package digest;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class SHA256 {

    public static String getSHA256(File file){
        String result = new String();
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] temp = new byte[1024];
            int size;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            in.close();
            byte[] content = out.toByteArray();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content);
            result = printHex(hash);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    private static String printHex(byte[] data) {
        String[] Hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        StringBuffer HexStr = new StringBuffer();
        for (int i = 0 ; i < data.length; i++) {
            int num = data[i] & 0xFF;
            HexStr.append(Hex[num / 16]);
            HexStr.append(Hex[num % 16]);
        }
        return HexStr.toString();
    }

}
