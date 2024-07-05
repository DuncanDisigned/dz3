package exceptions;

public class AnimalNotSupported extends Exception {
    public AnimalNotSupported() {
        super("Animal is not supported. Please enter a valid animal type.");
    }

}
