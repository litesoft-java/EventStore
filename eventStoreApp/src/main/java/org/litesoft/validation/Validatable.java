package org.litesoft.validation;

public interface Validatable {
    /**
     * Validate an object.
     *
     * @return Validation object (never Null)
     */
    Validation validate();
}
