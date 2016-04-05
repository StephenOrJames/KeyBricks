package code.model;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Stephen on 3/26/2016.
 */
public class Model {

    // Define the size of the Model
    public static final int COLUMNS = 6;
    public static final int ROWS = 6;

    // Instance variables
    private final ArrayList<ArrayList<Tile>> _board;  // the underlying structure of the model
    private final ArrayList<Tile> _unletteredTiles;  // Tiles that haven't been assigned a letter
    private char _nextLetter;  // the next letter to be assigned to a Tile
    private int _score;  // the user's score

    public Model() {
        _board = new ArrayList<ArrayList<Tile>>(COLUMNS);
        _unletteredTiles = new ArrayList<Tile>(COLUMNS * ROWS);
        _nextLetter = 'A';
        _score = 0;

        /* Initialize the Tiles within the board */
        Tile tile;
        for (int c = 0; c < COLUMNS; c++) {
            ArrayList<Tile> column = new ArrayList<Tile>(ROWS);
            for (int r = 0; r < ROWS; r++) {
                tile = new Tile(Tile.getRandomColor());
                column.add(tile);
                _unletteredTiles.add(tile);
            }
            _board.add(column);
        }

        /* Start with 5 lettered tiles */
        for (int i = 0; i < 5; i++) {
            assignNextLetter();
        }
    }

    /**
     * Assign the next letter in sequence to an unlettered {@link Tile}.
     */
    private void assignNextLetter() {
        if (!_unletteredTiles.isEmpty()) {
            Random random = new Random();
            int size = _unletteredTiles.size();
            int index = random.nextInt(size);

            Tile tile = _unletteredTiles.remove(index);
            tile.setLetter(_nextLetter);

            if (_nextLetter == 'Z') {
                _nextLetter = 'A';
            } else {
                _nextLetter++;
            }
        }
    }

    public ArrayList<ArrayList<Tile>> getBoard() {
        return _board;
    }

    /**
     * Get the column that contains a specified {@link Tile}.
     * @param tile    The {@link Tile} whose column is being searched for.
     * @return The column ({@link ArrayList}&lt;{@link Tile}&gt;), or {@code null} if {@code tile} wasn't found.
     */
    private ArrayList<Tile> getColumn(Tile tile) {
        for (ArrayList<Tile> column : _board) {
            if (column.contains(tile)) {
                return column;
            }
        }
        return null;
    }

    /**
     * Get the user's score.
     * @return The user's score.
     */
    public int getScore() {
        return _score;
    }

