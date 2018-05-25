public class ProjectGroup extends Project implements DefVar {

	public ProjectGroup(XmlTag xt, SeapineStat zst, String ... s_projectnames) {
		super(xt,zst);
		isGroupOfProjects = true;
		cl.add(new XmlTag("isGroupOfProjects",null));
		for (int i=0;i<s_projectnames.length;i++) {
			cl.add(new XmlTag("subproject",s_projectnames[i]));
		}
	}

}
