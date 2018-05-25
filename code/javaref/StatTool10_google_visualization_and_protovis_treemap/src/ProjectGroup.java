public class ProjectGroup extends Project implements DefVar {
	
	public ProjectGroup(Project p, String ... s_projectnames) {
		super(p.projectName, p.zst);
		isGroupOfProjects = true;
//		for (int i=0;i<s_projectnames.length;i++) {
//			subproducts.add(new Project(s_projectnames[i], p.zst));
//		}
		for (String s : s_projectnames) {
			subproducts.add(new Project(s, p.zst));
		}
	}

}
