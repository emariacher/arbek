//import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 10, 2004
 */
public class ChildList extends ArrayList<XmlTag> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean explored;
	Log L             = new Log();



	/**  Constructor for the ChildList object */
	ChildList() { }

	/**
	 *  Description of the Method
	 *
	 *@param  tagName         Description of the Parameter
	 *@param  attributeName   Description of the Parameter
	 *@param  attributeValue  Description of the Parameter
	 *@return                 Description of the Return Value
	 */
	XmlTag find(String tagName, String attributeName, String attributeValue) {
		Iterator<XmlTag> i = iterator();
		while(i.hasNext()){
			XmlTag xt = i.next();
			if(xt.find(tagName, attributeName, attributeValue) != null) {
				return xt.find(tagName, attributeName, attributeValue);
			}
		}
		return null;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  tagName   Description of the Parameter
	 *@param  tagValue  Description of the Parameter
	 *@return           Description of the Return Value
	 */
	XmlTag find(String tagName, String tagValue) {
		Iterator<XmlTag> i = iterator();
		while(i.hasNext()){
			XmlTag xt = i.next();
			if(xt.find(tagName, tagValue) != null) {
				return xt.find(tagName, tagValue);
			}
		}
		return null;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  xt_test  Description of the Parameter
	 *@return          Description of the Return Value
	 */
	XmlTag find(XmlTag xt_test) {
		Iterator<XmlTag> i = iterator();
		while(i.hasNext()){
			XmlTag xt = i.next();
			if(xt.find(xt_test) != null) {
				return xt.find(xt_test);
			}
		}
		return null;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  zeTagName       Description of the Parameter
	 *@param  zeTestTagName   Description of the Parameter
	 *@param  zeTestTagValue  Description of the Parameter
	 *@return                 Description of the Return Value
	 */
	XmlTag find2(String zeTagName, String zeTestTagName, String zeTestTagValue) {
		Iterator<XmlTag> i = iterator();
		while(i.hasNext()){
			XmlTag xt = i.next();
			if(xt.find2(zeTagName, zeTestTagName, zeTestTagValue) != null) {
				return xt.find2(zeTagName, zeTestTagName, zeTestTagValue);
			}
		}
		return null;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  tagName  Description of the Parameter
	 *@return          Description of the Return Value
	 */
	XmlTag find(String tagName) {
		Iterator<XmlTag> i = iterator();
		while(i.hasNext()){
			XmlTag xt = i.next();
			if(xt.find(tagName) != null) {
				return xt.find(tagName);
			}
		}
		return null;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  zeTagName  Description of the Parameter
	 *@param  found      Description of the Parameter
	 */
	void findAll(String zeTagName, ChildList found) {
		Iterator<XmlTag> i = iterator();
		while(i.hasNext()){
			XmlTag xt = i.next();
			xt.findAll(zeTagName, found);
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  zeTagName       Description of the Parameter
	 *@param  zeTestTagName   Description of the Parameter
	 *@param  zeTestTagValue  Description of the Parameter
	 *@param  found           Description of the Parameter
	 */
	void find2All(String zeTagName, String zeTestTagName, String zeTestTagValue, ChildList found) {
		Iterator<XmlTag> i = iterator();
		while(i.hasNext()){
			XmlTag xt = i.next();
			xt.find2All(zeTagName, zeTestTagName, zeTestTagValue, found);
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String toString() {
		StringBuffer sb  = new StringBuffer();
		Iterator<XmlTag> i = iterator();
		while(i.hasNext()){
			XmlTag xt = i.next();
			sb.append(xt.toString());
		}
		return sb.toString();
	}

}

