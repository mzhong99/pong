import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Paddle extends Entity {

    private static final int PADDLE_WIDTH  = 30;
    private static final int PADDLE_HEIGHT = 100;
    private static final int PADDLE_SPEED  = 400;
    
    public Paddle(Pong game, TeamType team) {
        super(game, team);
    }

    @Override
    protected void init() {
        
        Vector2D position = new Vector2D(0, 0.5 * (game.WORLD_HEIGHT - PADDLE_HEIGHT));

        TeamType team = (TeamType)components.get(CType.TEAM);

        if (team == TeamType.BLUE) {
            position.x = game.WORLD_WIDTH - PADDLE_WIDTH;
        }

        Rectangle rectangle = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT);
        Color teamColor = team == TeamType.RED ? Color.RED : Color.BLUE;

        rectangle.setStroke(teamColor);
        rectangle.setFill(teamColor.darker());
        rectangle.setX(position.x);
        rectangle.setY(position.y);
        
        game.mainScene.addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (team == TeamType.RED) {
                    if (KeyEvent.KEY_PRESSED == event.getEventType()) {
                        switch(event.getCode()) {
                            case W: moveUp();   break;
                            case S: moveDown(); break;
                        }
                    }
                    if (KeyEvent.KEY_RELEASED == event.getEventType()) {
                        switch(event.getCode()) {
                            case W: 
                            case S: standStill();
                        }
                    }
                }
                if (team == TeamType.BLUE) {
                    if (KeyEvent.KEY_PRESSED == event.getEventType()) {
                        switch(event.getCode()) {
                            case I: moveUp();   break;
                            case K: moveDown(); break;
                        }
                    }
                    if (KeyEvent.KEY_RELEASED == event.getEventType()) {
                        switch(event.getCode()) {
                            case I:
                            case K: standStill();
                        }
                    }
                }
            }
        });

        components.put(CType.VIEW, rectangle);
        components.put(CType.POSITION, position);
        components.put(CType.VELOCITY, new Vector2D());

        entityTypes.add(EType.MOVABLE);
        entityTypes.add(EType.VIEWABLE);
        entityTypes.add(EType.COLLIDABLE);
        entityTypes.add(EType.CONTROLABLE);
    }

    private void moveUp() {
        components.put(CType.VELOCITY, new Vector2D(0, -1 * PADDLE_SPEED));
    }
    
    private void moveDown() {
        components.put(CType.VELOCITY, new Vector2D(0, PADDLE_SPEED));
    }

    private void standStill() {
        components.put(CType.VELOCITY, new Vector2D());
    }
}

