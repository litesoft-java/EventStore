package org.litesoft.bean;

@SuppressWarnings("unused")
public interface Id32Accessor extends IdAccessor<Integer> {
    Id32Accessor NOOP = deNull( null );

    @Override
    Integer getId();

    static Id32Accessor deNull( Id32Accessor it ) {
        return (it != null) ? it : () -> null;
    }
}
