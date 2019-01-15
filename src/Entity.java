import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

public abstract class Entity {

    public static long GLOBAL_ID = 0;
    
    public final long ID;
    public final Map<CType, Object> components = new HashMap<CType, Object>();
    public final Set<EType> entityTypes        = new HashSet<EType>();
    
    public final Pong game;

    public Entity(Pong game, TeamType team) {

        this.ID   = GLOBAL_ID++;
        this.game = game;
        
        if (team != null) {
            components.put(CType.TEAM, team);
        }

        init();

        for (EType entityType : entityTypes) {
            game.entities.get(entityType).add(this);
        }
    }

    public Entity(Pong game) {
        this(game, null);
    }

    protected abstract void init();

    @Override
    public int hashCode() {
        return Long.hashCode(ID);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        else if (this == other) {
            return true;
        }
        else if (this.getClass() == other.getClass()) {
            return ID == ((Entity)other).ID;
        }
        else {
            return false;
        }
    }
}
