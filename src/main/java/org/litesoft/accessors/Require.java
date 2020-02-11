package org.litesoft.accessors;

public class Require {

    public static String normalize(CharSequence charSeq) {
        if (charSeq != null) {
            String str = charSeq.toString().trim();
            if (!str.isEmpty()) {
                return str;
            }
        }
        return null;
    }

    public static String significant(String what, CharSequence charSeq) {
        return notNull(normalize(charSeq), what, " and must be significant");
    }

    public static <T> T notNull(String what, T o) {
        return notNull(o, what, ", but was null");
    }

    private static <T> T notNull(T o, String what, String message) {
        if (o == null) {
            throw new IllegalArgumentException(what + " is Required" + message);
        }
        return o;
    }
}
