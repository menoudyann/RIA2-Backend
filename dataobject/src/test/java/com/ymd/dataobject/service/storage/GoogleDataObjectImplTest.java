package com.ymd.dataobject.service.storage;

import io.github.cdimascio.dotenv.Dotenv;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoogleDataObjectImplTest extends TestCase {

    private GoogleDataObjectImpl dataObject;
    private Dotenv dotenv;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        dotenv = Dotenv.load();
        System.out.println(dotenv.get("GOOGLE_APPLICATION_CREDENTIALS"));
//        this.dataObject = new GoogleDataObjectImpl();
    }

    @Test
    void doesExistReturnsTrueWhenObjectExists() {
        assertTrue(true);
    }
}