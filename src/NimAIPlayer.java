/*
	NimAIPlayer.java
	
	This class is provided as a skeleton code for the tasks of 
	Sections 2.4, 2.5 and 2.6 in Project C. Add code (do NOT delete any) to it
	to finish the tasks. 
*/

public class NimAIPlayer extends NimPlayer implements Testable {
    // you may further extend a class or implement an interface
    // to accomplish the tasks.

    public NimAIPlayer(String UserName, String FamilyName, String GivenName, boolean IsAI) {
        super(UserName, FamilyName, GivenName, IsAI);

    }

    // AI player's removal, which could guarantee its victory if situation permitted.

    public int Remove(int MaximumMove,int StoneLeft,int HumanMove){
        int Remove;
        int n = StoneLeft/(MaximumMove+1);
        if((n*(MaximumMove+1)+2)<=StoneLeft && (n+1)*(MaximumMove+1)>=StoneLeft){
            Remove = StoneLeft - (n*(MaximumMove+1)+1);
        }
        else{
            Remove = MaximumMove;
        }
        return Remove;
    }

    public String advancedMove(boolean[] available, String lastMove) {
        // the implementation of the victory
        // guaranteed strategy designed by you
        String move = "";

        return move;
    }
}