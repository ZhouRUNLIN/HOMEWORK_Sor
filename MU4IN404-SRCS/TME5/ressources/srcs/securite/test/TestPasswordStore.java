package srcs.securite.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static srcs.securite.test.ChannelTestUtil.ALGODIGEST;
import static srcs.securite.test.ChannelTestUtil.getFields;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import srcs.securite.PasswordStore;

public class TestPasswordStore {

	@Test
	public void test() throws Exception {
		
		Class<PasswordStore> cl = PasswordStore.class;
		List<Field> fields =  getFields(cl, Map.class);
		assertEquals(1, fields.size());
		Field f_map = fields.get(0);
		f_map.setAccessible(true);
		
		
		PasswordStore ps = new PasswordStore(ALGODIGEST);
		Map<?,?> map_obj = (Map<?,?>) f_map.get(ps);
		
		assertTrue(map_obj.isEmpty());
		
		
		String user1="user1";
		String pwdusr1 = "ze4f:!df";
		
		
		ps.storePassword(user1, pwdusr1);
		assertEquals(1,map_obj.size());
		
		assertNotEquals(pwdusr1,map_obj.get(user1));
		assertNotEquals(pwdusr1.getBytes(),map_obj.get(user1));
		assertTrue(ps.checkPassword(user1, pwdusr1));
		
		
		ps.storePassword(user1, pwdusr1);
		assertEquals(1,map_obj.size());
		
		
		
		String user2="user2";
		String pwdusr2 = "dd!f*s";
		ps.storePassword(user2, pwdusr2);
		assertEquals(2,map_obj.size());
		assertNotEquals(pwdusr2,map_obj.get(user2));
		assertNotEquals(pwdusr2.getBytes(),map_obj.get(user2));
		
		assertTrue(ps.checkPassword(user2, pwdusr2));
		assertFalse(ps.checkPassword(user2, pwdusr1));
		assertFalse(ps.checkPassword(user1, pwdusr2));
		
	}
	
	

}
