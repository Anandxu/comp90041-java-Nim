public abstract class NimPlayer {

    public NimPlayer(){

    }

    String UserName;
    String FamilyName;
    String GivenName;
    boolean IsAI;
    int Game = 0;
    int Win = 0;

public NimPlayer(String UserName, String FamilyName, String GivenName, boolean IsAI) {

    this.UserName = UserName;
    this.FamilyName = FamilyName;
    this.GivenName = GivenName;
    this.IsAI = IsAI;

}

public String getUserName(){
    return UserName;

}

public String getFamilyName(){
    return FamilyName;
}

public String getGivenName(){
    return GivenName;
}

public void setGame(int Game){
    this.Game = Game;
}

public int getGame()
{
    return Game;
}

public void setWin(int Win){
    this.Win = Win;
}

public int getWin(){
    return Win;
}

// Store boolean data as strings.

public String AI(){
    String AI;
    if (IsAI){
        AI = "true";
        return AI;
    }
    else{
        AI = "false";
        return AI;
    }
}

// Calculate the win rate of the player.

public double WinRatio(){
    double WinRatio;
    if(this.Win==0){
        WinRatio = 0.00;
    }
    else WinRatio = this.Win/this.Game*1.00;
    return WinRatio;
}

// Update the data if the player wins.

public void WinGame(){
    this.Game ++;
    this.Win ++;
}

// Update the data if the player loses.

public void LoseGame(){
    this.Game ++;
}

public abstract int Remove(int MaximumMove,int StoneLeft,int HumanMove);


}