package protocol.types;

import java.util.ArrayList;

public class ProtocolArray extends Protocol
{
	private ArrayList<ProtocolValue<?>> elements = new ArrayList<>();
	
	public void addElement(Integer value) {
		elements.add(new ProtocolValue<Integer>(value, TypeValue.TYPE_INTEGER));
	}
	
	public void addElement(String value) {
		elements.add(new ProtocolValue<String>(value, TypeValue.TYPE_STRING));
	}
	
	public void addElement(Double value) {
		elements.add(new ProtocolValue<Double>(value, TypeValue.TYPE_DOUBLE));
	}
	
	public void addElement(Boolean value) {
		elements.add(new ProtocolValue<Boolean>(value, TypeValue.TYPE_BOOLEAN));
	}
	
	public void addElement(ProtocolObject value) {
		if (value != null)
			elements.add(new ProtocolValue<ProtocolObject>(value, TypeValue.TYPE_OBJECT));
	}
	
	public void addElement(ProtocolArray value) {
		if (value != null)
			elements.add(new ProtocolValue<ProtocolArray>(value, TypeValue.TYPE_ARRAY));
	}
	
	public ProtocolValue<?> getElement(int index) {
		if (index >= 0 && index < elements.size())
			return elements.get(index);
		
		return null;
	}
	
	public ProtocolValue<?>[] getArray() {
		return elements.toArray(new ProtocolValue<?>[elements.size()]);
	}

	@Override
	public String toProtocol() {
		String content = "";
		
		for (ProtocolValue<?> i : getArray())
			content += " " + i.toProtocol();
		
		return "[" + content.trim() + "]";
	}
}