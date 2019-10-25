import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class NimGame{
    final int MAX_PLAYER = 100;

    int InitialStone;
    int MaximumMove;
    String Player1GName;
    String Player1FName;
    String Player2GName;
    String Player2FName;
    int StoneLeft;

    Scanner kb = new Scanner(System.in);
    NimPlayer[] Player = new NimPlayer[MAX_PLAYER];

    // Display the current number of stones.

    public void Information(){
        System.out.print(StoneLeft + " stones " + "left:");
        for(int i=0;i<StoneLeft;i++){
            System.out.print(" *");
        }
        System.out.println();
    }

    // The main program of Nim game.

    public void Play(int Mark1, int Mark2){
        int HumanMove = 0;
        int WantMove = 0;

        while (StoneLeft > 0){
            Information();
            System.out.println(Player1GName + "'s turn - remove how many?");
            if(!Player[Mark1].IsAI){
                while (true) {
                    HumanMove = kb.nextInt();

                    // Check if human player moves correctly.

                    try{
                        if (HumanMove > 0 && HumanMove <= MaximumMove && StoneLeft - HumanMove >= 0) {
                            break;
                        } else {
                            InvalidInputException e = new InvalidInputException();
                            throw e;
                        }
                    }
                    catch (InvalidInputException e){
                        System.out.println();
                        System.err.println("Invalid move. You must remove between 1 and " + MaximumMove + " stones.");
                        System.out.println();
                        Information();
                        System.out.println(Player1GName + "'s turn - remove how many?");

                    }
                }
            }
            WantMove = Player[Mark1].Remove(MaximumMove,StoneLeft,HumanMove);
            StoneLeft = StoneLeft - WantMove;
            System.out.println();

            if(StoneLeft==0){
                System.out.println("Game Over");
                System.out.println(Player2GName + " " + Player2FName + " wins!");
                Player[Mark2].WinGame();
                Player[Mark1].LoseGame();

                break;
            }

            Information();
            System.out.println(Player2GName + "'s turn - remove how many?");
            if(!Player[Mark2].IsAI){
                while (true) {
                    HumanMove = kb.nextInt();
                    try{
                        if (HumanMove > 0 && HumanMove <= MaximumMove && StoneLeft - HumanMove >= 0) {
                            break;
                        } else {
                            throw new InvalidInputException();

                        }
                    }
                    catch (InvalidInputException e){
                        System.out.println();
                        System.err.println("Invalid move. You must remove between 1 and " + MaximumMove + " stones.");
                        System.out.println();
                        Information();
                        System.out.println(Player2GName + "'s turn - remove how many?");

                    }
                }
            }
            WantMove = Player[Mark2].Remove(MaximumMove,StoneLeft,HumanMove);
            StoneLeft = StoneLeft - WantMove;
            System.out.println();

            if(StoneLeft==0){
                System.out.println("Game Over");
                System.out.println(Player1GName + " " + Player1FName + " wins!");
                Player[Mark1].WinGame();
                Player[Mark2].LoseGame();

                break;
            }
        }

        // Consume '/n' in the input stream.

        String blank = kb.nextLine();
    }

    // Judge whether the player exists.

    public boolean IsExist(String CmdSpilt, String UserName) {
            if (CmdSpilt.equals(UserName)) {
                return true;
            }

            else{
                return false;
            }
    }

    // Sort all players in ascending alphabetical order.

    public void SortPlayer(){

        NimPlayer Backup;

        for (int i=0;i<Player.length-1;i++){
            Backup = Player[i];
            for(int j=i+1;j<Player.length;j++){
                if(Player[j]!=null && Player[j].getUserName().compareTo(Player[i].getUserName())<0){
                        Player[i] = Player[j];
                        Player[j] = Backup;

                }
            }
        }
    }

    // Save players' data to 'players.dat'.

    public void Save(){
        String[] DataOut = new String[MAX_PLAYER];
        for (int i=0;i<Player.length;i++){
            if(Player[i]!=null) {
                DataOut[i] = Player[i].getUserName() + ","
                        + Player[i].getFamilyName() + ","
                        + Player[i].getGivenName() + ","
                        + Player[i].AI() + ","
                        + Player[i].getGame() + ","
                        + Player[i].getWin();
            }
        }

        try{
            FileOutputStream OutF = new FileOutputStream("players.dat");
            ObjectOutputStream OutO = new ObjectOutputStream(OutF);
            OutO.writeObject(DataOut);
        }
        catch (IOException e){

        }

    }

    // Load players' data from 'players.dat'

    public void Load(){
        String[] DataIn = new String[MAX_PLAYER];
        try {
            FileInputStream InF = new FileInputStream("players.dat");
            ObjectInputStream InO = new ObjectInputStream(InF);
            DataIn = (String[]) InO.readObject();
        }
        catch (FileNotFoundException e){

        }
        catch (ClassNotFoundException e){

        }
        catch (IOException e){

        }
        for(int i=0;i<MAX_PLAYER;i++){
            if (DataIn[i]!=null){
                String[] DataInSplit = DataIn[i].split(",");
                if(DataInSplit[3].equals("false")) {
                    Player[i] = new NimHumanPlayer(DataInSplit[0], DataInSplit[1], DataInSplit[2],false);
                    Player[i].setGame(Integer.parseInt(DataInSplit[4]));
                    Player[i].setWin(Integer.parseInt(DataInSplit[5]));

                }
                else if (DataInSplit[3].equals("true")){
                    Player[i] = new NimAIPlayer(DataInSplit[0], DataInSplit[1], DataInSplit[2], true);
                    Player[i].setGame(Integer.parseInt(DataInSplit[4]));
                    Player[i].setWin(Integer.parseInt(DataInSplit[5]));
                }

            }

        }


    }

    // Judge various input instructions and execute them.

    public void RunGame(){

        Load();

        while (true){

            System.out.println();
            System.out.print("$");

            String Cmd = kb.nextLine();
            String[] CmdSplit = Cmd.split(" |,");

            try {

                if (CmdSplit[0].equals("addplayer")) {
                    try {

                        for (int i = 0; i < MAX_PLAYER; i++) {
                            if (Player[i] == null) {
                                Player[i] = new NimHumanPlayer(CmdSplit[1], CmdSplit[2], CmdSplit[3], false);

                                break;

                            } else if (IsExist(CmdSplit[1], Player[i].getUserName())) {
                                System.out.println("The player already exists.");

                                break;
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Incorrect number of arguments supplied to command.");
                    }
                }

                else if (CmdSplit[0].equals("addaiplayer")){
                    try {

                        for (int i = 0; i < MAX_PLAYER; i++) {
                            if (Player[i] == null) {
                                Player[i] = new NimAIPlayer(CmdSplit[1], CmdSplit[2], CmdSplit[3], true);

                                break;

                            } else if (IsExist(CmdSplit[1], Player[i].getUserName())) {
                                System.out.println("The player already exists.");

                                break;
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Incorrect number of arguments supplied to command.");
                    }
                }

                else if(CmdSplit[0].equals("removeplayer")){
                    if(CmdSplit.length==1){
                        System.out.println("Are you sure you want to remove all players? (y/n)");
                        String Option = kb.nextLine();
                        if(Option.equals("y")){
                            for(int i=0;i<Player.length;i++){
                                Player[i] = null;
                            }

                        }

                    }
                    else {
                        for(int i=0;i<=Player.length;i++){
                            if(i==Player.length){
                                System.out.println("The player does not exist");
                            }
                            else if(Player[i]!=null && IsExist(CmdSplit[1],Player[i].getUserName())){
                                Player[i] = null;

                                break;
                            }
                        }
                    }
                }

                else if(CmdSplit[0].equals("editplayer")){
                    try {
                        for (int i = 0; i <= Player.length; i++) {
                            if (i == Player.length) {
                                System.out.println("The player does not exist");
                            } else if (Player[i] != null && IsExist(CmdSplit[1], Player[i].getUserName())) {
                                if (Player[i].IsAI) {
                                    Player[i] = new NimAIPlayer(CmdSplit[1], CmdSplit[2], CmdSplit[3], true);

                                    break;
                                } else {
                                    Player[i] = new NimHumanPlayer(CmdSplit[1], CmdSplit[2], CmdSplit[3], false);

                                    break;
                                }
                            }
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        System.err.println("Incorrect number of arguments supplied to command.");
                    }
                }

                else if (CmdSplit[0].equals("resetstats")){
                    if (CmdSplit.length==1){
                        System.out.println("Are you sure you want to reset all player statistics? (y/n)");
                        String Option = kb.nextLine();
                        if(Option.equals("y")){
                            for(int i=0;i<Player.length;i++){
                                Player[i].setGame(0);
                                Player[i].setWin(0);
                            }
                        }
                        else {
                            for(int i=0;i<=Player.length;i++){
                                if(i==Player.length){
                                    System.out.println("The player does not exist.");
                                }
                                else if(Player[i]!=null && IsExist(CmdSplit[i],Player[i].getUserName())){
                                    Player[i].setGame(0);
                                    Player[i].setWin(0);

                                    break;
                                }
                            }
                        }
                    }
                }

                else if (CmdSplit[0].equals("displayplayer")){
                    SortPlayer();
                    if(CmdSplit.length==1){
                        for(int i=0;i<Player.length;i++){
                            if (Player[i]!=null) {
                                System.out.println(Player[i].getUserName() + ","
                                        + Player[i].getGivenName() + ","
                                        + Player[i].getFamilyName() + ","
                                        + Player[i].getGame() + " " + "games" + ","
                                        + Player[i].getWin() + " " + "wins");
                            }
                        }
                    }
                    else {
                        for (int i=0;i<=Player.length;i++){
                            if(i==Player.length){
                                System.out.println("The player does not exist.");
                            }

                            else if(Player[i]!=null && IsExist(CmdSplit[1],Player[i].getUserName())){
                                System.out.println(Player[i].getUserName() + ","
                                        + Player[i].getGivenName() + ","
                                        + Player[i].getFamilyName() + ","
                                        + Player[i].getGame() + " " + "games" + ","
                                        + Player[i].getWin() + " " + "wins");

                                break;
                            }
                        }
                    }
                }

                else if (CmdSplit[0].equals("rankings")){
                    SortPlayer();

                    NimPlayer Backup;

                    if(CmdSplit.length==1 || CmdSplit[1].equals("desc")) {
                        for(int i = 0; i < Player.length - 1; i++){
                            Backup = Player[i];
                            for(int j = i + 1; j < Player.length;j++){
                                if(Player[j]!=null && (Player[j].WinRatio()-Player[i].WinRatio())>0){
                                    Player[i] = Player[j];
                                    Player[j] = Backup;
                                }
                            }
                        }
                    }

                    else if(CmdSplit[1].equals("asc")) {

                        for (int i = 0; i < Player.length - 1; i++) {
                            Backup = Player[i];
                            for (int j = i + 1; j < Player.length; j++) {
                                if (Player[j] != null && (Player[j].WinRatio()-Player[i].WinRatio())<0) {
                                    Player[i] = Player[j];
                                    Player[j] = Backup;
                                }
                                }
                            }
                        }

                    for(int i=0;i<10;i++){
                        if(Player[i]!=null){
                            int Present = (int) Player[i].WinRatio()*100;
                            System.out.println(String.format("%-5s",Present+"%")
                                    + "| " + String.format("%02d", Player[i].getGame()) + " games"
                                    + " | " + Player[i].getGivenName()
                                    + " " + Player[i].getFamilyName());
                        }
                    }
                    }

                else if (CmdSplit[0].equals("startgame")){
                    int Mark1 = 0;
                    int Mark2 = 0;
                    int Count = 0;
                    try {
                        for (int i = 0; i < Player.length; i++) {
                            if (Player[i]!=null && IsExist(CmdSplit[3], Player[i].getUserName())) {
                                Mark1 = i;
                                Count++;
                            }
                            else if(Player[i]!=null && IsExist(CmdSplit[4], Player[i].getUserName())){
                                Mark2 = i;
                                Count++;
                            }
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        System.err.println("Incorrect number of arguments supplied to command.");
                    }

                    if(Count==2){

                        InitialStone = Integer.parseInt(CmdSplit[1]);
                        MaximumMove = Integer.parseInt(CmdSplit[2]);
                        Player1GName = Player[Mark1].getGivenName();
                        Player1FName = Player[Mark1].getFamilyName();
                        Player2GName = Player[Mark2].getGivenName();
                        Player2FName = Player[Mark2].getFamilyName();
                        StoneLeft = InitialStone;

                        System.out.println();
                        System.out.println("Initial stone count: " + CmdSplit[1]);
                        System.out.println("Maximum stone removal: " + CmdSplit[2]);
                        System.out.println("Player 1: " + Player1GName + " " + Player1FName);
                        System.out.println("Player 2: " + Player2GName + " " + Player2FName);
                        System.out.println();

                        Play(Mark1, Mark2);

                    }
                    else {
                        System.out.println("One of the players does not exist.");
                    }

                }

                else if (CmdSplit[0].equals("exit"))
                {
                    Save();
                    System.out.println();
                    System.exit(0);
                }


                else {
                     throw new NoSuchCommandException();

                }
            }
            catch (NoSuchCommandException e){
                System.err.println(e.getMessage());
            }
                }
            }

        }













