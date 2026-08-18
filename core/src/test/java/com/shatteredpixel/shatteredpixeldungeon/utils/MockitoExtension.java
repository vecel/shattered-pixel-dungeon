package com.shatteredpixel.shatteredpixeldungeon.utils;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

public class MockitoExtension {

    public static <T> T verifyNever(T mock) {
        return verify(mock, never());
    }

    public static <T> T verifyOnce(T mock) {
        return verify(mock, times(1));
    }
}
