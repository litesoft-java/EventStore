package org.litesoft.validation;

import org.junit.Test;
import org.litesoft.validation.Validation;

import static org.junit.Assert.*;

public class ValidationTest {

    @Test
    public void noErrors() {
        Validation zValidation = Validation.NO_ERRORS;
        zValidation = zValidation.addError( "a1", " " );
        zValidation = zValidation.addError( "a1", Validation.NO_ERRORS );
        zValidation = zValidation.addError( "a1", (String)null );
        zValidation = zValidation.addError( "a1", (Validation)null );

        assertSame( Validation.NO_ERRORS, zValidation );

        zValidation = zValidation.addErrorsOnClass( null );

        assertSame( Validation.NO_ERRORS, zValidation );

        zValidation = zValidation.addErrorsOnClass( this );

        assertSame( Validation.NO_ERRORS, zValidation );

        assertEquals( zValidation.toString(), Validation.NO_VALIDATION_ERRORS );
    }

    @Test
    public void addTextErrors() {
        Validation zValidation = Validation.NO_ERRORS;
        zValidation = zValidation.addError( "a1", "Bad Robot " );
        zValidation = zValidation.addError( "a2", " Bad Robot" );

        assertNotSame( Validation.NO_ERRORS, zValidation );

        assertTrue( "hasErrors", zValidation.hasErrors() );
        assertTrue( "hasErrors 4 a1", zValidation.hasErrorForAttribute( "a1" ) );
        assertTrue( "hasErrors 4 a2", zValidation.hasErrorForAttribute( "a2" ) );

        assertEquals( zValidation.toString(), ""
                                              + "a2 - Bad Robot\n"
                                              + "a1 - Bad Robot"
                                              + "" );

        assertSame( zValidation, zValidation.addErrorsOnClass( null ) );

        zValidation = zValidation.addErrorsOnClass( this );
        assertEquals( zValidation.toString(), this.getClass().getSimpleName() + " errors:\n"
                                              + "    a2 - Bad Robot\n"
                                              + "    a1 - Bad Robot"
                                              + "" );
    }

    @Test
    public void addValidationToTextErrors() {

        Validation zSubValidation = Validation.NO_ERRORS
                .addError( "s1", "BS1" )
                .addError( "s2", "BS2" );

        Validation zValidation = Validation.NO_ERRORS;
        zValidation = zValidation.addError( "a1", "Bad Robot " );
        zValidation = zValidation.addError( "v1", zSubValidation );
        zValidation = zValidation.addError( "a2", " Bad Robot" );

        assertNotSame( Validation.NO_ERRORS, zValidation );

        assertTrue( "hasErrors", zValidation.hasErrors() );
        assertTrue( "hasErrors 4 a1", zValidation.hasErrorForAttribute( "a1" ) );
        assertTrue( "hasErrors 4 v1", zValidation.hasErrorForAttribute( "v1" ) );
        assertTrue( "hasErrors 4 a2", zValidation.hasErrorForAttribute( "a2" ) );

        assertEquals( zValidation.toString(), ""
                                              + "a2 - Bad Robot\n"
                                              + "v1 - \n"
                                              + "     s2 - BS2\n"
                                              + "     s1 - BS1\n"
                                              + "a1 - Bad Robot"
                                              + "" );

        zValidation = zValidation.addErrorsOnClass( this );
        assertEquals( zValidation.toString(), this.getClass().getSimpleName() + " errors:\n"
                                              + "    a2 - Bad Robot\n"
                                              + "    v1 - \n"
                                              + "         s2 - BS2\n"
                                              + "         s1 - BS1\n"
                                              + "    a1 - Bad Robot"
                                              + "" );
    }

    @Test
    public void addValidationWithClassToTextErrors() {

        Validation zSubValidation = Validation.NO_ERRORS
                .addError( "s1", "BS1" )
                .addError( "s2", "BS2" )
                .addErrorsOnClass( "" );

        Validation zValidation = Validation.NO_ERRORS;
        zValidation = zValidation.addError( "a1", "Bad Robot " );
        zValidation = zValidation.addError( "v1", zSubValidation );
        zValidation = zValidation.addError( "a2", " Bad Robot" );

        assertNotSame( Validation.NO_ERRORS, zValidation );

        assertTrue( "hasErrors", zValidation.hasErrors() );
        assertTrue( "hasErrors 4 a1", zValidation.hasErrorForAttribute( "a1" ) );
        assertTrue( "hasErrors 4 v1", zValidation.hasErrorForAttribute( "v1" ) );
        assertTrue( "hasErrors 4 a2", zValidation.hasErrorForAttribute( "a2" ) );

        assertEquals( zValidation.toString(), ""
                                              + "a2 - Bad Robot\n"
                                              + "v1 - String errors:\n"
                                              + "         s2 - BS2\n"
                                              + "         s1 - BS1\n"
                                              + "a1 - Bad Robot"
                                              + "" );

        zValidation = zValidation.addErrorsOnClass( this );
        assertEquals( zValidation.toString(), this.getClass().getSimpleName() + " errors:\n"
                                              + "    a2 - Bad Robot\n"
                                              + "    v1 - String errors:\n"
                                              + "             s2 - BS2\n"
                                              + "             s1 - BS1\n"
                                              + "    a1 - Bad Robot"
                                              + "" );
    }
}