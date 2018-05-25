import org.keplerproject.luajava.*;
/**
 *  Description of the Class
 *
 *@author     emariach
 *@created    May 19, 2008
 */
public class luajava1 {
	/**
	 *  The main program for the luajava1 class
	 *
	 */
	public luajava1() { }


	/**
	 *  The main program for the luajava1 class
	 *
	 *@param  args  The command line arguments
	 */
	public static void main(String[] args) {
		System.out.println("coucou1");
		LuaState l = LuaStateFactory.newLuaState();
		System.out.println("coucou2");
		l.LdoFile("main.lua");
		TestDataObject obj = new TestDataObject("test", 1, true);
		System.out.println("before lua: " + obj.toString());
		l.getGlobal("test");
		l.pushJavaObject(obj);
		l.call(1, 0);
		System.out.println("after lua: " + obj.toString());
	}
}

