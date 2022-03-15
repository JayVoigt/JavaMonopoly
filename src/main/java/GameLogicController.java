/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jay
 */
public class GameLogicController {

    Board board;
    MainView view;

    Player currentPlayer;
    Space currentSpace;
    GameEvent currentGameEvent;
    Property currentProperty;

    boolean isGameActive;

    int playersCount,
        turnCounter;

    public GameLogicController(Board inputBoard) {
        board = inputBoard;
    }

    public void initialEvaluator() {
        currentPlayer = board.players.get(board.getCurrentPlayerID());

        if (currentPlayer.getIsJailed() == true) {
            jailStateEvaluator();
        } else {
            normalTurnEvaluator();
        }
    }

    private void jailStateEvaluator() {

    }

    private void normalTurnEvaluator() {
        if (currentPlayer.hasRolledDice == true) {
            currentPlayer.setIsInMAEState(true);
            maeStateEvaluator();
        } else {
            currentPlayer.setActionLockedRollDice(false);
        }
    }

    private void maeStateEvaluator() {
        if (currentPlayer.getIsInMAEState() == true) {
            currentPlayer.setActionLockedEndTurn(false);
        } else {
            if (currentPlayer.getHasRolledDice() == true) {
                if (currentPlayer.getMadeDecisionPropertyAction() == true) {
                    currentPlayer.setActionLockedEndTurn(false);
                    //view.update();
                } else {
                    propertyPurchaseManager();
                }
            } else {
                currentPlayer.setActionLockedRollDice(false);
                //view.update();
            }
        }
    }

    private void propertyPurchaseManager() {

    }

    public void diceRollManager() {
        currentPlayer = board.players.get(board.getCurrentPlayerID());
        
        currentPlayer.rollDice();
        //view.update();
        
        // Reset roll dice condition for doubles
        if (currentPlayer.getHasRolledDoubles() == true) {
            currentPlayer.setHasRolledDice(false);            
        }
        
        movementEvaluator();
    }
    
    private void movementEvaluator() {
        int diceSum = currentPlayer.getDiceSum();
        boolean playerPassedGo = currentPlayer.advancePosition(diceSum);
        
        // Issue GO bonus
        if (playerPassedGo == true) {
            currentPlayer.updateCurrentBalance(200);
        }
        
        currentSpace = board.spaces.get(currentPlayer.getCurrentPosition());
        currentSpace.incrementTimesLanded();
        
        if (currentSpace.getSpaceType().equals(Space.spaceTypeKeys.gameEvent)) {
            gameEventEvaluator();
        }
        else {
            propertyEvaluator();
        }
    }
    
    private void gameEventEvaluator() {
        
    }
    
    private void propertyEvaluator() {
        currentProperty = (Property)currentSpace;
        if (currentProperty.getIsOwned() == false) {
            currentPlayer.setMadeDecisionPropertyAction(false);
        }
        else {
            currentPlayer.setMadeDecisionPropertyAction(true);
        }
    }
    
    public void endTurnManager() {
        currentPlayer = board.players.get(board.getCurrentPlayerID());
        currentPlayer.setActionLockedEndTurn(true);
        //view.update();
    }
}
