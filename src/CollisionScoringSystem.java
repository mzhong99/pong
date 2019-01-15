import java.util.Map;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.shape.Rectangle;

public class CollisionScoringSystem implements GameSystem {
    
    private final Pong game;

    public CollisionScoringSystem(Pong game) {
        this.game = game;
    }

    @Override
    public void update(Map<EType, List<Entity>> entities, double deltaTime) {
        
        List<Entity>  blockables = entities.get(EType.BLOCKABLE );
        List<Entity> collidables = entities.get(EType.COLLIDABLE);
        
        for (Entity blockable : blockables) {
            for (Entity collidable : collidables) {
                resolveCollision(blockable, collidable);
            }
        }

        for (Entity entity : blockables) {
            resolveWorldCollision(entity);
        }
        
        for (Entity entity : collidables) {
            resolveWorldCollision(entity);
        }
    }

    private void resolveCollision(Entity blockable, Entity collidable) {
        
        Rectangle rect1 = (Rectangle)  blockable.components.get(CType.VIEW);
        Rectangle rect2 = (Rectangle) collidable.components.get(CType.VIEW);

        List<Vector2D> vert1 = getVertices(rect1);
        List<Vector2D> vert2 = getVertices(rect2);
        
        boolean needsResolving = false;

        for (Vector2D vertex : vert1) {
            if (vertexLiesWithin(vertex, rect2)) {
                needsResolving = true;
            }
        }

        for (Vector2D vertex : vert2) {
            if (vertexLiesWithin(vertex, rect1)) {
                needsResolving = true;
            }
        }

        if (needsResolving) {
            resolveCollisionX(blockable, collidable);
        }
    }

    private List<Vector2D> getVertices(Rectangle rect) {
        
        List<Vector2D> vertices = new ArrayList<Vector2D>();

        vertices.add(new Vector2D(rect.getX()                  , rect.getY()                   ));
        vertices.add(new Vector2D(rect.getX() + rect.getWidth(), rect.getY()                   ));
        vertices.add(new Vector2D(rect.getX()                  , rect.getY() + rect.getHeight()));
        vertices.add(new Vector2D(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()));

        return vertices;
    }

    private boolean vertexLiesWithin(Vector2D vertex, Rectangle rect) {
        return vertex.x > rect.getX() 
            && vertex.y > rect.getY()
            && vertex.x < rect.getX() + rect.getWidth()
            && vertex.y < rect.getY() + rect.getHeight();
    }

    private void resolveCollisionX(Entity blockable, Entity collidable) {

        Vector2D  blockPosition = (Vector2D)   blockable.components.get(CType.POSITION);
        Vector2D  blockVelocity = (Vector2D)   blockable.components.get(CType.VELOCITY);

        Rectangle blockRect     = (Rectangle)  blockable.components.get(CType.VIEW    );
        Rectangle colliderRect  = (Rectangle) collidable.components.get(CType.VIEW    );

        if (blockVelocity.x < 0) {
            game.incrementRedScore();
            blockPosition.x = colliderRect.getX() + colliderRect.getWidth() + 1.0;
        }
        else {
            game.incrementBlueScore();
            blockPosition.x = colliderRect.getX() - blockRect.getWidth() - 1.0;
        }

        blockVelocity.x *= -1;

        blockable.components.put(CType.POSITION, blockPosition);
        blockable.components.put(CType.VELOCITY, blockVelocity);
    }

    private void resolveWorldCollision(Entity entity) {
        
        Vector2D  position = (Vector2D)  entity.components.get(CType.POSITION);
        Vector2D  velocity = (Vector2D)  entity.components.get(CType.VELOCITY);
        Rectangle rectData = (Rectangle) entity.components.get(CType.VIEW    );
        
        boolean needsReflect = false;
        
        if (position.y < game.WORLD_HEIGHT_OFFSET) {
            position.y = game.WORLD_HEIGHT_OFFSET + 1.0;
            needsReflect = true;
        }
        
        if (position.y > game.WORLD_HEIGHT_OFFSET - rectData.getHeight()) {
            position.y = game.WORLD_HEIGHT_OFFSET - rectData.getHeight() - 1.0;
            needsReflect = true;
        }

        if (needsReflect && velocity != null) {
            velocity.y *= -1;
        }

        entity.components.put(CType.POSITION, position);
        
        if (velocity != null) {
            entity.components.put(CType.VELOCITY, velocity);
        }
    }
}
