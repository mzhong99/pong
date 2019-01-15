import java.util.Map;
import java.util.List;

import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;

public class RenderSystem implements GameSystem {
    
    private final Pong game;

    public RenderSystem(Pong game) {
        this.game = game;
    }

    @Override
    public void update(Map<EType, List<Entity>> entities, double deltaTime) {

        List<Entity> viewables = entities.get(EType.VIEWABLE);
        List<Entity> scorables = entities.get(EType.SCORABLE);

        for (Entity viewable : viewables) {
            updateView(viewable);
        }

        for (Entity scorable : scorables) {
            updateScore(scorable);
        }
    }

    private void updateView(Entity viewable) {
        
        Vector2D  position = (Vector2D)  viewable.components.get(CType.POSITION);
        Rectangle view     = (Rectangle) viewable.components.get(CType.VIEW    );
        
        view.setX(position.x);
        view.setY(position.y);
    }

    private void updateScore(Entity scorable) {

        Text     scoreOutput = (Text)     scorable.components.get(CType.SCORE_VIEW);
        TeamType team        = (TeamType) scorable.components.get(CType.TEAM      );
        
        if (team == TeamType.RED ) {
            scoreOutput.setText("Red: "  + game.redScore() );
        }

        if (team == TeamType.BLUE) {
            scoreOutput.setText("Blue: " + game.blueScore());
        }
    }
}
