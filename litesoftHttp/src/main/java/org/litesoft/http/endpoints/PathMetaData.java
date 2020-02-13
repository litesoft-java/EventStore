package org.litesoft.http.endpoints;

public interface PathMetaData {
  String name();

  EndpointMetaData getEndpointMetaData();

  default String template() {
    return name();
  }

  default String path() {
    return getEndpointMetaData().getPath();
  }

  default String specialPermissionsPath() {
    return getEndpointMetaData().getSpecialPermissionsPath();
  }

  default EndpointType endpointType() {
    return getEndpointMetaData().getEndpointType();
  }
}
