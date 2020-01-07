
import protocol.parser.ProtocolParser;
import protocol.types.ProtocolArray;
import protocol.types.ProtocolObject;
import protocol.types.ProtocolValue;
import protocol.types.TypeValue;

public class Main {
	public static void main(String[] args) throws Exception
	{
		ProtocolValue<?> parser = ProtocolParser.protocolParse("{Status-online:false}");
		
		if (parser.getTypeValue() == TypeValue.TYPE_OBJECT) {
			ProtocolObject rootObject = (ProtocolObject) parser.getValue();
			System.out.println(rootObject.getElement("Status-online").getValue());
		}
	}
}