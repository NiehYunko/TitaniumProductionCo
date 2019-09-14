package com.titaniumproductionco.db.services.func;

@FunctionalInterface
public interface IErrorMessage {
    /**
     * Get error message based on the error code
     * 
     * @param error
     * @return
     */
    String getErrorMessage(int error);
}
