package org.litesoft.restish.support;

public class PageLimiter {
    private final int mMin, mDefault, mMax;

    private PageLimiter(int mMin, int mDefault, int mMax) {
        this.mMin = mMin;
        this.mDefault = mDefault;
        this.mMax = mMax;
    }

    public int normalize(Integer pLimit) {
        if (pLimit == null) {
            return mDefault;
        }
        if (pLimit < mMin) {
            return mMin;
        }
        if (pLimit > mMax) {
            return mMax;
        }
        return pLimit;
    }

    public static class Builder {
        private int mMin = 0;
        private int mDefault = 100;
        private int mMax = 1000;

        public Builder withMin(int pMin) {
            if (pMin < 0) {
                throw new IllegalArgumentException("Min can NOT be negative");
            }
            mMin = pMin;
            return this;
        }

        public Builder withDefault(int pDefault) {
            if (pDefault < 0) {
                throw new IllegalArgumentException("Default must be at least 1");
            }
            mDefault = pDefault;
            return this;
        }

        public Builder withMax(int pMax) {
            if (pMax < 0) {
                throw new IllegalArgumentException("Max must be at least 1");
            }
            mMax = pMax;
            return this;
        }

        public PageLimiter build() {
            int zMin = mMin;
            int zDefault = mDefault;
            int zMax = mMax;
            if (zMax < zMin) {
                throw new IllegalArgumentException("Max (" + zMax + ") < (" + zMin + ") Min");
            }
            if (zMax < zDefault) {
                throw new IllegalArgumentException("Max (" + zMax + ") < (" + zDefault + ") Default");
            }
            if (zDefault < zMin) {
                throw new IllegalArgumentException("Default (" + zDefault + ") < (" + zMin + ") Min");
            }
            return new PageLimiter(zMin, zDefault, zMax);
        }
    }

    public static Builder withMin(int pMin) {
        return new Builder().withMin(pMin);
    }

    public static Builder withDefault(int pDefault) {
        return new Builder().withDefault(pDefault);
    }

    public static Builder withMax(int pMax) {
        return new Builder().withMax(pMax);
    }
}
