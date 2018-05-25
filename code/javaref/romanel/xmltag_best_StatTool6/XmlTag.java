//import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 8, 2004
 */
public class XmlTag implements Comparable<XmlTag> {
	List<XmlAttribute> a              = new ArrayList<XmlAttribute>();
	String tagName;
	String noTag          = null;
	ChildList cl          = new ChildList();
	XmlTag parent         = null;
	boolean foundflag     = false;
	StringBuffer sbtrace;


	/**
	 *  Constructor for the XmlTag object
	 *
	 *@param  parent  Description of the Parameter
	 */
	XmlTag(XmlTag parent) {
		this.parent = parent;
	}


	/**  Constructor for the XmlTag object */
	XmlTag() { }


	/**
	 *  Constructor for the XmlTag object
	 *
	 *@param  zeTagName   Description of the Parameter
	 *@param  zeTagValue  Description of the Parameter
	 */
	XmlTag(String zeTagName, String zeTagValue) {
		tagName = new String(zeTagName);
		if(zeTagValue != null) {
			noTag = new String(zeTagValue.replaceAll("&", "&amp;"));
		}
	}


	/**
	 *  Constructor for the XmlTag object
	 *
	 *@param  xt  Description of the Parameter
	 */
	void XmlTag2(XmlTag xt) {
		a = xt.a;
		tagName = xt.tagName;
		noTag = xt.noTag;
		cl = xt.cl;
		parent = xt.parent;
	}


	/**
	 *  Gets the attributeValue attribute of the XmlTag object
	 *
	 *@param  s  Description of the Parameter
	 *@return    The attributeValue value
	 */
	String getAttributeValue(String s) {
		if(a.size() > 0) {
			Iterator<XmlAttribute> i = a.iterator();
			while(i.hasNext()){
				XmlAttribute xa = i.next();
				String value  = xa.getValue(s);
				if(value != null) {
					return new String(value);
				}
			}
			return new String("");
		} else {
			return new String("");
		}
	}


