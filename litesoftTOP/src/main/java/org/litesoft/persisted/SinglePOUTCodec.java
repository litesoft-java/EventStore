package org.litesoft.persisted;

import org.litesoft.bean.Id32Mutator;
import org.litesoft.bean.VersionMutator;
import org.litesoft.codecs.numeric.Base32Av1;

/**
 * Manage StudentUT - Compound Encoded field that contains the following:
 * <li>PO "Id" (or null)</li>
 * <li>PO "Version" (or null)</li>
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
public class SinglePOUTCodec implements Id32Mutator<SinglePOUTCodec>,
                                        VersionMutator<SinglePOUTCodec> {
    private Integer mId;
    private Integer mVersion;

    public SinglePOUTCodec( IPersistedObjectId32 pPO ) {
        if ( pPO != null ) {
            mId = pPO.getId();
            mVersion = pPO.getVersion();
        }
    }

    public SinglePOUTCodec() {
    }

    public String encode() {
        return Base32Av1.INSTANCE.encodeMultiple(
                mId /* 0 */,
                mVersion /* 1 */ );
    }

    public static SinglePOUTCodec decode( String pEncoded ) {
        Integer[] zIntegers = Base32Av1.INSTANCE.decodeMultiple( 2, pEncoded );
        SinglePOUTCodec zCodec = new SinglePOUTCodec();
        zCodec.mId = zIntegers[0];
        zCodec.mVersion = zIntegers[1];
        return zCodec;
    }

    @Override
    public Integer getId() {
        return mId;
    }

    @Override
    public Integer getVersion() {
        return mVersion;
    }

    @Override
    public void setId( Integer pId ) {
        mId = pId;
    }

    @Override
    public void setVersion( Integer pVersion ) {
        mVersion = pVersion;
    }
}
