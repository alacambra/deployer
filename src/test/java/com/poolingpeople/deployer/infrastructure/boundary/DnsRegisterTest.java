package com.poolingpeople.deployer.infrastructure.boundary;

import org.junit.Test;

public class DnsRegisterTest {

    DnsRegister cut = new DnsRegister();

    @Test
    public void testGetInstances() throws Exception {
        cut.getInstances();
    }

    @Test
    public void testCreateDndRecords(){
        cut.createDnsRecords();
    }

    @Test
    public void testUpdateDndRecords(){
        cut.updateDnsRecord();
    }

}