package core.datasource;


/**
 *
 * @author julien jeanbourquin (julien.jeanbourquin AT hotmail.com)
 */
public class ContrainteUniquePersistenceException extends PersistenceException {

    public ContrainteUniquePersistenceException() {
    }

    public ContrainteUniquePersistenceException(final String message) {
        super(message);
    }

    public ContrainteUniquePersistenceException(final String message,
            final Throwable cause) {
        super(message, cause);
    }

    public ContrainteUniquePersistenceException(final Throwable cause) {
        super(cause);
    }

    public ContrainteUniquePersistenceException(final String message,
            final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
