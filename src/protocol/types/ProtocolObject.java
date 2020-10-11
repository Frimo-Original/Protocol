package protocol.types;

import java.util.HashMap;
import java.util.Map;

public class ProtocolObject implements Protocol
{
	private HashMap<String, ProtocolValue<?>> elements = new HashMap<String, ProtocolValue<?>>();
	
	public void addElement(String key, Integer value) {
		elements.put(key, new ProtocolValue<Integer>(value, TypeValue.TYPE_INTEGER));
	}
	
	public void addElement(String key, String value) {
		elements.put(key, new ProtocolValue<String>(value, TypeValue.TYPE_STRING));
	}
	
	public void addElement(String key, Boolean value) {
		elements.put(key, new ProtocolValue<Boolean>(value, TypeValue.TYPE_BOOLEAN));
	}
	
	public void addElement(String key, Double value) {
		elements.put(key, new ProtocolValue<Double>(value, TypeValue.TYPE_DOUBLE));
	}
	
	public void addElement(String key, ProtocolObject value) {
		if (value != null)
			elements.put(key, new ProtocolValue<ProtocolObject>(value, TypeValue.TYPE_OBJECT));
	}
	
	public void addElement(String key, ProtocolArray value) {
		if (value != null)
			elements.put(key, new ProtocolValue<ProtocolArray>(value, TypeValue.TYPE_ARRAY));
	}
	
	public ProtocolValue<?> getElement(String key) {
		return elements.get(key);
	}

	@Override
	public String toProtocol() {
		String content = "";
		
		for (Map.Entry<String, ProtocolValue<?>> i : elements.entrySet())
			content += " " + i.getKey() + ":" + i.getValue().toProtocol();
		
		return "{" + content.trim() +  "}";
	}
}