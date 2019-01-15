import java.util.Map;
import java.util.List;

public class VelocitySystem implements GameSystem {
    
    private static final EType singleTarget = EType.MOVABLE;
    
    @Override
    public void update(Map<EType, List<Entity>> entities, double deltaTime) {
        for (Entity movable : entities.get(singleTarget)) {
            updatePosition(movable, deltaTime);
        }
    }

    private void updatePosition(Entity movable, double deltaTime) {
        
        Vector2D position = (Vector2D)movable.components.get(CType.POSITION);
        Vector2D velocity = (Vector2D)movable.components.get(CType.VELOCITY);
        
        position.add(Vector2D.scale(deltaTime, velocity));
        
        movable.components.put(CType.POSITION, position);
    }
}
