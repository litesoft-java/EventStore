package org.litesoft.persisted;

import java.util.function.Function;

import org.litesoft.alleviative.functionalinterfaces.FluentSetter;

@SuppressWarnings({"unused", "WeakerAccess"})
public class POField<T extends IPersistedObject, B extends POBuilder, FT> {
  private final String mName;
  private final Class<FT> mType;
  private final boolean mSecure;
  private final Function<T, FT> mGetter;
  private final FluentSetter<B, FT> mSetter;

  private POField( String pName, Class<FT> pType, boolean pSecure, Function<T, FT> pGetter, FluentSetter<B, FT> pSetter ) {
    mName = pName;
    mType = pType;
    mSecure = pSecure;
    mGetter = pGetter;
    mSetter = pSetter;
  }

  public static <T extends IPersistedObject,
          B extends POBuilder,
          FT> POField<T, B, ?> with( String pName, Class<FT> pType,
                                     Function<T, FT> pGetter,
                                     FluentSetter<B, FT> pSetter ) {
    return new POField<>( pName, pType, false, pGetter, pSetter );
  }

  public static <T extends IPersistedObject,
          B extends POBuilder> POField<T, B, ?> withSecure( String pName,
                                                            Function<T, String> pGetter,
                                                            FluentSetter<B, String> pSetter ) {
    return new POField<>( pName, String.class, true, pGetter, pSetter );
  }

  public String getName() {
    return mName;
  }

  public Class<FT> getType() {
    return mType;
  }

  public boolean isSecure() {
    return mSecure;
  }

  public Function<T, FT> getGetter() {
    return mGetter;
  }

  public FluentSetter<B, FT> getSetter() {
    return mSetter;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder( "POField(" )
            .append( " '" ).append( mName ).append( "'" )
            .append( ", '" ).append( mType.getSimpleName() ).append( "'" )
            .append( ", " ).append( mSecure );
    add( sb, "Getter", mGetter );
    add( sb, "Setter", mSetter );
    return sb.append( " )" ).toString();
  }

  private void add( StringBuilder pSb, String pMethodName, Object pMethod ) {
    pSb.append( ", " ).append( (pMethod == null) ? "NO " : "w/ " ).append( pMethodName );
  }
}
