package org.litesoft.bean;

@SuppressWarnings("unused")
public interface InstallationAccessor extends Accessor {
    String getInstallation();

    static InstallationAccessor deNull( InstallationAccessor it ) {
        return (it != null) ? it : () -> null;
    }
}
