package com.mrocker.push.demo;

import android.util.Log;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class IoUtil {

    private final static String TAG = IoUtil.class.getName();

    public static int copy(InputStream input, OutputStream output)
            throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static void copy(InputStream input, Writer output)
            throws IOException {
        InputStreamReader in = new InputStreamReader(input);
        copy(in, output);
    }

    public static String toString(InputStream input) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        copy(input, sw);
        return sw.toString();
    }

    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(Reader input, Writer output)
            throws IOException {
        char[] buffer = new char[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * @param content
     * @return
     * @throws java.io.IOException
     */
    public static byte[] deflate(byte[] content) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DeflaterOutputStream compresser = new DeflaterOutputStream(output);
        try {
            compresser.write(content);
            compresser.flush();
        } catch (IOException e) {
            Log.w(TAG, e.getMessage());
        } finally {
            try {
                compresser.close();
            } catch (IOException e) {
            }
        }
        return output.toByteArray();
        // return content;
    }

    /**
     * @param content
     * @return
     * @throws java.io.IOException
     */
    public static byte[] inflate(byte[] content) {
        ByteArrayInputStream bytein = new ByteArrayInputStream(content);
        InflaterInputStream decompresser = new InflaterInputStream(bytein);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            IoUtil.copy(decompresser, output);
            decompresser.close();
        } catch (IOException e) {
            Log.w(TAG, e.getMessage());
        }
        return output.toByteArray();
        // return content;
    }

    public static void close(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
            }
        }
    }

    public static void close(OutputStream output) {
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
            }
        }
    }

    static class StringBuilderWriter extends Writer implements Serializable {
        private final StringBuilder builder;

        public StringBuilderWriter() {
            this.builder = new StringBuilder();
        }

        public StringBuilderWriter(int capacity) {
            this.builder = new StringBuilder(capacity);
        }

        public StringBuilderWriter(StringBuilder builder) {
            this.builder = (builder != null ? builder : new StringBuilder());
        }

        public Writer append(char value) {
            this.builder.append(value);
            return this;
        }

        public Writer append(CharSequence value) {
            this.builder.append(value);
            return this;
        }

        public Writer append(CharSequence value, int start, int end) {
            this.builder.append(value, start, end);
            return this;
        }

        public void close() {
        }

        public void flush() {
        }

        public void write(String value) {
            if (value != null)
                this.builder.append(value);
        }

        public void write(char[] value, int offset, int length) {
            if (value != null)
                this.builder.append(value, offset, length);
        }

        public StringBuilder getBuilder() {
            return this.builder;
        }

        public String toString() {
            return this.builder.toString();
        }
    }

}
