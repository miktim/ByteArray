/**
 * ByteArrayTest, MIT (c) 2018 miktim@mail.ru
 *
 * Created: 2018-12-15
 */


import java.util.Arrays;
import org.miktim.ByteArray;

public class ByteArrayTest {

    public static void main(String[] args) throws Exception {
        String title = "ByteArray test";
        byte b = (byte) 12;
        short s = (short) 0xf0f0;
        int i = 0xf0f0f0f0;
        long l = 0xf0f0f0f0f0f0f0f0L;
        byte[] t = title.getBytes();
        float f = 3.1416f;
        double d = (double) f;

        System.out.println(title + "\r\nPut data...");
        ByteArray ba = new ByteArray();

        ba.put(b);
        ba.putShort(s);
        ba.putShort((short) ~s);
        ba.putInt(i);
        ba.putInt(~i);
        ba.put(t);
        ba.putLong(l);
        ba.putLong(~l);
        ba.putFloat(f);
        ba.putFloat(-f);
        ba.putDouble(d);
        int p = ba.putDouble(-d);

        int expectedLength = 1 + 2 + 2 + 4 + 4 + t.length + 8 + 8 + 4 + 4 + 8 + 8;
        if (ba.length() != expectedLength) {
            System.out.println("  Expected length FAIL!");
        }

        while (ba.length() < ByteArray.AUTOEXPAND_INCREMENT) {
            ba.put(ba.array());
        }

        if (ba.getPointer() != ba.length()) {
            System.out.println("  Resizable enabled FAIL!");
        }
        ba.setPointer(p);
        ba.truncate();
        ba.enableResizing(false);
        try {
            ba.put(b);
            System.out.println("  Resizable disabled FAIL!");
        } catch (Exception e) {
        }

        System.out.println("Get data...");
        ba = new ByteArray(ba.array());

        ba.get(0);
        ba.put(new byte[0]);
        if (ba.get() != b) {
            System.out.println("  Byte put/get FAIL!");
        }
        if (ba.getShort() != s) {
            System.out.println("  Short1 put/get FAIL!");
        }
        if (ba.getShort() != ~s) {
            System.out.println("  Short2 put/get FAIL!");
        }
        if (ba.getInt() != i) {
            System.out.println("  Int1 put/get FAIL!");
        }
        if (ba.getInt() != ~i) {
            System.out.println("  Int2 put/get FAIL!");
        }
        if (!Arrays.equals(ba.get(t.length), t)) {
            System.out.println("  Byte[] put/get FAIL!");
        }
        if (ba.getLong() != l) {
            System.out.println("  Long1 put/get FAIL!");
        }
        if (ba.getLong() != ~l) {
            System.out.println("  Long2 put/get FAIL!");
        }
        if (ba.getFloat() != f) {
            System.out.println("  Float1 put/get FAIL!");
        }
        if (ba.getFloat() != -f) {
            System.out.println("  Float2 put/get FAIL!");
        }
        if (ba.getDouble() != d) {
            System.out.println("  Double1 put/get FAIL!");
        }
        if (ba.getDouble() != -d) {
            System.out.println("  Double2 put/get FAIL!");
        }
        try {
            ba.get(1);
            System.out.println("  Get after data end FAIL!");
        } catch (Exception e) {
        }
        System.out.println("Test completed");
    }

}
