package org.litesoft.alleviative;

import org.junit.Assert;
import org.junit.Test;

public class ToStringTest {

  @Test
  public void test_build() { // !pretty
    addFieldsAndCheck( ToString.of( this ),
                       "{ String1='One', StringNull=null, Int2=2, IntNull=null }" );
  }

  @Test
  public void test_pretty() { // build
    addFieldsAndCheck( ToString.of( this ).pretty(),
                       "{\n  String1='One',\n  StringNull=null,\n  Int2=2,\n  IntNull=null\n}" );
  }

  private void addFieldsAndCheck( ToString pBuilder, String pExpected ) {
    String zActual = pBuilder
            .with( this::getString1, "String1" )
            .with( this::getStringNull, "StringNull" )
            .with( this::getInt2, "Int2" )
            .with( this::getIntNull, "IntNull" )
            .build();
    String zExpected = this.getClass().getSimpleName() + " " + pExpected;
    Assert.assertEquals( zExpected, zActual );
  }

  private String getString1() {
    return "One";
  }

  private String getStringNull() {
    return null;
  }

  private int getInt2() {
    return 2;
  }

  private Integer getIntNull() {
    return null;
  }
}