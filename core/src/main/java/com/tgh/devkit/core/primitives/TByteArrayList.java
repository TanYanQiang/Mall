package com.tgh.devkit.core.primitives;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * 基本类型 byte 的可变数组
 * Created by albert on 15/11/19.
 */
public class TByteArrayList implements Serializable, Cloneable {
    protected transient byte[] _data;
    protected transient int _pos;
    protected static final int DEFAULT_CAPACITY = 10;

    public TByteArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public TByteArrayList(int capacity) {
        this._data = new byte[capacity];
        this._pos = 0;
    }

    public TByteArrayList(byte[] values) {
        this(Math.max(values.length, DEFAULT_CAPACITY));
        add(values);
    }

    public void ensureCapacity(int capacity) {
        if (capacity > this._data.length) {
            int newCap = Math.max(this._data.length << 1, capacity);
            byte[] tmp = new byte[newCap];
            System.arraycopy(this._data, 0, tmp, 0, this._data.length);
            this._data = tmp;
        }
    }

    public int size() {
        return this._pos;
    }

    public boolean isEmpty() {
        return this._pos == 0;
    }

    public void trimToSize() {
        if (this._data.length > size()) {
            byte[] tmp = new byte[size()];
            toNativeArray(tmp, 0, tmp.length);
            this._data = tmp;
        }
    }

    public void add(byte val) {
        ensureCapacity(this._pos + 1);
        this._data[(this._pos++)] = val;
    }

    public void add(byte[] vals) {
        add(vals, 0, vals.length);
    }

    public void add(byte[] vals, int offset, int length) {
        ensureCapacity(this._pos + length);
        System.arraycopy(vals, offset, this._data, this._pos, length);
        this._pos += length;
    }

    public void insert(int offset, byte value) {
        if (offset == this._pos) {
            add(value);
            return;
        }
        ensureCapacity(this._pos + 1);

        System.arraycopy(this._data, offset, this._data, offset + 1, this._pos - offset);

        this._data[offset] = value;
        this._pos += 1;
    }

    public void insert(int offset, byte[] values) {
        insert(offset, values, 0, values.length);
    }

    public void insert(int offset, byte[] values, int valOffset, int len) {
        if (offset == this._pos) {
            add(values, valOffset, len);
            return;
        }

        ensureCapacity(this._pos + len);

        System.arraycopy(this._data, offset, this._data, offset + len, this._pos - offset);

        System.arraycopy(values, valOffset, this._data, offset, len);
        this._pos += len;
    }

