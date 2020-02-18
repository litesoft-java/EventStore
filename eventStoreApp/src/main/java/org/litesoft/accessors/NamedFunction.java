package org.litesoft.accessors;

import java.util.function.Function;

public class NamedFunction<T> {
    private final String mName;
    private final Function<T, Object> mFunction;

    public NamedFunction( String pName, Function<T, Object> pFunction ) {
        if ( pName == null ) {
            pName = "";
        }
        mName = pName.trim();
        mFunction = pFunction;
        if ( mName.isEmpty() ) {
            throw new IllegalArgumentException( "Name required" );
        }
    }

    public String getName() {
        return mName;
    }

    public Function<T, Object> getFunction() {
        return mFunction;
    }

    @Override
    public String toString() {
        return "NamedFunction('" + mName + "', " +
               "..." +
               ")";
    }

    @Override
    public int hashCode() {
        return mName.hashCode();
    }

    @Override
    public boolean equals( Object them ) {
        return (this == them) || ((them instanceof NamedFunction<?>)
                                  && equals( (NamedFunction<?>)them )
        );
    }

    public boolean equals( NamedFunction<?> them ) {
        return (this == them) || ((them != null) // Left to Right!
                                  && this.mName.equals( them.mName )
                                  && (this.mFunction == them.mFunction)
        );
    }
}
