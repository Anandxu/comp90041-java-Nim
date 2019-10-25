public class NoSuchCommandException extends Exception {
    public NoSuchCommandException(){
        super("Invalid command!");
    }

    public NoSuchCommandException(String message){
        super(message);
    }

}