    public byte get(int offset) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        return this._data[offset];
    }

    public byte getQuick(int offset) {
        return this._data[offset];
    }

    public void set(int offset, byte val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        this._data[offset] = val;
    }

    public byte getSet(int offset, byte val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        byte old = this._data[offset];
        this._data[offset] = val;
        return old;
    }

    public void set(int offset, byte[] values) {
        set(offset, values, 0, values.length);
    }

    public void set(int offset, byte[] values, int valOffset, int length) {
        if ((offset < 0) || (offset + length >= this._pos)) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(values, valOffset, this._data, offset, length);
    }

    public void setQuick(int offset, byte val) {
        this._data[offset] = val;
    }

    public void clear() {
        clear(DEFAULT_CAPACITY);
    }

    public void clear(int capacity) {
        this._data = new byte[capacity];
        this._pos = 0;
    }

    public void reset() {
        this._pos = 0;
        fill((byte) 0);
    }

    public void resetQuick() {
        this._pos = 0;
    }

    public byte remove(int offset) {
        byte old = get(offset);
        remove(offset, 1);
        return old;
    }

    public void remove(int offset, int length) {
        if ((offset < 0) || (offset >= this._pos)) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }

        if (offset == 0) {
            System.arraycopy(this._data, length, this._data, 0, this._pos - length);
        } else if (this._pos - length != offset) {
            System.arraycopy(this._data, offset + length, this._data, offset, this._pos - (offset + length));
        }

        this._pos -= length;
    }




    public void reverse() {
        reverse(0, this._pos);
    }
    /**
     * 反转,[from,to)
     * @param from startIndex
     * @param to endIndex+1
     */
    public void reverse(int from, int to) {
        if (from == to) {
            return;
        }
        if (from > to) {
            throw new IllegalArgumentException("from cannot be greater than to");
        }
        int i = from;
        for (int j = to-1; i < j; j--) {
            swap(i, j);

            i++;
        }
    }

    public void shuffle(Random rand) {
        for (int i = this._pos; i-- > 1; )
            swap(i, rand.nextInt(i));
    }

    private void swap(int i, int j) {
        byte tmp = this._data[i];
        this._data[i] = this._data[j];
        this._data[j] = tmp;
    }

    public TByteArrayList clone() {
        TByteArrayList clone = null;
        try {
            clone = (TByteArrayList) super.clone();
            clone._data = this._data.clone();
        } catch (CloneNotSupportedException e) {
        }
        return clone;
    }

    public byte[] toNativeArray() {
        return toNativeArray(0, this._pos);
    }

    public byte[] toNativeArray(int offset, int len) {
        byte[] rv = new byte[len];
        toNativeArray(rv, offset, len);
        return rv;
    }

    public void toNativeArray(byte[] dest, int offset, int len) {
        if (len == 0) {
            return;
        }
        if ((offset < 0) || (offset >= this._pos)) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(this._data, offset, dest, 0, len);
    }

    public boolean equals(Object other) {
        if (other == this)
            return true;
        if ((other instanceof TByteArrayList)) {
            TByteArrayList that = (TByteArrayList) other;
            if (that.size() != size()) {
                return false;
            }
            for (int i = this._pos; i-- > 0; ) {
                if (this._data[i] != that._data[i]) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public int hashCode() {
        int h = 0;
        for (int i = this._pos; i-- > 0; ) {
            h += this._data[i];
        }
        return h;
    }


    public void sort() {
        Arrays.sort(this._data, 0, this._pos);
    }
    /**
     * 排序,[from,to)
     * @param fromIndex startIndex
     * @param toIndex endIndex+1
     */
    public void sort(int fromIndex, int toIndex) {
        Arrays.sort(this._data, fromIndex, toIndex);
    }

    public void fill(byte val) {
        Arrays.fill(this._data, 0, this._pos, val);
    }
    /**
     * 填充,[from,to)
     * @param fromIndex
     *            the first index to fill.
     * @param toIndex
     *            the last + 1 index to fill.
     * @param val
     *            the {@code long} element.
     * @throws IllegalArgumentException
     *                if {@code start > end}.
     * @throws ArrayIndexOutOfBoundsException
     *                if {@code start < 0} or {@code end > array.length}.
     */
    public void fill(int fromIndex, int toIndex, byte val) {
        if (toIndex > this._pos) {
            ensureCapacity(toIndex);
            this._pos = toIndex;
        }
        Arrays.fill(this._data, fromIndex, toIndex, val);
    }

    public int binarySearch(byte value) {
        return binarySearch(value, 0, this._pos);
    }

    public int binarySearch(byte value, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        }
        if (toIndex > this._pos) {
            throw new ArrayIndexOutOfBoundsException(toIndex);
        }

        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = low + high >> 1;
            byte midVal = this._data[mid];

            if (midVal < value)
                low = mid + 1;
            else if (midVal > value)
                high = mid - 1;
            else {
                return mid;
            }
        }
        return -(low + 1);
    }

    public int indexOf(byte value) {
        return indexOf(0, value);
    }

    public int indexOf(int offset, byte value) {
        for (int i = offset; i < this._pos; i++) {
            if (this._data[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(byte value) {
        return lastIndexOf(this._pos, value);
    }

    public int lastIndexOf(int offset, byte value) {
        for (int i = offset; i-- > 0; ) {
            if (this._data[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(byte value) {
        return lastIndexOf(value) >= 0;
    }

    public byte max() {
        if (size() == 0) {
            throw new IllegalStateException("cannot find maximum of an empty list");
        }
        byte max = this._data[(this._pos - 1)];
        for (int i = this._pos - 1; i-- > 0; ) {
            max = (byte) Math.max(max, this._data[i]);
        }
        return max;
    }

    public byte min() {
        if (size() == 0) {
            throw new IllegalStateException("cannot find minimum of an empty list");
        }
        byte min = this._data[(this._pos - 1)];
        for (int i = this._pos - 1; i-- > 0; ) {
            min = (byte) Math.min(min, this._data[i]);
        }
        return min;
    }

    @Override
    public String toString() {
        return Arrays.toString(toNativeArray());
    }
}

