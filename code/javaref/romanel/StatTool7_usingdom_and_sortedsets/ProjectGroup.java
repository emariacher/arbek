import java.util.SortedSet;
import java.util.TreeSet;

public class ProjectGroup extends Project implements DefVar {
	
	SortedSet<Project> subproducts = new TreeSet<Project>(new Project.compareName());
	
	public ProjectGroup(Project p, String ... s_projectnames) {
		super(p.projectName, p.zst);
		isGroupOfProjects = true;
		for (int i=0;i<s_projectnames.length;i++) {
			subproducts.add(new Project(s_projectnames[i], p.zst));
		}
	}

}
