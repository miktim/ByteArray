/**
 * ByteArray, MIT (c) 2018 miktim@mail.ru
 *
 * Relative put/get data into/from byte array
 * Data types: byte[], byte, short, int, long, float, double
 *
 * Usage: see ./ByteArrayTest.java
 *
 * Created: 2018-12-15
 */

package org.miktim;

import java.util.Arrays;

public class ByteArray {
  private byte[] data;
  private int dataPointer = 0; 
  private int dataLength = 0;
  private boolean dataResizable = true;
  
  public static final int AUTOEXPAND_INCREMENT = 64;

  public ByteArray() {
    data = new byte[AUTOEXPAND_INCREMENT];
  }

  public ByteArray(byte[] bytes) throws NullPointerException {
    data = bytes;
    dataLength = data.length;
    dataResizable = false;
  }
  
  public byte[] array() {
    if (dataLength == data.length) return data;
    return Arrays.copyOf(data, dataLength);
  }

  public int truncate() throws IllegalStateException {
    if(!dataResizable) throw new IllegalStateException(); //??? suitable java exception
    dataLength = dataPointer;
    return dataLength;
  }
   
  private int checkPointer(int pointer) throws ArrayIndexOutOfBoundsException {
    if (pointer < 0 || pointer > dataLength) 
      throw new ArrayIndexOutOfBoundsException();
    return pointer;
  }
  
  private int checkLength(int newPointer) throws ArrayIndexOutOfBoundsException {
    if (!dataResizable) checkPointer(newPointer);
    if (newPointer > data.length) {
      data = Arrays.copyOf(data, newPointer + AUTOEXPAND_INCREMENT);
    }
    if (dataLength < newPointer) dataLength = newPointer; 
    return newPointer;
  }
   
  public void enableResizing(boolean onoff) {
    dataResizable = onoff;
  }
  
  boolean isResizable() {
    return dataResizable;
  }

  public int length() {
    return dataLength;
  }

  public int getPointer() {
    return dataPointer;
  }
  
  public void setPointer(int newPointer) throws ArrayIndexOutOfBoundsException {
    dataPointer = checkPointer(newPointer);
  }
  
/*
 * PUT data methods. All methods return а new pointer (offset).
 */
  public int put(byte[] bytes) throws ArrayIndexOutOfBoundsException, NullPointerException {
    int newPointer = checkLength(dataPointer + bytes.length);
    System.arraycopy(bytes, 0, data, dataPointer, bytes.length);
    dataPointer = newPointer;
//    checkLength(dataPointer + bytes.length);
//    for(int i = 0; i < bytes.length; i++) data(dataPointer++) = bytes[i];
    return dataPointer;
  }
  
  public int put(byte b) throws ArrayIndexOutOfBoundsException {
    checkLength(dataPointer + 1);
    data[dataPointer++] = b;
    return dataPointer;
  }
  
  public int putShort(short s) throws ArrayIndexOutOfBoundsException {
    checkLength(dataPointer + 2);
    data[dataPointer++] = (byte) (s >>> 8);
    data[dataPointer++] = (byte) s;
    return dataPointer;
  }

  public int putInt(int i) throws ArrayIndexOutOfBoundsException {
    checkLength(dataPointer + 4);
    data[dataPointer++] = (byte)(i >>> 24);
    data[dataPointer++] = (byte)(i >>> 16);
    data[dataPointer++] = (byte)(i >>> 8);
    data[dataPointer++] = (byte) i ;
    return dataPointer;
  }
// IEE754
  public int putFloat(float f) throws ArrayIndexOutOfBoundsException {
//      return putInt(java.lang.Float.floatToIntBits(f)); 
    return putInt(java.lang.Float.floatToRawIntBits(f)); // preserving NaN
  }
  
  public int putLong(long i) throws ArrayIndexOutOfBoundsException {
    checkLength(dataPointer + 8);
    data[dataPointer++] = (byte)(i >>> 56);
    data[dataPointer++] = (byte)(i >>> 48);
    data[dataPointer++] = (byte)(i >>> 40);
    data[dataPointer++] = (byte)(i >>> 32);
    data[dataPointer++] = (byte)(i >>> 24);
    data[dataPointer++] = (byte)(i >>> 16);
    data[dataPointer++] = (byte)(i >>> 8);
    data[dataPointer++] = (byte) i;
    return dataPointer;
  }
// IEE754 
  public int putDouble(double d) throws ArrayIndexOutOfBoundsException {
//    return putLong(java.lang.Double.doubleToLongBits(d)); 
    return putLong(java.lang.Double.doubleToRawLongBits(d)); //preserving NaN
  }
/*
 * GET data methods.
 */
  public byte[] get(int len) 
          throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
    if (len < 0) throw new IllegalArgumentException();
    int oldPointer = dataPointer;
    dataPointer = checkPointer(dataPointer + len);
    return Arrays.copyOfRange(data, oldPointer, dataPointer);
  }
  
  public byte get() throws ArrayIndexOutOfBoundsException {
    checkPointer(dataPointer + 1) ;
    return data[dataPointer++];
  }
  
  public short getShort() throws ArrayIndexOutOfBoundsException {
    checkPointer(dataPointer + 2);
    return (short)((data[dataPointer++] << 8) + (data[dataPointer++] & 0xff));
  }

  public int getInt() throws ArrayIndexOutOfBoundsException {
    checkPointer(dataPointer + 4);
    return (data[dataPointer++] << 24)
	  + ((data[dataPointer++] & 0xff) << 16)
	  + ((data[dataPointer++] & 0xff) << 8)
	  + (data[dataPointer++]  & 0xff);
  }
  
  public float getFloat() throws ArrayIndexOutOfBoundsException {
    return java.lang.Float.intBitsToFloat(getInt());
  }
  
  public long getLong() throws ArrayIndexOutOfBoundsException {
    checkPointer(dataPointer + 8);
    return ((long)data[dataPointer++] << 56)
	  + ((long)(data[dataPointer++] & 0xff) << 48)
	  + ((long)(data[dataPointer++] & 0xff) << 40)
	  + ((long)(data[dataPointer++] & 0xff) << 32)
	  + ((long)(data[dataPointer++] & 0xff) << 24)
	  + (long)((data[dataPointer++] & 0xff) << 16)
	  + (long)((data[dataPointer++] & 0xff) << 8)
	  + (long)(data[dataPointer++] & 0xff);
  }

  public double getDouble() throws ArrayIndexOutOfBoundsException {
    return java.lang.Double.longBitsToDouble(getLong());
  }
 
}
