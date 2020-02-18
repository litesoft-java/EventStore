package org.litesoft.bean;

@SuppressWarnings("unused")
public interface IdUuidAccessor extends IdAccessor<String> {
    IdUuidAccessor NOOP = deNull( null );

    /**
     * get ID (UUID)
     *
     * @return null or String (UUID)
     */
    @Override
    String getId();

    static IdUuidAccessor deNull( IdUuidAccessor it ) {
        return (it != null) ? it : () -> null;
    }
}
