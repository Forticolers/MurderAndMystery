package core.datasource;


/**
 *
 * @author julien jeanbourquin (julien.jeanbourquin AT hotmail.com)
 */
public class ContrainteNotNullPersistenceException extends PersistenceException {

    public ContrainteNotNullPersistenceException() {
    }

    public ContrainteNotNullPersistenceException(final String message) {
        super(message);
    }

    public ContrainteNotNullPersistenceException(final String message,
            final Throwable cause) {
        super(message, cause);
    }

    public ContrainteNotNullPersistenceException(final Throwable cause) {
        super(cause);
    }

    public ContrainteNotNullPersistenceException(final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
