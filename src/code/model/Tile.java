package code.model;

import java.awt.*;
import java.util.Random;

/**
 * Created by Stephen on 3/26/2016.
 */
public class Tile {

    private Color _color;
    private char _letter;
    private final char NO_LETTER = ' ';

    /**
     * Create a Tile object with a specified color.
     * @param color    The {@link Color} of the Tile
     */
    public Tile(Color color) {
        _color = color;
        _letter = NO_LETTER;
    }

    /**
     * Determine if a letter has been assigned to the {@link Tile}.
     * @return {@code true} if a letter was assigned, {@code false} otherwise.
     */
    public boolean hasLetter() {
        return _letter != NO_LETTER;
    }

    /**
     * Retrieve the {@link Color} of the {@link Tile}.
     * @return the {@link Color} of the {@link Tile}
     */
    public Color getColor() {
        return _color;
    }

    /**
     * Retrieve the letter assigned to the {@link Tile}.
     * @return The letter assigned to the {@link Tile}, or {@code ' '} if none.
     */
    public char getLetter() {
        return _letter;
    }

    /**
     * Assign a letter to the {@link Tile}.
     * @param letter    A char to be displayed on the tile.
     */
    public void setLetter(char letter) {
        _letter = letter;
    }

    /**
     * Get a random {@link Color} to assign to a {@link Tile}.
     * @return A {@link Color} (either red, blue, green, or yellow).
     */
    public static Color getRandomColor() {
        Color[] colors = new Color[4];
        colors[0] = Color.RED;
        colors[1] = Color.BLUE;
        colors[2] = Color.GREEN;
        colors[3] = Color.YELLOW;
        Random r = new Random();
        return colors[r.nextInt(colors.length)];
    }

    public static Color charToColor(char c) {
        switch (c) {
            case 'R': return Color.RED;
            case 'B': return Color.BLUE;
            case 'G': return Color.GREEN;
            case 'Y': return Color.YELLOW;
            default: return null;
        }
    }

    public static char colorToChar(Color c) {
        if (c.equals(Color.RED)) {
            return 'R';
        } else if (c.equals(Color.BLUE)) {
            return 'B';
        } else if (c.equals(Color.GREEN)) {
            return 'G';
        } else if (c.equals(Color.YELLOW)) {
            return 'Y';
        } else {
            return 'K';
        }
    }

    public static Tile fromString(String string) {
        Color color = charToColor(string.charAt(1));
        if (color == null) {
            return null;
        }
        Tile tile = new Tile(color);
        tile.setLetter(string.charAt(0));
        return tile;
    }

    @Override
    public String toString() {
        return String.valueOf(_letter) + String.valueOf(colorToChar(_color));
    }

}
