package org.rhw.bmr.project.common.convention.exception;

import org.rhw.bmr.project.common.convention.errorcode.BaseErrorCode;
import org.rhw.bmr.project.common.convention.errorcode.IErrorCode;

/**
 * Remote service invocation exception
 */
public class RemoteException extends AbstractException {

    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }

    public RemoteException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "RemoteException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
