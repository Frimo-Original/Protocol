package protocol.parser;

import protocol.types.ProtocolArray;
import protocol.types.ProtocolObject;
import protocol.types.ProtocolValue;
import protocol.types.TypeValue;

public class ProtocolParser {
	private int index = 1;
	private char[] arrayProtocol;
	
	private ProtocolParser() { }
	
	public static ProtocolValue<?> protocolParse(String strProtocol) throws ProtocolException
	{
		ProtocolParser parser = new ProtocolParser();
		parser.arrayProtocol = strProtocol.trim().toCharArray();
		
		if (parser.arrayProtocol.length >= 1) {
			if (parser.arrayProtocol[0] == '{')
				return new ProtocolValue<ProtocolObject>(parser.getProtocolObject(), TypeValue.TYPE_OBJECT);
			else if (parser.arrayProtocol[0] == '[')
				return new ProtocolValue<ProtocolArray>(parser.getProtocolArray(), TypeValue.TYPE_ARRAY);
			else
				throw new ProtocolException("Unkown element " + new String(parser.arrayProtocol), 0);
		}
		else
			throw new ProtocolException("Incorrect protocol", -1);
	}
	
	@SuppressWarnings("unchecked")
	ProtocolObject getProtocolObject() throws ProtocolException
	{
		ProtocolObject protocolObject = new ProtocolObject();
		
		for (; index < arrayProtocol.length;)
		{
			if (arrayProtocol[index] == '}') {
				index++;
				return protocolObject;
			}
			else if (checkIsEnd())
				throw new ProtocolException("Incorrect syntax " + new String(arrayProtocol), -1);
			
			else if (checkEmptySpace(arrayProtocol[index]))
				index++;
			
			else {
				String nameElement = getNameElement();
				ProtocolValue<?> value = getValueElement();
				
				switch (value.getTypeValue()) {
				case TYPE_STRING:
					protocolObject.addElement(nameElement, ((ProtocolValue<String>) value).getValue());
					break;
				case TYPE_INTEGER:
					protocolObject.addElement(nameElement, ((ProtocolValue<Integer>) value).getValue());
					break;
				case TYPE_DOUBLE:
					protocolObject.addElement(nameElement, ((ProtocolValue<Double>) value).getValue());
					break;
				case TYPE_BOOLEAN:
					protocolObject.addElement(nameElement, ((ProtocolValue<Boolean>) value).getValue());
					break;
				case TYPE_OBJECT:
					protocolObject.addElement(nameElement, ((ProtocolValue<ProtocolObject>) value).getValue());
					break;
				case TYPE_ARRAY:
					protocolObject.addElement(nameElement, ((ProtocolValue<ProtocolArray>) value).getValue());
					break;
				}
			}
		}
		
		return protocolObject;
	}
	
	@SuppressWarnings("unchecked")
	ProtocolArray getProtocolArray() throws ProtocolException
	{
		ProtocolArray protocolArray = new ProtocolArray();
		
		for (; index < arrayProtocol.length;)
		{
			if (arrayProtocol[index] == ']') {
				index++;
				break;
			}
			else {
				ProtocolValue<?> value = getValueElement();
				
				switch (value.getTypeValue()) {
				case TYPE_STRING:
					protocolArray.addElement(((ProtocolValue<String>) value).getValue());
					break;
				case TYPE_INTEGER:
					protocolArray.addElement(((ProtocolValue<Integer>) value).getValue());
					break;
				case TYPE_DOUBLE:
					protocolArray.addElement(((ProtocolValue<Double>) value).getValue());
					break;
				case TYPE_BOOLEAN:
					protocolArray.addElement(((ProtocolValue<Boolean>) value).getValue());
					break;
				case TYPE_OBJECT:
					protocolArray.addElement(((ProtocolValue<ProtocolObject>) value).getValue());
					break;
				case TYPE_ARRAY:
					protocolArray.addElement(((ProtocolValue<ProtocolArray>) value).getValue());
					break;
				}
			}
		}
		
		return protocolArray;
	}
	
