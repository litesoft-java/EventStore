package org.litesoft.bean;

@SuppressWarnings("unused")
public interface VersionAccessor extends Accessor {
    VersionAccessor NOOP = deNull( null );

    Integer getVersion();

    static VersionAccessor deNull( VersionAccessor it ) {
        return (it != null) ? it : () -> null;
    }
}
