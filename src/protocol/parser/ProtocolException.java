package protocol.parser;

@SuppressWarnings("serial")
public class ProtocolException extends Exception
{
	private String message;
	private int index;
	
	public ProtocolException(String message, int index) {
		this.message = message;
		this.index = index;
	}
	
	@Override
	public String getMessage() {
		return message + (index >= 0 ? "\r\n" + "Symbol " + (index + 1) : "");
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
}