	String getNameElement() throws ProtocolException
	{
		String nameElement = "";
		boolean isEndNameElement = false;
		
		for (; index < arrayProtocol.length; index++)
		{
			if (checkEmptySpace(arrayProtocol[index])) {
				if (!isEndNameElement)
					isEndNameElement = true;
			}
			else if (String.valueOf(arrayProtocol[index]).matches("[a-zA-Z-]")) {
				if (!isEndNameElement)
					nameElement += arrayProtocol[index];
				else
					throw new ProtocolException("Incorrect syntax " + new String(arrayProtocol), index);
			}
			else if (arrayProtocol[index] == ':') {
				index++;
				break;
			}
			else
				throw new ProtocolException("Unkown symbol " + new String(arrayProtocol), index);
			
			if (checkIsEnd())
				throw new ProtocolException("Incorrect syntax " + new String(arrayProtocol), -1);
		}
		
		return nameElement;
	}
	
	ProtocolValue<?> getValueElement() throws ProtocolException
	{
		for (; index < arrayProtocol.length; index++)
		{
			if (checkEmptySpace(arrayProtocol[index]))
				continue;
			
			if (checkIsEnd())
				throw new ProtocolException("Incorrect syntax " + new String(arrayProtocol), -1);
			
			else if (arrayProtocol[index] == '{') {
				index++;
				return new ProtocolValue<ProtocolObject>(getProtocolObject(), TypeValue.TYPE_OBJECT);
			}
			else if (arrayProtocol[index] == '[') {
				index++;
				return new ProtocolValue<ProtocolArray>(getProtocolArray(), TypeValue.TYPE_ARRAY);
			}
			else if (arrayProtocol[index] == '"')
			{
				String value = "";
				boolean isSpecialChar = false;
				
				for (index++; index < arrayProtocol.length; index++) {
					if (checkIsEnd())
						throw new ProtocolException("Incorrect syntax " + new String(arrayProtocol), -1);
					
					if (!isSpecialChar) {
						if (arrayProtocol[index] == '"') {
							index++;
							return new ProtocolValue<String>(value, TypeValue.TYPE_STRING);
						}
						else if (arrayProtocol[index] == '\\')
							isSpecialChar = true;
						else
							value += arrayProtocol[index];
					}
					
					else
						switch (arrayProtocol[index]) {
						case '"':
							value += '"';
							break;
						case '\\':
							value += '\\';
							break;
						case 't':
							value += '\t';
							break;
						case 'r':
							value += '\r';
							break;
						case 'n':
							value += '\n';
							break;
						default:
							throw new ProtocolException("Incorrect literal " + new String(arrayProtocol), index);
						}
				}
			}
			
			else if (String.valueOf(arrayProtocol[index]).matches("[0-9]")) {
				String numberValue = "" + arrayProtocol[index];
				boolean isDouble = false;
				
				for (index++; index < arrayProtocol.length; index++) {
					if (checkIsEnd())
						throw new ProtocolException("Incorrect syntax " + new String(arrayProtocol), -1);
					
					if (String.valueOf(arrayProtocol[index]).matches("[0-9]"))
						numberValue += arrayProtocol[index];
					
					else if (arrayProtocol[index] == '.') {
						if (isDouble)
							throw new ProtocolException("Incorrect syntax " + new String(arrayProtocol), index);
						else {
							isDouble = true;
							numberValue += arrayProtocol[index];
						}
					}
					
					else {
						if (isDouble)
							return new ProtocolValue<Double>(Double.parseDouble(numberValue), TypeValue.TYPE_DOUBLE);
						
						return new ProtocolValue<Integer>(Integer.parseInt(numberValue), TypeValue.TYPE_INTEGER);
					}
				}
			}
			
			else if (String.valueOf(arrayProtocol[index]).matches("[truefals]")) {
				String specialWord = "" + arrayProtocol[index];
				
				for (index++; index < arrayProtocol.length; index++) {
					if (checkIsEnd())
						throw new ProtocolException("Incorrect syntax " + new String(arrayProtocol), -1);
					
					if (String.valueOf(arrayProtocol[index]).matches("[truefals]"))
						specialWord += arrayProtocol[index];
					
					else {
						if (specialWord.equals("true") || specialWord.equals("false"))
							return new ProtocolValue<Boolean>(Boolean.parseBoolean(specialWord), TypeValue.TYPE_BOOLEAN);
						else
							throw new ProtocolException("Incorrect syntax " + new String(arrayProtocol), index);
					}
				}
			}
			else
				throw new ProtocolException("Incorrect syntax " + new String(arrayProtocol), index);
		}
		
		return null;
	}
	
	private boolean checkIsEnd() {
		if (index == arrayProtocol.length)
			return true;
		return false;
	}
	
	private boolean checkEmptySpace(char symbol) {
		if (String.valueOf(symbol).matches("[ \r\n\t]"))
			return true;
		
		return false;
	}
}