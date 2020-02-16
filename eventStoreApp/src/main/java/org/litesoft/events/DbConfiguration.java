package org.litesoft.events;

import org.litesoft.springjpa.adaptors.TransactionalProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfiguration {
  @Bean
  TransactionalProxy createTransactionalProxy() {
    return new TransactionalProxy();
  }
}