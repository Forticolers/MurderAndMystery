package murderandmystery.datasource.memory;

import core.domain.Identifiant;
import core.domain.IdentifiantBase;

/**
 *
 * @author jeanbourquj
 */
public class IdentifiantMemory extends IdentifiantBase implements Identifiant {

    /**
     *
     * @param identifiant
     */
    public IdentifiantMemory(final Identifiant identifiant) {
        super(IdentifiantBase.builder()
                .identifiant(identifiant));
    }

    /**
     *
     */
    public void incrementVersion() {
        this.setVersion(this.getVersion() + 1);
    }
}
