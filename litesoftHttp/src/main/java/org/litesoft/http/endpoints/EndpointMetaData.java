package org.litesoft.http.endpoints;

import java.util.Objects;

import org.litesoft.alleviative.validation.Significant;

@SuppressWarnings("WeakerAccess")
public class EndpointMetaData {
  public static final Builder GET = new Builder( EndpointType.GET );
  public static final Builder POST = new Builder( EndpointType.POST );

  private final EndpointType mEndpointType;
  private final String mPath;
  private final String mSpecialPermissionsPath;

  private EndpointMetaData( EndpointType pEndpointType, String pPath, String pSpecialPermissionsPath ) {
    mEndpointType = (pEndpointType != null) ? pEndpointType : EndpointType.GET;
    mPath = pPath;
    mSpecialPermissionsPath = pSpecialPermissionsPath;
  }

  public EndpointType getEndpointType() {
    return mEndpointType;
  }

  public String getPath() {
    return mPath;
  }

  public String getSpecialPermissionsPath() {
    return mSpecialPermissionsPath;
  }

  /**
   * Immutable Builder!
   */
  public static class Builder {
    private final EndpointType mEndpointType;
    private final String mPath;
    private final String mTrailingVariables;

    private Builder( EndpointType pEndpointType, String pPath, String pTrailingVariables ) {
      mEndpointType = pEndpointType;
      mPath = pPath;
      mTrailingVariables = pTrailingVariables;
    }

    private Builder( EndpointType pEndpointType ) {
      this( pEndpointType, "", null );
    }

    public Builder withPath( String pPath ) {
      pPath = Significant.orEmpty( pPath );
      return Objects.equals( pPath, mPath ) ? this :
             new Builder( mEndpointType, pPath, mTrailingVariables );
    }

    public Builder withTrailingVariables( String pTrailingVariables ) {
      pTrailingVariables = Significant.orNull( pTrailingVariables );
      return Objects.equals( pTrailingVariables, mTrailingVariables ) ? this :
             new Builder( mEndpointType, mPath, pTrailingVariables );
    }

    public EndpointMetaData createFor( String pName ) {
      pName = Significant.orNull( pName );
      Objects.requireNonNull( pName, "Endpoint Name" );
      String zBasePath = mPath.startsWith( "/" ) ? mPath : ("/" + pName);
      String zPath, zSpecialPermissionsPath;
      if ( mTrailingVariables == null ) {
        zPath = zSpecialPermissionsPath = zBasePath;
      } else {
        zBasePath += "/";
        zPath = zBasePath + mTrailingVariables;
        zSpecialPermissionsPath = zBasePath + "**";
      }
      return new EndpointMetaData( mEndpointType, zPath, zSpecialPermissionsPath );
    }
  }
}
