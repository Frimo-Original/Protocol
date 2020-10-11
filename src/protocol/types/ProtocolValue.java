package protocol.types;

public class ProtocolValue<T> implements Protocol
{
	private T value;
	private TypeValue typeValue;
	
	public ProtocolValue(T value, TypeValue typeValue) 
	{
		this.value = value;
		
		if (typeValue != null)
			this.typeValue = typeValue;
		else
			this.typeValue = TypeValue.TYPE_STRING;
	}
	
	public T getValue() {
		return value;
	}
	
	public TypeValue getTypeValue() {
		return typeValue;
	}

	@Override
	public String toProtocol()
	{
		String temp = "";
		
		switch (typeValue) {
		case TYPE_STRING:
			String strValue = ((String) value);
			
			for (int i = 0; i < strValue.length(); i++)
				switch (strValue.charAt(i))
				{
				case '"':
					temp += "\\\"";
					break;
				case '\\':
					temp += "\\\\";
					break;
				case '\t':
					temp += "\\t";
					break;
				case '\n':
					temp += "\\n";
					break;
				case '\r':
					temp += "\\r";
					break;
				default:
					temp += strValue.charAt(i);
				}
			
			return "\"" + temp + "\"";
			
		case TYPE_INTEGER:
			return ((Integer) value).toString();
			
		case TYPE_DOUBLE:
			return ((Double) value).toString();
			
		case TYPE_BOOLEAN:
			if ((Boolean) value == true)
				return "true";
			
			return "false";
			
		case TYPE_OBJECT:
			return ((ProtocolObject) value).toProtocol();
			
		case TYPE_ARRAY:
			return ((ProtocolArray) value).toProtocol();
			
		default:
			return "";
		}
	}
}