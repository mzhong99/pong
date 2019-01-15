import javafx.scene.text.Text;

public class Score extends Entity {
    
    public Score(Pong game, TeamType team) {
        super(game, team);
    }

    @Override
    protected void init() {
        
        Text text = new Text();
        TeamType team = (TeamType)components.get(CType.TEAM);

        if (team == TeamType.RED ) text.setText("Red: 0");
        if (team == TeamType.BLUE) text.setText("Blue: 0");
        
        components.put(CType.SCORE_VIEW, text);
        entityTypes.add(EType.SCORABLE);
    }
}
