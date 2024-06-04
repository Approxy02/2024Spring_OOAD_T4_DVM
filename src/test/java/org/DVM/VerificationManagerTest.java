package org.DVM;

import org.DVM.Control.Verification.VerificationManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerificationManagerTest {
    private final VerificationManager verificationManager = new VerificationManager();

    @Test
    public void testVerifyVCode() {
        assertTrue(verificationManager.verifyVCode("1234567890"), "Verification should be successful");
        assertFalse(verificationManager.verifyVCode("1234567891"), "Verification should be Failed");
    }
}
