package org.litesoft.events;

import org.litesoft.restish.support.AbstractApiAuthFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ApiAuthFilter extends AbstractApiAuthFilter {
}
