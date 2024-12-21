package ara.projet.checkpointing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NodeState {

	private Map<String, Object> map = new HashMap<>();
	
	public void saveVariable(String name, Object value) {
		map.put(name, value);
		
	}

	public Object loadVariable(String name) {
		return map.get(name);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Entry<String,Object> e : map.entrySet()){
			sb.append("( "+e.getKey()+" = "+e.getValue().toString()+") ");
		}
		return sb.toString();
	}

}