    /**
     * Remove the given {@link Tile} from the board.
     * If the {@link Tile} had a letter assigned, add a letter to another one.
     * @param tile    The {@link Tile} to be removed.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    private boolean removeTile(Tile tile) {
        for (ArrayList<Tile> column : _board) {
            if (column.remove(tile)) {
                if (tile.hasLetter()) {
                    assignNextLetter();
                } else {
                    _unletteredTiles.remove(tile);
                }

                // Remove the column if it is empty
                if (column.isEmpty()) {
                    _board.remove(column);

                    // End the game if no columns remain
                    if (_board.isEmpty()) {
                        System.out.println("Your score is " + _score + "!");
                        System.exit(0);
                    }
                }

                return true;
            }
        }
        return false;
    }

    /**
     * Remove all {@link Tile}s with the same {@link Color} that are
     * adjacent to the Tile whose letter was pressed.
     * @param letter    The letter of the {@link Tile} to be removed.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    public boolean removeLetterGroup(char letter) {
        ArrayList<Tile> toCheck = new ArrayList<Tile>();
        HashSet<Tile> toRemove = new HashSet<Tile>();
        Tile checking;
        Tile tile;
        ArrayList<Tile> column = null;
        Color color = null;

        for (ArrayList<Tile> c : _board) {
            for (Tile t : c) {
                if (t.getLetter() == letter) {
                    toCheck.add(t);
                    color = t.getColor();
                }
            }
        }

        while (!toCheck.isEmpty()) {
            checking = toCheck.get(0);
            column = getColumn(checking);
            if (column == null) {
                return false;
            }
            int colIndex = _board.indexOf(column);
            int rowIndex = column.indexOf(checking);

			// Check Tile below
            if (rowIndex > 0) {
                tile = column.get(rowIndex - 1);
                if (tile.getColor().equals(color)) {
                    if (!toCheck.contains(tile) && !toRemove.contains(tile)) {
                        toCheck.add(tile);
                    }
                }
            }

			// Check Tile above
            if (rowIndex + 1 < column.size()) {
                tile = column.get(rowIndex + 1);
                if (tile.getColor().equals(color)) {
                    if (!toCheck.contains(tile) && !toRemove.contains(tile)) {
                        toCheck.add(tile);
                    }
                }
            }

			// Check Tile to the left
            if (colIndex > 0) {
                column = _board.get(colIndex - 1);
                if (rowIndex < column.size()) {
                    tile = column.get(rowIndex);
                    if (tile.getColor().equals(color)) {
                        if (!toCheck.contains(tile) && !toRemove.contains(tile)) {
                            toCheck.add(tile);
                        }
                    }
                }
            }

			// Check Tile to the right
            if (colIndex + 1 < _board.size()) {
                column = _board.get(colIndex + 1);
                if (rowIndex < column.size()) {
                    tile = column.get(rowIndex);
                    if (tile.getColor().equals(color)) {
                        if (!toCheck.contains(tile) && !toRemove.contains(tile)) {
                            toCheck.add(tile);
                        }
                    }
                }
            }

            toCheck.remove(checking);
            toRemove.add(checking);
        }
        if (toRemove.isEmpty()) {
            return false;
        }

        // Update score (must be before removal since game ends during removal)
        _score += Math.pow(toRemove.size(), 2);

		// Remove tiles
        toRemove.forEach(this::removeTile);

        return true;
    }

    /**
     * Writes the current state of the Model to a file (keybricks.txt).
     * @see #load(String)
     */
    // TODO: allow saving and restoration to track score
    public void save() {
        String modelString = "";

        for (ArrayList<Tile> column : _board) {
            for (Tile tile : column) {
                modelString += tile;
            }

            // Placeholders to fill the gap of removed Tiles
            for (int r = column.size(); r < ROWS; r++) {
                modelString += " K";
            }
        }

        // Placeholders to fill the gap of removed columns
        for (int c = _board.size(); c < COLUMNS; c++) {
            for (int r = 0; r < ROWS; r++) {
                modelString += " K";
            }
        }

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new File("keybricks.txt"));
            printWriter.println(modelString);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    /**
     * Loads a saved {@link Model} from a specified file.
     * @param fileName    The name of the file with the saved {@link Model} to be loaded.
     * @return The loaded {@link Model}, or {@code null} if none could be loaded.
     * @see #save()
     */
    public static Model load(String fileName) {
        // Read file
        String modelString = "";
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName));
            while (scanner.hasNext()) {
                modelString += scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        // Cannot load from empty file, and each tile is represented with 2 chars
        if (modelString.equals("") || modelString.length() % 2 == 1) {
            return null;
        }
        Model model = new Model();

        // Determine the largest letter in the model.
        // Note: this does not account for wrapping back to A.
        char largestLetter = modelString.charAt(0);
        char nextChar;
        for (int i = 2; i < modelString.length(); i += 2) {
            nextChar = modelString.charAt(i);
            if (largestLetter < nextChar) {
                largestLetter = nextChar;
            }
        }
        model._nextLetter = (char)(largestLetter + 1);

        // Update the board to reflect the saved Tiles
        ArrayList<Tile> column;
        Tile tile;
        int index;  // the base index of a Tile's data in modelString
        model._unletteredTiles.clear();
        for (int c = 0; c < COLUMNS; c++) {
            column = model._board.get(c);
            for (int r = 0; r < ROWS; r++) {
                index = (c*ROWS + r) * 2;
                tile = Tile.fromString(modelString.substring(index, index+2));
                column.set(r, tile);
                if (tile != null && !tile.hasLetter()) {
                    model._unletteredTiles.add(tile);
                }
            }
            column.removeAll(Collections.singleton((Tile) null));  // remove non-Tiles
            if (column.isEmpty()) {
                model._board.set(c, null);
            }
        }
        model._board.removeAll(Collections.singleton((ArrayList<Tile>) null));  // remove empty columns

        return model;
    }

}
