package org.rhw.bmr.user.common.convention.exception;

import org.rhw.bmr.user.common.convention.errorcode.IErrorCode;
import org.rhw.bmr.user.common.convention.errorcode.BaseErrorCode;

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
