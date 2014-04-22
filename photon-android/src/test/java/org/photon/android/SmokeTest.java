package org.photon.android;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class SmokeTest extends TestCase {

    public SmokeTest(String testName) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite(SmokeTest.class);
    }

    public void testApp(){
        assertTrue(true);
    }
}
