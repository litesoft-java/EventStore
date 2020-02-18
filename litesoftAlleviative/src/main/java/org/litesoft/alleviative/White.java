package org.litesoft.alleviative;

public class White extends AbstractCharacters {
    public static final White Space = new White();

    private White() {
        super( ' ', Character::isWhitespace );
    }
}
