package org.litesoft.events.api.v03.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.litesoft.accessors.NamedFunction;
import org.litesoft.alleviative.beans.SetValue;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class PatchEventTest {

    @Test
    public void jsonRT()
            throws Exception {
        ObjectMapper zMapper = new ObjectMapper();

        PatchEventMD zMD = PatchEventMD.INSTANCE;

        PatchEvent zDTO = new PatchEvent();

        String zJson = zMapper.writeValueAsString( zDTO );

        Map<String, ?> zMap = zMapper.readValue( zJson, HashMap.class );

        assertMapHasNullsForOnly( zMap,
                                  zMD.nfId(),
                                  zMD.nfUpdateToken(),
                                  zMD.nfUser(),
                                  zMD.nfWhat(),
                                  zMD.nfWhen(),
                                  zMD.nfLocalTimeOffset(),
                                  zMD.nfLocalTzName(),
                                  zMD.nfWhere(),
                                  zMD.nfBillable(),
                                  zMD.nfClient(),
                                  zMD.nfDone() );

        PatchEvent zDTO_RT = zMapper.readValue( zJson, PatchEvent.class );

        assertWasSet( zDTO_RT, zMD.nfId(), zDTO_RT.getId(), zDTO_RT.onId(), null );
        assertWasSet( zDTO_RT, zMD.nfUpdateToken(), zDTO_RT.getUpdateToken(), zDTO_RT.onUpdateToken(), null );
        assertWasSet( zDTO_RT, zMD.nfUser(), zDTO_RT.getUser(), zDTO_RT.onUser(), null );
        assertWasSet( zDTO_RT, zMD.nfWhat(), zDTO_RT.getWhat(), zDTO_RT.onWhat(), null );
        assertWasSet( zDTO_RT, zMD.nfWhen(), zDTO_RT.getWhen(), zDTO_RT.onWhen(), null );
        assertWasSet( zDTO_RT, zMD.nfLocalTimeOffset(), zDTO_RT.getLocalTimeOffset(), zDTO_RT.onLocalTimeOffset(), null );
        assertWasSet( zDTO_RT, zMD.nfLocalTzName(), zDTO_RT.getLocalTzName(), zDTO_RT.onLocalTzName(), null );
        assertWasSet( zDTO_RT, zMD.nfWhere(), zDTO_RT.getWhere(), zDTO_RT.onWhere(), null );
        assertWasSet( zDTO_RT, zMD.nfBillable(), zDTO_RT.getBillable(), zDTO_RT.onBillable(), null );
        assertWasSet( zDTO_RT, zMD.nfClient(), zDTO_RT.getClient(), zDTO_RT.onClient(), null );
        assertWasSet( zDTO_RT, zMD.nfDone(), zDTO_RT.getDone(), zDTO_RT.onDone(), null );

        String zJson_RT = zMapper.writeValueAsString( zDTO_RT );

        assertEquals( zJson, zJson_RT );

        assertEquals( zDTO, zDTO_RT );
        assertEquals( zDTO.hashCode(), zDTO_RT.hashCode() );

        zDTO_RT = zMapper.readValue( "{\"user\":\"fred@flintstones.com\"}", PatchEvent.class );

        assertNotSet( zDTO_RT, zMD.nfId(), zDTO_RT.getId(), zDTO_RT.onId() );
        assertNotSet( zDTO_RT, zMD.nfUpdateToken(), zDTO_RT.getUpdateToken(), zDTO_RT.onUpdateToken() );
        assertWasSet( zDTO_RT, zMD.nfUser(), zDTO_RT.getUser(), zDTO_RT.onUser(), "fred@flintstones.com" );
        assertNotSet( zDTO_RT, zMD.nfWhat(), zDTO_RT.getWhat(), zDTO_RT.onWhat() );
        assertNotSet( zDTO_RT, zMD.nfWhen(), zDTO_RT.getWhen(), zDTO_RT.onWhen() );
        assertNotSet( zDTO_RT, zMD.nfLocalTimeOffset(), zDTO_RT.getLocalTimeOffset(), zDTO_RT.onLocalTimeOffset() );
        assertNotSet( zDTO_RT, zMD.nfLocalTzName(), zDTO_RT.getLocalTzName(), zDTO_RT.onLocalTzName() );
        assertNotSet( zDTO_RT, zMD.nfWhere(), zDTO_RT.getWhere(), zDTO_RT.onWhere() );
        assertNotSet( zDTO_RT, zMD.nfBillable(), zDTO_RT.getBillable(), zDTO_RT.onBillable() );
        assertNotSet( zDTO_RT, zMD.nfClient(), zDTO_RT.getClient(), zDTO_RT.onClient() );
        assertNotSet( zDTO_RT, zMD.nfDone(), zDTO_RT.getDone(), zDTO_RT.onDone() );
    }

    @SafeVarargs
    private final void assertMapHasNullsForOnly( Map<String, ?> pMap, NamedFunction<PatchEvent>... pNFs ) {
        Set<String> zKeys = pMap.keySet();
        assertEquals( pNFs.length, zKeys.size() );
        for ( NamedFunction<PatchEvent> zNF : pNFs ) {
            String zName = zNF.getName();
            assertTrue( zName, zKeys.contains( zName ) );
            assertNull( zName, pMap.get( zName ) );
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void assertNotSet( PatchEvent pDTO, NamedFunction<PatchEvent> pNF, Object pGet, SetValue<?> pOn ) {
        assertNull( pNF.getName(), pGet );
        assertNull( pNF.getName(), pOn );
        assertNull( pNF.getName(), pDTO.fieldGet( pNF ) );
        assertNull( pNF.getName(), pDTO.fieldGetSetValue( pNF ) );
    }

    private void assertWasSet( PatchEvent pDTO, NamedFunction<PatchEvent> pNF, Object pGet, SetValue<?> pOn, Object pValue ) {
        assertEquals( pNF.getName(), pValue, pGet );
        assertNotNull( pNF.getName(), pOn );
        assertEquals( pNF.getName(), pValue, pOn.get() );
        assertEquals( pNF.getName(), pValue, pDTO.fieldGet( pNF ) );
        SetValue<Object> zOn = pDTO.fieldGetSetValue( pNF );
        assertNotNull( pNF.getName(), zOn );
        assertEquals( pNF.getName(), pValue, zOn.get() );
    }
}