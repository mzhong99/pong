import java.util.Map;
import java.util.List;

public interface GameSystem {
    public void update(Map<EType, List<Entity>> entities, double deltaTime);
}