	/**
	 *  Sets the attributeValue attribute of the XmlTag object
	 *
	 *@param  s      The new attributeValue value
	 *@param  v      The new attributeValue value
	 *@param  force  The new attributeValue value
	 *@return        Description of the Return Value
	 */
	boolean setAttributeValue(String s, String v, boolean force) {
		if(a.size() > 0) {
			Iterator<XmlAttribute> i = a.iterator();
			while(i.hasNext()){
				XmlAttribute xa = i.next();
				String value     = xa.getValue(s);
				if(value != null) {
					xa.setValue(new String(v));
					return true;
				}
			}
		}
		if(force) {
			addAttribute(new String(s), new String(v));
		}
		return false;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  s  Description of the Parameter
	 *@return    Description of the Return Value
	 */
	boolean removeAttribute(String s) {
		if(a.size() > 0) {
			Iterator<XmlAttribute> i = a.iterator();
			while(i.hasNext()){
				XmlAttribute xa = i.next();
				String value     = xa.getValue(s);
				if(value != null) {
					a.remove(i);
					return true;
				}
			}
		}
		return false;
	}


	/**
	 *  Adds a feature to the Cdata attribute of the XmlTag object
	 *
	 *@param  s  The feature to be added to the Cdata attribute
	 */
	void addCdata(String s) { }


	/**
	 *  Adds a feature to the DocType attribute of the XmlTag object
	 *
	 *@param  s  The feature to be added to the DocType attribute
	 */
	void addDocType(String s) { }


	/**
	 *  Adds a feature to the XmlCom attribute of the XmlTag object
	 *
	 *@param  s  The feature to be added to the XmlCom attribute
	 */
	void addXmlCom(String s) { }


	/**
	 *  Adds a feature to the Notag attribute of the XmlTag object
	 *
	 *@param  s  The feature to be added to the Notag attribute
	 */
	void addNoTag(String s) {
		if(noTag == null) {
			noTag = new String(s.replaceAll("&", "&amp;"));
			System.out.println(noTag);
		} else {
			noTag = new String(noTag + "\n" + s.replaceAll("&", "&amp;"));
			System.out.println(noTag);
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  s  Description of the Parameter
	 */
	void name(String s) {
		tagName = new String(s.toLowerCase());
	}


	/**
	 *  Adds a feature to the Child attribute of the XmlTag object
	 *
	 *@param  xt  The feature to be added to the Child attribute
	 */
	void addChild(XmlTag xt) {
		//System.out.println("[" + xt.toString() + "]\n");
		if(xt.parent == null) {
			xt.parent = this;
		}
		cl.add(xt);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  attributeName   Description of the Parameter
	 *@param  attributeValue  Description of the Parameter
	 *@return                 Description of the Return Value
	 */
	boolean match(String attributeName, String attributeValue) {
		if(attributeValue.equals(getAttributeValue(attributeName))) {
			return true;
		} else {
			return false;
		}
	}



	/**
	 *  Adds a feature to the Attribute attribute of the XmlTag object
	 *
	 *@param  attr  The feature to be added to the Attribute attribute
	 */
	void addAttribute(String name, String value) {
		XmlAttribute attr = new XmlAttribute(name, value);
		int i  = a.indexOf(attr);
		if(i < 0) {
			a.add(attr);
		} else {
			XmlAttribute xa  = a.get(i);
			xa.value = new String(attr.getValue());
		}
	}


	/**
	 *  Adds a feature to the Attribute2 attribute of the XmlTag object
	 *
	 *@param  attr  The feature to be added to the Attribute2 attribute
	 */
	void addAttribute2(XmlAttribute attr) {
		a.add(attr);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  zeTagName       Description of the Parameter
	 *@param  attributeName   Description of the Parameter
	 *@param  attributeValue  Description of the Parameter
	 *@return                 Description of the Return Value
	 */
	XmlTag find(String zeTagName, String attributeName, String attributeValue) {
		if(is(zeTagName) && (match(attributeName, attributeValue))) {
			return this;
		}
		if(cl.find(zeTagName, attributeName, attributeValue) != null) {
			return cl.find(zeTagName, attributeName, attributeValue);
		}
		return null;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  zeTagName   Description of the Parameter
	 *@param  zeTagValue  Description of the Parameter
	 *@return             Description of the Return Value
	 */
	XmlTag find(String zeTagName, String zeTagValue) {
		if(is(zeTagName, zeTagValue)) {
			return this;
		}
		if(cl.find(zeTagName, zeTagValue) != null) {
			return cl.find(zeTagName, zeTagValue);
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
		if(is(zeTagName)) {
			if(find(zeTestTagName, zeTestTagValue) != null) {
				return this;
			}
		}
		if(cl.find(zeTagName) != null) {
			return cl.find2(zeTagName, zeTestTagName, zeTestTagValue);
		}
		return null;
	}



	/**
	 *  Description of the Method
	 *
	 *@param  zeTagName  Description of the Parameter
	 *@return            Description of the Return Value
	 */
	XmlTag find(String zeTagName) {
		if(is(zeTagName)) {
			return this;
		}
		if(cl.find(zeTagName) != null) {
			return cl.find(zeTagName);
		}
		return null;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  xt  Description of the Parameter
	 *@return     Description of the Return Value
	 */
	XmlTag find(XmlTag xt) {
		if(is(xt)) {
			return this;
		}
		if(tagName.equals(xt.tagName)) {
			return null; //tagname matches but not value and hierarchy below -> no need to go below
		}
		if(cl.find(xt) != null) {
			return cl.find(xt);
		}
		return null;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  xt  Description of the Parameter
	 *@return     Description of the Return Value
	 */
	boolean is(XmlTag xt) {
		if(tagName.equals(xt.tagName)) {
			return toString().equals(xt.toString());
		}
		return false;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  zeTagName  Description of the Parameter
	 *@return            Description of the Return Value
	 */
	boolean is(String zeTagName) {
		return zeTagName.equals(tagName);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  zeTagName   Description of the Parameter
	 *@param  zeTagValue  Description of the Parameter
	 *@return             Description of the Return Value
	 */
	boolean is(String zeTagName, String zeTagValue) {
		return (is(zeTagName) && zeTagValue.equals(noTag));
	}


	/**
	 *  Description of the Method
	 *
	 *@param  zeTagName  Description of the Parameter
	 *@param  found      Description of the Parameter
	 */
	void findAll(String zeTagName, ChildList found) {
		if(is(zeTagName)) {
			if(found == null) { //reset foundflag
				foundflag = false;
			} else if(!foundflag) {
				found.add(this);
				foundflag = true;
			}
		}
		cl.findAll(zeTagName, found);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  zeTagName      Description of the Parameter
	 *@param  zeTestTagName  Description of the Parameter
	 *@param  cl_in          Description of the Parameter
	 *@param  found          Description of the Parameter
	 */
	void find2All(String zeTagName, String zeTestTagName, ChildList cl_in, ChildList found) {
		for(int i = 0; i < cl_in.size(); i++) {
			find2All(zeTagName, zeTestTagName, ((XmlTag)cl_in.get(i)).noTag, found);
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
		if(is(zeTagName)) {
			if(find(zeTestTagName, zeTestTagValue) != null) {
				if(found == null) { //reset foundflag
					foundflag = false;
				} else if(!foundflag) {
					found.add(this);
					foundflag = true;
				}
			}
		}
		if(cl.find(zeTagName) != null) {
			cl.find2All(zeTagName, zeTestTagName, zeTestTagValue, found);
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	String endTag() {
		return new String("</" + tagName + ">");
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	String beginTag() {
		StringBuffer sb  = new StringBuffer("<" + tagName);
		Iterator<XmlAttribute> i = a.iterator();
		while(i.hasNext()){
			XmlAttribute xa = i.next();
			sb.append(" " + xa.toString());
		}
		sb.append(">");
		return sb.toString();
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String toString() {
		StringBuffer sb  = new StringBuffer();
		if(tagName != null) {
			sb.append(beginTag());
		}
		if(noTag != null) {
			sb.append(noTag);
			//System.out.println("[" + noTag + "]");
		}
		if(cl.size() > 0) {
			sb.append(cl.toString());
		}
		if(tagName != null) {
			sb.append(endTag() + "\n");
		}
		return sb.toString();
	}


	/**
	 *  Gets the root attribute of the XmlTag object
	 *
	 *@param  zeTagName  Description of the Parameter
	 *@return            The root value
	 */
	XmlTag getParent(String zeTagName) {
		XmlTag xt  = this;
		while((!xt.is(zeTagName)) && (xt.parent != null)) {
			xt = xt.parent;
		}
		if(xt.parent == null) {
			return null;
		} else {
			return xt;
		}
	}


	/**
	 *  Gets the root attribute of the XmlTag object
	 *
	 *@return    The root value
	 */
	XmlTag getRoot() {
		XmlTag xt  = this;
		while(xt.parent != null) {
			xt = xt.parent;
		}
		return xt;
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	String toString4Compare() {
		return new String(tagName + "_" + noTag);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  zeTestTagName  Description of the Parameter
	 *@return                Description of the Return Value
	 */
	ChildList report(String zeTestTagName) {
		ChildList cl_in   = new ChildList();
		ChildList cl_out  = new ChildList();
		sbtrace = new StringBuffer();
		findAll(zeTestTagName, cl_in);
		sbtrace.append("\n" + zeTestTagName + " ");
		for(int i = 0; i < cl_in.size(); i++) {
			XmlTag xt  = (XmlTag)cl_in.get(i);
			if(cl_out.find(xt.tagName, xt.noTag) == null) {
				cl_out.add(xt);
				sbtrace.append(", " + xt.noTag);
			}
		}
		sbtrace.append("\nAvant:");
		for(int i = 0; i < cl_out.size(); i++) {
			XmlTag xt  = (XmlTag)cl_out.get(i);
			sbtrace.append(", " + xt.noTag);
		}
		//QuickSort qs      = new QuickSort();
		//QuickSort.sort(cl_out.childs);
		Collections.sort(cl_out);
		sbtrace.append("\nApres:");
		for(int i = 0; i < cl_out.size(); i++) {
			XmlTag xt  = (XmlTag)cl_out.get(i);
			sbtrace.append(", " + xt.noTag);
		}
		sbtrace.append("\n  ...\n");
		return cl_out;
	}


	@Override
	public int compareTo(XmlTag xt) {
		if(toString4Compare().compareTo(xt.toString4Compare()) < 0) {
			return -1;
		} else if(toString4Compare().compareTo(xt.toString4Compare()) > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	private class XmlAttribute {
		String name   = null;
		String value  = null;


		/**  Constructor for the XmlAttribute object */
		XmlAttribute() { }


		/**
		 *  Constructor for the XmlAttribute object
		 *
		 *@param  name   Description of the Parameter
		 *@param  value  Description of the Parameter
		 */
		XmlAttribute(String name, String value) {
			this.name = new String(name);
			this.value = new String(value);
		}


		/**
		 *  Description of the Method
		 *
		 *@param  obj  Description of the Parameter
		 *@return      Description of the Return Value
		 */
		public boolean equals(Object obj) {
			XmlAttribute xa  = (XmlAttribute)obj;
			return (name.equals(xa.name) && value.equals(xa.value));
		}


		/**
		 *  Description of the Method
		 *
		 *@param  s  Description of the Parameter
		 *@return    Description of the Return Value
		 */
		String unquote(String s) {
			return s.replaceAll("\"", "");
		}


		/**
		 *  Description of the Method
		 *
		 *@return    Description of the Return Value
		 */
		public String toString() {
			return new String(name + "=\"" + value + "\"");
		}


		/**
		 *  Gets the value attribute of the XmlAttribute object
		 *
		 *@param  s  Description of the Parameter
		 *@return    The value value
		 */
		String getValue(String s) {
			if(s.equals(name)) {
				return new String(value);
			} else {
				return null;
			}
		}


		/**
		 *  Sets the value attribute of the XmlAttribute object
		 *
		 *@param  s  The new value value
		 */
		void setValue(String s) {
			value = new String(s);
		}


		/**
		 *  Gets the value attribute of the XmlAttribute object
		 *
		 *@return    The value value
		 */
		String getValue() {
			return new String(value);
		}
	}

}

