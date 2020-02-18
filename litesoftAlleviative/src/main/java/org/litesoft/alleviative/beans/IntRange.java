package org.litesoft.alleviative.beans;

@SuppressWarnings("unused")
public class IntRange {
    private final int mFrom, mThru;

    public IntRange( int pFrom, int pThru ) {
        mFrom = pFrom;
        mThru = pThru;
    }

    public int getFrom() {
        return mFrom;
    }

    public int getThru() {
        return mThru;
    }

    public boolean contains( int pToCheck ) {
        return (getFrom() <= pToCheck) && (pToCheck <= getThru());
    }
}
