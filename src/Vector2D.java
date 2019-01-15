public class Vector2D {

	public double x;
	public double y;

	public Vector2D() {
		this(0, 0);
	}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D unitVector() {
		return (x == 0 && y == 0)
			? new Vector2D(0, 0)
			: new Vector2D(x / magnitude(), y / magnitude());
	}

	public double magnitude() {
		return Math.sqrt((x * x) + (y * y));
	}

    public void add(Vector2D rhs) {
        x += rhs.x;
        y += rhs.y;
    }

    public void subtract(Vector2D rhs) {
        x -= rhs.x;
        y -= rhs.y;
    }

	public static Vector2D add(Vector2D lhs, Vector2D rhs) {
		return new Vector2D(lhs.x + rhs.x, lhs.y + rhs.y);
	}

	public static Vector2D subtract(Vector2D lhs, Vector2D rhs) {
		return Vector2D.add(lhs, Vector2D.scale(-1, rhs));
	}

	public static Vector2D scale(double scalar, Vector2D vector) {
		return new Vector2D(scalar * vector.x, scalar * vector.y);
	}
}
