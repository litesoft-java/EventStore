package org.litesoft.bean;

import org.litesoft.alleviative.Cast;

@SuppressWarnings("unused")
public interface InstallationMutator<T extends InstallationMutator<T>> extends InstallationAccessor {
  void setInstallation( String pInstallation );

  default T withInstallation( String pInstallation ) {
    setInstallation( pInstallation );
    return Cast.it( this );
  }

  default T withInstallation( InstallationAccessor pAccessor ) {
    return withInstallation( InstallationAccessor.deNull( pAccessor ).getInstallation() );
  }
}
