package cpsc2150.extendedTicTacToe;

/**
 * The TicTacToe controller class will handle communication between our TicTacToeView and our Model (IGameBoard and BoardPosition)
 * <p>
 * This is where you will write code
 * <p>
 * You will need to include your BoardPosition class, the IGameBoard interface
 * and the implementations from previous homeworks
 * If your code was correct you will not need to make any changes to your IGameBoard classes
 */
public class TicTacToeController {

    //our current game that is being played
    private IGameBoard curGame;

    //The screen that provides our view
    private TicTacToeView screen;

    // variable that will allow turns to be incremented
    private int turns;
    // hard coded values for the player markers
    Character[] players = {'X', 'O', 'A', 'B', 'C', 'M', 'K', 'S', 'T', 'Z'};
    // the number of players that will be used in the constructor
    private int numPlayers;
    // allows the game to keep going until the game is over
    private boolean gameDone;

    public static final int MAX_PLAYERS = 10;

    /**
     * @param model the board implementation
     * @param view  the screen that is shown
     * @param np    The number of players for the game
     *
     * @post the controller will respond to actions on the view using the model.
     */
    public TicTacToeController(IGameBoard model, TicTacToeView view, int np) {
        this.curGame = model;
        this.screen = view;
        numPlayers = np;
    }

    /**
     * @param row the row of the activated button
     * @param col the column of the activated button
     *
     * @pre row and col are in the bounds of the game represented by the view
     * @post The button pressed will show the right token and check if a player has won.
     */
    public void processButtonClick(int row, int col) {
       // if the game is done, then a new game is created
        if (gameDone) {
            newGame();
            gameDone = false;
        }

        // if the position is not available, then a message is printed
        if (!curGame.checkSpace(new BoardPosition(row, col))) {
            screen.setMessage("Position is taken.");
            return;
        }

        // gets the current player
        char curPlayer = players[(turns % numPlayers)];

        // checks that the position is empty
        BoardPosition pos = new BoardPosition(row, col);
        while (curGame.whatsAtPos(pos) != ' ') {
            row ++;
            pos = new BoardPosition(row, col);
        }

        // places the marker on the board then moves to the next turn
        curGame.placeMarker(new BoardPosition(row, col), curPlayer);
        screen.setMarker(row, col, curPlayer);
        turns ++;

        // if there is a winner, then a message is printed and the user can play again
        if (curGame.checkForWinner(new BoardPosition(row, col))) {
            screen.setMessage("Player " + curPlayer + " won! Click any space to replay.");
            gameDone = true;
            return;
        }

        // if there is a tie, then a message is printed and the user can play again
        if (curGame.checkForDraw()) {
            screen.setMessage("Tie! Click any space to replay.");
            gameDone = true;
            return;
        }

        // prints message after each turn to show who is up
        screen.setMessage("It is Player " + players[(turns % numPlayers)] + "'s turn.");
    }

    private void newGame() {
        // You do not need to make any changes to this code.
        screen.dispose();
        GameSetupScreen screen = new GameSetupScreen();
        GameSetupController controller = new GameSetupController(screen);
        screen.registerObserver(controller);
    }
}