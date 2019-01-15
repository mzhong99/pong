import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.control.Button;

import javafx.scene.shape.Rectangle;

import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

import javafx.scene.text.Text;

import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.application.Platform;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class Pong {
    
    private class Board {
        
        private final VBox root = new VBox(6);
        private final Pane view = new Pane();

        public Board() {

            root.setPadding(new Insets(6));
            
            Rectangle topBorder = new Rectangle(
                WORLD_WIDTH + (2 * PROJECTILE_WIDTH), 
                WORLD_HEIGHT_OFFSET
            );

            Rectangle bottomBorder = new Rectangle(
                WORLD_WIDTH + (2 * PROJECTILE_WIDTH), 
                WORLD_HEIGHT_OFFSET
            );

            topBorder.setStroke(Color.GREY);
            topBorder.setFill(Color.GREY.darker());

            bottomBorder.setStroke(Color.GREY);
            bottomBorder.setFill(Color.GREY.darker());
            
            bottomBorder.setY(WORLD_HEIGHT + WORLD_HEIGHT_OFFSET);

            view.getChildren().addAll(topBorder, bottomBorder);
            
            StackPane wrapper = new StackPane();
            wrapper.getChildren().addAll(view);

            root.getChildren().addAll(wrapper);
        }
    }

    private class OptionsPanel {
        
        private final VBox   root   = new VBox(6);
        private final Button bStart = new Button("Start Game");
        private final Button bReset = new Button("Reset");
        
        public OptionsPanel() {

            root.setPadding(new Insets(6));
            
            bStart.setOnAction(new EventHandler<ActionEvent>() {
                
                @Override
                public void handle(ActionEvent event) {

                    IS_RUNNING = true;
                    LAST_TIME_NS = System.nanoTime();

                    bStart.setDisable(true);
                    bReset.setDisable(false);

                    RED_SCORE  = 0;
                    BLUE_SCORE = 0;

                    Platform.runLater(new UpdateWorldEvent());
                }
            });

            bReset.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    
                    IS_RUNNING = false;

                    bStart.setDisable(false);
                    bReset.setDisable(true);
                }
            });

            bReset.setDisable(true);

            Entity redScore  = new Score(selfReference, TeamType.RED);
            Entity blueScore = new Score(selfReference, TeamType.BLUE);

            Text redScoreView  = (Text)  redScore.components.get(CType.SCORE_VIEW); 
            Text blueScoreView = (Text) blueScore.components.get(CType.SCORE_VIEW);

            root.getChildren().addAll(bStart, bReset, redScoreView, blueScoreView);
        }
    }

    private class UpdateWorldEvent implements Runnable {

        @Override
        public void run() {
            
            long  TIME_NS = System.nanoTime();
            DELTA_TIME_NS = TIME_NS - LAST_TIME_NS;
            LAST_TIME_NS  = TIME_NS;
            
            for (GameSystem system : systems) {
                system.update(entities, deltaTime());
            }
            
            if (IS_RUNNING) {
                Platform.runLater(new UpdateWorldEvent());
            }

            renderSystem.update(entities, deltaTime());
        }
    }

    private final Pong selfReference = this;

    public  final Stage mainStage;
    public  final Scene mainScene;

    public  final Map<EType, List<Entity>> entities     = new HashMap<EType, List<Entity>>();
    private final List<GameSystem>         systems      = new ArrayList<GameSystem>();
    private final RenderSystem             renderSystem = new RenderSystem(this);

    private       boolean IS_RUNNING          = false;
    public  final int     WORLD_HEIGHT_OFFSET = 20;
    public  final int     WORLD_WIDTH         = 800;
    public  final int     WORLD_HEIGHT        = 600;
    public  final int     PROJECTILE_WIDTH    = 15;
    public  final int     SCORE_LIMIT         = 5;

    private int RED_SCORE  = 0;
    private int BLUE_SCORE = 0;

    private Board        board;
    private OptionsPanel options;

    private double LAST_TIME_NS;
    private double DELTA_TIME_NS;

    public Pong(Stage primaryStage) {
        
        mainStage = primaryStage;
        mainStage.show();

        for (EType val : EType.values()) {
            entities.put(val, new ArrayList<Entity>());
        }
        
        board   = new Board();
        options = new OptionsPanel();
        
        systems.add(new VelocitySystem());
        // systems.add(new CollisionScoringSystem(this));

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(board.root);
        borderPane.setRight(options.root);
        mainScene = new Scene(borderPane);
        mainStage.setScene(mainScene);

        Entity projectile = new Projectile(this);
        Entity redPaddle  = new Paddle(this, TeamType.RED);
        Entity bluePaddle = new Paddle(this, TeamType.BLUE);

        board.view.getChildren().addAll(
            (Rectangle) projectile.components.get(CType.VIEW),
            (Rectangle)  redPaddle.components.get(CType.VIEW),
            (Rectangle) bluePaddle.components.get(CType.VIEW)
        );
        
        renderSystem.update(entities, 0.1);
    }

    public double deltaTime()          { return DELTA_TIME_NS * 1e-9; }
    public int    redScore()           { return RED_SCORE;            }
    public int    blueScore()          { return BLUE_SCORE;           }
    public void   incrementRedScore()  { RED_SCORE++;                 }
    public void   incrementBlueScore() { BLUE_SCORE++;                }
}
