package com.poolingpeople.deployer.infrastructure.boundary;

import org.junit.Test;

import static org.junit.Assert.*;

public class DnsRegisterTest {

    DnsRegister cut = new DnsRegister();

    @Test
    public void testGetInstances() throws Exception {
        cut.getInstances();
    }

    @Test
    public void testGetUpdateDndRecords(){
        cut.getUpdateDndRecords();
    }

}