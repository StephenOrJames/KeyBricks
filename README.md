# KeyBricks

This is a version of the [original KeyBricks game](http://www.typinggames.zone/keybricks.html), and was not indended to be exactly like it.

The game starts out with a 6×6 board of colored tiles (red, blue, green, and yellow).
Up to 5 tiles will have a letter displayed on them.
By pressing the corresponding letter on your keyboard, you will remove the tile with that letter.
Additionally, all tiles that have the same color as a tile being removed, and directly shares an edge with a such tile, will also get removed.
In other words, any area directly linked to and having the same color as the selected tile will be removed.
This will continue until the board contains no more tiles.


## Features

### Saving and Restoring ("Loading")
The state of the game can be saved at any time before the game ends by clicking the *Save* button.
This will write a plaintext representation of the game board to a file called "keybricks.txt".
A saved game can be restored by passing the name of the file containing the saved game state as the first command-line argument.
Note: At the moment, saved games will not retain their when restored.

### Scoring
A user's score starts at 0.
Whenever a letter that removes a tile (or group of tiles) is typed, the score increases by the square of the number of types being removed at that time.
This means that one tile gives one point, two tiles give four points, 3 points give 9 points, and so on.
Therefore, the aim should be to eliminate a maximum number of tiles each play.
