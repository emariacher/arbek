
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils {

	static String getTextValue(Element el, String tagName) {
		String textVal = null;
		NodeList nl = el.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element ele = (Element)nl.item(0);
			Node firstChild= ele.getFirstChild();
			if(firstChild!=null) {
				textVal = ele.getFirstChild().getNodeValue();
			}		
		}
		return textVal;
	}

	static ArrayList<Element> getElementsByTagName(Element el, String regex) {
		ArrayList<Element> l_elements = new ArrayList<Element>();
		NodeList nl = el.getElementsByTagName("*");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element ele = (Element)nl.item(i);
				String tagName=ele.getTagName();
				if(Pattern.matches(regex, tagName)) {
					l_elements.add(ele);
				}
			}
		}
		return l_elements;
	}

}

