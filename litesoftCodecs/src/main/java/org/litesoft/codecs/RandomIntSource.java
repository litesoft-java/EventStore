package org.litesoft.codecs;

import java.util.Random;

public interface RandomIntSource {
    /**
     * Return a pseudorandom, uniformly distributed {@code int} value
     * between 0 (inclusive) and the specified value (exclusive).
     *
     * @param bound the upper bound (exclusive).  Must be positive.
     * @return the next value (non-negative)
     * @throws IllegalArgumentException if bound is not positive
     */
    int nextInt( int bound );

    static RandomIntSource deNull( RandomIntSource it ) {
        return (it != null) ? it : new Random()::nextInt;
    }
}
