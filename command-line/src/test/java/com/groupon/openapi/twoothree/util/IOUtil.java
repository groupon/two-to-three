package com.groupon.openapi.twoothree.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class IOUtil {
    private static final int READ_BUFFER_SIZE = 2048;

    private IOUtil() {
    }

    public static byte[] toByteArray(InputStream is) throws IOException {
        if (is == null) {
            return new byte[0];
        } else {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];

            int read;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                os.write(buffer, 0, read);
            }

            os.flush();
            return os.toByteArray();
        }
    }
}
