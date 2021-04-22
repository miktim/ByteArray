package bytearray;

/**
 * ByteArrayTest, MIT (c) 2018 miktim@mail.ru
 *
 * Created: 2018-12-15
 */
import java.util.Arrays;
import org.miktim.ByteArray;

public class ByteArrayTest {

    public static void log(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) throws Exception {
        String title = "ByteArray test";
        byte b = (byte) 12;
        short s = (short) 0xf0f0;
        int i = 0xf0f0f0f0;
        long l = 0xf0f0f0f0f0f0f0f0L;
        byte[] t = title.getBytes();
        float f = 3.1416f;
        double d = (double) f;

        log(title + "\r\nPut data...");
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
            log("  Expected length FAIL!");
        }

        while (ba.length() < ByteArray.AUTOEXPAND_INCREMENT) {
            ba.put(ba.array());
        }

        if (ba.getPointer() != ba.length()) {
            log("  Resizable enabled FAIL!");
        }
        ba.setPointer(p);
        ba.truncate();
        ba.enableResizing(false);
        try {
            ba.put(b);
            log("  Resizable disabled FAIL!");
        } catch (Exception e) {
        }

        log("Get data...");
        ba = new ByteArray(ba.array());

        ba.get(0);
        ba.put(new byte[0]);
        if (ba.get() != b) {
            log("  Byte put/get FAIL!");
        }
        if (ba.getShort() != s) {
            log("  Short1 put/get FAIL!");
        }
        if (ba.getShort() != ~s) {
            log("  Short2 put/get FAIL!");
        }
        if (ba.getInt() != i) {
            log("  Int1 put/get FAIL!");
        }
        if (ba.getInt() != ~i) {
            log("  Int2 put/get FAIL!");
        }
        if (!Arrays.equals(ba.get(t.length), t)) {
            log("  Byte[] put/get FAIL!");
        }
        if (ba.getLong() != l) {
            log("  Long1 put/get FAIL!");
        }
        if (ba.getLong() != ~l) {
            log("  Long2 put/get FAIL!");
        }
        if (ba.getFloat() != f) {
            log("  Float1 put/get FAIL!");
        }
        if (ba.getFloat() != -f) {
            log("  Float2 put/get FAIL!");
        }
        if (ba.getDouble() != d) {
            log("  Double1 put/get FAIL!");
        }
        if (ba.getDouble() != -d) {
            log("  Double2 put/get FAIL!");
        }
        try {
            ba.get(1);
            log("  Get after data end FAIL!");
        } catch (Exception e) {
        }

        i = ByteArray.AUTOEXPAND_INCREMENT * 4 + 3;
        byte[] bytes = new byte[i];
        ba = new ByteArray();
        ba.put(new byte[3]);
        p = ba.put(bytes);
        ba.setPointer(3);
        bytes = ba.get(i);
        if (p != (i + 3) || bytes.length != i) {
            log(" byte[AUTOEXPAND_INCREMENT*4+3] put/get FAIL!");
        }

        log("Test completed");
    }

}
