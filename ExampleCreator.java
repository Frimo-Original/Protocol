
import protocol.parser.ProtocolParser;
import protocol.types.ProtocolArray;
import protocol.types.ProtocolObject;
import protocol.types.ProtocolValue;
import protocol.types.TypeValue;

public class Main {
	public static void main(String[] args) throws Exception
	{
		ProtocolObject humanNode = new ProtocolObject();
		
		humanNode.addElement("Name", "name");
		humanNode.addElement("Year", 2000);
		humanNode.addElement("Status-online", true);
		humanNode.addElement("City", "Moscow");
		
		ProtocolArray numbersPhone = new ProtocolArray();
		
		numbersPhone.addElement(646646);
		numbersPhone.addElement(24546);
		numbersPhone.addElement(3654);
		
		humanNode.addElement("Number-phones", numbersPhone);
		
		System.out.println(humanNode.toProtocol());
	}
}