import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Projectile extends Entity {

    private static final double STARTING_SPEED = 350;
    private static final double DEFAULT_SIZE   = 40;

    public Projectile(Pong game) {
        super(game);
    }

    @Override
    protected void init() {

        Vector2D position = new Vector2D(
            (game.WORLD_WIDTH  - DEFAULT_SIZE) * 0.5, 
            (game.WORLD_HEIGHT - DEFAULT_SIZE) * 0.5
        );

        components.put(CType.POSITION, position);
        components.put(CType.VELOCITY, new Vector2D(STARTING_SPEED, 0));
        
        Rectangle rectangle = new Rectangle(DEFAULT_SIZE, DEFAULT_SIZE);

        rectangle.setStroke(Color.DARKGRAY);
        rectangle.setFill(Color.DARKSLATEGRAY.darker());
        rectangle.setX(position.x);
        rectangle.setY(position.y);

        components.put(CType.VIEW, rectangle);

        entityTypes.add(EType.MOVABLE);
        entityTypes.add(EType.VIEWABLE);
        entityTypes.add(EType.BLOCKABLE);
    }
}
