package nz.co.delacour.firefall.core.entities;

import lombok.Data;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.annotations.Entity;
import nz.co.delacour.firefall.core.annotations.SubCollectionEntity;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 17/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@Data
@Entity
public class BasicSubCollectionEntity extends HasId<BasicSubCollectionEntity> {

    private String testString;

    public BasicSubCollectionEntity() {
        super(BasicSubCollectionEntity.class);
    }

}