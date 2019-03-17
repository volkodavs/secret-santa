package com.sergeyvolkodav.secretsanta.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SecretSantaExceptionTest {

    @Test
    public void errorCodeSetTest() {
        SecretSantaException santaException
                = new SecretSantaException("No pair found", ErrorCode.NO_PAIR_FOUND);

        assertThat(santaException.getErrorCode()).isEqualTo(ErrorCode.NO_PAIR_FOUND);
        assertThat(santaException.getMessage()).isEqualTo("No pair found");
    }
}
