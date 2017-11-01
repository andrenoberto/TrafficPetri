package com.messagehelper;

import java.io.ByteArrayOutputStream;

public class Decode {
    /** Converts a ByteArrayOutputStream to a string
     * @param toConvert A ByteArrayOutputStream to convert to string
     * @return String decoded from the ByteArrayOutputStream
     */
    public static String decodeString(ByteArrayOutputStream toConvert) {
        return toConvert.toString();
    }
}
