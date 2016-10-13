package robert.exception;

public class NoSuchRoomException extends Exception {

	public NoSuchRoomException(String building, String roomNumber) {
		super("No room " + roomNumber + " in building " + building + "has been found.");
	}
}
