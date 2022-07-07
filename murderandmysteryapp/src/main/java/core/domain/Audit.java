package core.domain;

import java.time.Instant;

/**
 *
 * @author julien jeanbourquin (julien.jeanbourquin AT hotmail.com)
 */
public interface Audit {

    Instant getDateCreation();

    String getUserCreation();

    Instant getDateModification();

    String getUserModification();

    Instant getDateSuppression();

    String getUserSuppression();

}
