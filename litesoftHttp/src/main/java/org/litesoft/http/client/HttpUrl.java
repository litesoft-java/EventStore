package org.litesoft.http.client;

import java.util.Objects;

@SuppressWarnings("unused")
public class HttpUrl implements ToHttpURL {
  private final boolean mSecure;
  private final String mDomain;
  private final String mSubDomain;
  private final String mPath;
  private String mKey;

  private HttpUrl( boolean pSecure, String pDomain, String pSubDomain, String pPath ) {
    mSecure = pSecure;
    mDomain = pDomain;
    mSubDomain = pSubDomain;
    mPath = pPath;
  }

  public static HttpUrl withDomain( String pDomain ) {
    pDomain = normalize( pDomain );
    if ( pDomain.isEmpty() ) {
      throw new IllegalArgumentException( "Domain may not be Empty" );
    }
    return new HttpUrl( true, pDomain, "", "" );
  }

  public HttpUrl withNoSSL() {
    return !mSecure ? this : new HttpUrl( false, mDomain, mSubDomain, mPath );
  }

  public HttpUrl withSSL() {
    return mSecure ? this : new HttpUrl( true, mDomain, mSubDomain, mPath );
  }

  public HttpUrl withSubDomain( String pSubDomain ) {
    pSubDomain = normalize( pSubDomain );
    if ( !pSubDomain.isEmpty() && !pSubDomain.endsWith( "." ) ) {
      pSubDomain += ".";
    }
    return mSubDomain.equals( pSubDomain ) ? this : new HttpUrl( mSecure, mDomain, pSubDomain, mPath );
  }

  public HttpUrl withPath( String pPath ) {
    pPath = normalize( pPath );
    return mPath.equals( pPath ) ? this : new HttpUrl( mSecure, mDomain, mSubDomain, pPath );
  }

  public String getKey() {
    if ( mKey == null ) { // Unsafe (but good enough) lazy initialization
      mKey = mSubDomain + mDomain + "/" + mPath;
    }
    return mKey;
  }

  public String getDomain() {
    return mDomain;
  }

  public String getSubDomain() {
    return mSubDomain;
  }

  public String getPath() {
    return mPath;
  }

  @Override
  public String toURL() {
    return (mSecure ? "https://" : "http://") + getKey();
  }

  @Override
  public String toString() {
    return mSubDomain + mPath;
  }

  @Override
  public int hashCode() {
    return Objects.hash( mDomain, mSubDomain, mPath );
  }

  @Override
  public boolean equals( Object them ) {
    return (this == them) ||
           ((them instanceof HttpUrl) && equals( (HttpUrl)them )); // Left to Right!
  }

  public boolean equals( HttpUrl them ) {
    return (this == them) ||
           ((them != null) // Left to Right!
            && (this.mSecure == them.mSecure)
            && Objects.equals( this.mDomain, them.mDomain )
            && Objects.equals( this.mSubDomain, them.mSubDomain )
            && Objects.equals( this.mPath, them.mPath )
           );
  }

  private static String normalize( String pValue ) {
    return (pValue == null) ? "" : pValue.trim();
  }
}
