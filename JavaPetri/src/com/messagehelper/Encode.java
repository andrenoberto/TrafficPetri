package com.messagehelper;

import java.io.ByteArrayInputStream;

public class Encode {
    /** Converts a string to a ByteArrayInputStream
     * @param toConvert The string to convert
     * @return A ByteArrayInputStream representing the string
     */
    public static ByteArrayInputStream encodeString(String toConvert) {
        return new ByteArrayInputStream(toConvert.getBytes());
    }
}
