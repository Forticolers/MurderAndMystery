package core.domain;

/**
 *
 * @author julien jeanbourquin (julien.jeanbourquin AT hotmail.com)
 */
public interface Entite<T> {

    Identifiant getIdentifiant();

    Audit getAudit();

    void update(T entite);

}
