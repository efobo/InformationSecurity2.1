import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BigInteger N = new BigInteger("85609460573249");
        BigInteger e = new BigInteger("2448539");

        String[] C = {
                "523815866990",
                "26788001211021",
                "34569932939126",
                "85581094055910",
                "23256663175806",
                "62527703621248",
                "7622521689363",
                "32655715523491",
                "81242663069415",
                "60438288306445",
                "73937478628138",
                "7793112362388"

        };

        String decryptedStr = decrypt(N, e, C);
        System.out.println("decrypted string:");
        System.out.println(decryptedStr);

    }

    static String decrypt(BigInteger N, BigInteger e, String[] C) {
        System.out.println("N = " + N);
        System.out.println("e = " + e);
        System.out.println("C = ");
        for (String c : C) {
            System.out.println(c);
        }
        BigInteger n = N.sqrt().add(BigInteger.ONE);
        System.out.println("n = [sqrt(N)] + 1 = " + n);
        int i = 0;
        BigInteger t;
        BigInteger sqrt_w;
        while (true) {
            i++;
            t = n.add(BigInteger.valueOf(i));
            System.out.println("t" + i + " = n + " + i + " = " + t);
            BigInteger w = t.pow(2).subtract(N);
            System.out.println("w" + i + " = t" + i + "^2 - N = " + w);
            float sqrt_w_double = (float) Math.sqrt(w.longValue());
            if (sqrt_w_double % 1 == 0) {
                sqrt_w = BigDecimal.valueOf(sqrt_w_double).toBigInteger();
                //sqrt_w = (long) sqrt_d1_double;
                System.out.println("sqrt(w" + i + ") = " + sqrt_w);
                break;
            } else System.out.println("sqrt(w" + i + ") = " + sqrt_w_double + " - error");

        }

        //p = t + sqrt_w
        BigInteger p = t.add(sqrt_w);
        //q = t - sqrt_w
        BigInteger q = t.subtract(sqrt_w);
        //phi = (p-1) * (q-1)
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        //long d = BigInteger.valueOf(e).modInverse(BigInteger.valueOf(phi)).longValue();
        BigInteger d = e.modInverse(phi);

        System.out.println("p = t + sqrt(d" + i + ") = " + p);
        System.out.println("q = t - sqrt(d)" + i + " = " + q);
        System.out.println("phi = (p - 1) * (q - 1) = " + phi);
        System.out.println("d = e^(-1) mod phi = " + d);

        String decryptStr = "";
        i = 0;
        for (String c : C) {
            i++;
            //c^d
            BigInteger m = (new BigInteger(c)).modPow(d, N);
            System.out.println("m" + i + " = " + m);
            String part = decodeWindows1251(m);
            System.out.println("m" + i + " = C[" + i + "]^d mod N = " + c + "^" + d + " mod " + N + " = " + m + " => text(" + m + ") = " + part);
            decryptStr += part;



        }

        return decryptStr;
    }


    public static String decodeWindows1251(BigInteger encodedValue) {
        byte[] bytes = encodedValue.toByteArray();

        List<Byte> byteList = new ArrayList<>();
        for (byte b : bytes) {
            if (b != 0) {
                byteList.add(b);
            }
        }
        bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        try {
            return new String(bytes, Charset.forName("Windows-1251"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}