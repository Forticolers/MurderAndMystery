package core.datasource;


/**
 *
 * @author julien jeanbourquin (julien.jeanbourquin AT hotmail.com)
 */
public class EntiteTropAnciennePersistenceException
        extends PersistenceException {

    public EntiteTropAnciennePersistenceException() {
    }

    public EntiteTropAnciennePersistenceException(final String message) {
        super(message);
    }

    public EntiteTropAnciennePersistenceException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }

    public EntiteTropAnciennePersistenceException(final Throwable cause) {
        super(cause);
    }

}
