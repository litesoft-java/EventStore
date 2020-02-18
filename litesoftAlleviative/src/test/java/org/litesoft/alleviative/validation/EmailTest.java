package org.litesoft.alleviative.validation;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmailTest {

    @Test
    public void validateOptional() {
        assertOK( Email.validateOptional( null ), null );
        assertOK( Email.validateOptional( "  " ), null );
        assertOK( Email.validateOptional( " fred @ flintstones.com " ), "fred@flintstones.com" );
    }

    @Test
    public void validateRequired() {
        assertOK( Email.validateRequired( " fred @ flintstones.com " ), "fred@flintstones.com" );
        assertOK( Email.validateRequired( "fred@flint-stones.com" ), "fred@flint-stones.com" );
        assertOK( Email.validateRequired( "fred@flintstones.c-1-om" ), "fred@flintstones.c-1-om" );
        assertError( Email.validateRequired( null ), "is Required" );
        assertError( Email.validateRequired( "  " ), "is Required" );
        assertError( Email.validateRequired( "@flintstones" ), "nothing before the '@' in: @flintstones" );
        assertError( Email.validateRequired( "fred@" ), "nothing after the '@' in: fred@" );
        assertError( Email.validateRequired( "fred@flintstones" ), "Domain too few Labels (min: 2) in: fred@flintstones" );
        assertError( Email.validateRequired( "fred@flintstones.c_om" ), "Domain Last Label character (at offset: 1) is unacceptable in: fred@flintstones.c_om" );
        assertError( Email.validateRequired( "fred@flint_stones.com" ), "Domain Label-0 character (at offset: 5) is unacceptable in: fred@flint_stones.com" );
        assertError( Email.validateRequired( "fred@flintstones.-com-" ), "Domain Last Label last character must only be Alpha-Numeric in: fred@flintstones.-com-" );
        assertError( Email.validateRequired( "fred@flintstones.-com" ), "Domain Last Label first character must only be Alpha-Numeric in: fred@flintstones.-com" );
        assertError( Email.validateRequired( "fred@flintstones-.com" ), "Domain Label-0 last character must only be Alpha-Numeric in: fred@flintstones-.com" );
        assertError( Email.validateRequired( "fred@-flintstones.com" ), "Domain Label-0 first character must only be Alpha-Numeric in: fred@-flintstones.com" );
        assertError( Email.validateRequired( "fr ed@flintstones.com" ), "Local Part contains space(s) in: fr ed@flintstones.com" );
        assertError( Email.validateRequired( "fred@flint stones.com" ), "Domain contains space(s) in: fred@flint stones.com" );
        assertError( Email.validateRequired( "fred..f@flintstones.com" ), "Local Part contains two dots (..) in: fred..f@flintstones.com" );
        assertError( Email.validateRequired( "fred@flint..stones.com" ), "Domain contains two dots (..) in: fred@flint..stones.com" );
        assertError( Email.validateRequired( ".fred@flintstones.com" ), "Local Part starts with a dot (.) in: .fred@flintstones.com" );
        assertError( Email.validateRequired( "fred@.flintstones.com" ), "Domain starts with a dot (.) in: fred@.flintstones.com" );
        assertError( Email.validateRequired( "fred.@flintstones.com" ), "Local Part ends with a dot (.) in: fred.@flintstones.com" );
        assertError( Email.validateRequired( "fred@flintstones.com." ), "Domain ends with a dot (.) in: fred@flintstones.com." );
        assertError( Email.validateRequired( "fr--ed@flintstones.com" ), "Local Part adjacent 'special' characters (at offset: 3) is unacceptable in: fr--ed@flintstones.com" );
        assertError( Email.validateRequired( "fred@flint--stones.com" ), "Domain Label-0 adjacent 'special' characters (at offset: 6) is unacceptable in: fred@flint--stones.com" );
        assertError( Email.validateRequired( "fred@flintstones.123" ), "Domain Last Label may NOT be all Digits in: fred@flintstones.123" );

        // LocalPart containing ALL the special chars:
        String zEmail = "init" + Email.SPECIAL_CHARS_LOCAL_PART + "fini@q.nom";
        assertOK( Email.validateRequired( zEmail ), zEmail );

        // Test Max Lengths:

        // Local Part:
        String zLocalPart = buildMaxLength( "local Part", '.', Email.MAX_LENGTH_LOCAL_PART );
        zEmail = zLocalPart + "@q.nom";
        assertOK( Email.validateRequired( zEmail ), zEmail );
        zEmail = "a" + zEmail;
        assertError( Email.validateRequired( zEmail ), "Local Part too long (max length: " +
                                                       Email.MAX_LENGTH_LOCAL_PART + ") in: " + zEmail );

        // Max Domain:
        String zDomain = buildMaxLength( "Domain", '.', Email.MAX_LENGTH_DOMAIN );
        zEmail = "fred@" + zDomain;
        assertOK( Email.validateRequired( zEmail ), zEmail );
        zEmail += "a";
        assertError( Email.validateRequired( zEmail ), "Domain too long (max length: " +
                                                       Email.MAX_LENGTH_DOMAIN + ") in: " + zEmail );

        // Max Domain Label:
        String zDomainLabel = buildMaxLength( "Domain Label", '-', Email.MAX_LENGTH_DOMAIN_LABEL );
        zEmail = "fred@" + zDomainLabel + ".com";
        assertOK( Email.validateRequired( zEmail ), zEmail );
        zEmail = "fred@a" + zDomainLabel + ".com";
        assertError( Email.validateRequired( zEmail ), "Domain Label-0 too long (max length: " +
                                                       Email.MAX_LENGTH_DOMAIN_LABEL + ") in: " + zEmail );
    }

    private String buildMaxLength( String pWhat, char pSep, int pMaxLength ) {
        int zAddSepLength = pMaxLength - 2;
        int zLettersMultiplier = pMaxLength / 26;
        StringBuilder zCollector = new StringBuilder();
        for ( char c = 'a'; (c <= 'z') && (zCollector.length() < pMaxLength); c++ ) {
            if ( (zCollector.length() != 0) && (zCollector.length() <= zAddSepLength) ) {
                zCollector.append( pSep );
            }
            for ( int i = 0; (zCollector.length() < pMaxLength) && (i < zLettersMultiplier); i++ ) {
                zCollector.append( c );
            }
        }
        assertEquals( pWhat + " Max Length", pMaxLength, zCollector.length() );
        return zCollector.toString();
    }

    private void assertError( ValidationResult<String> pResult, String pExpectedError ) {
        if ( !pResult.hasError() ) {
            fail( "No Error, Email (" + pResult.getValue() + "), Expected error was: " + pExpectedError );
        }
        assertEquals( "Error but...", pExpectedError, pResult.getError() );
    }

    private void assertOK( ValidationResult<String> pResult, String pExpectedEmail ) {
        if ( pResult.hasError() ) {
            fail( "Expected (" + pExpectedEmail + "), but got error: " + pResult.getError() );
        }
        assertEquals( "OK but...", pExpectedEmail, pResult.getValue() );
    }
}