package code;

import code.gui.GUI;
import code.model.Model;

import javax.swing.*;

/**
 * Created by Stephen on 3/27/2016.
 */
public class Driver {

    public static void main(String[] args) {
        Model model = null;

        // Attempt to restore a game if command-line arguments were passed
        if (args.length > 0) {
            model = Model.load(args[0]);
        }
        if (model == null) {
            model = new Model();
        }

        SwingUtilities.invokeLater(new GUI(model));
    }

}
