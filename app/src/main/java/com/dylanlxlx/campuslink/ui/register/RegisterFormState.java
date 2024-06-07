package com.dylanlxlx.campuslink.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the register form.
 */
class RegisterFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer codeError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer password2Error;
    private boolean isDataValid;

    RegisterFormState(@Nullable Integer usernameError, @Nullable Integer emailError, @Nullable Integer codeError, @Nullable Integer passwordError, @Nullable Integer password2Error) {
        this.usernameError = usernameError;
        this.emailError = emailError;
        this.codeError = codeError;
        this.passwordError = passwordError;
        this.password2Error = password2Error;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.emailError = null;
        this.codeError = null;
        this.passwordError = null;
        this.password2Error = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getemailError() {
        return emailError;
    }
    @Nullable
    Integer getcodeError() {
        return codeError;
    }
    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }
    @Nullable
    Integer getPassword2Error() {
        return password2Error;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}