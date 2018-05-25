
/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 15, 2005
 */
public class SeapineBuildReleaseToolManualLabel extends SeapineBuildReleaseTool {
  /**  Constructor for the SeapineBuildReleaseToolManualLabel object */
  public SeapineBuildReleaseToolManualLabel() {
    super();
  }


  /**
   *  Gets the getBuildMode() attribute of the
   *  SeapineBuildReleaseToolManualLabel object
   *
   *@return    The getBuildMode() value
   */
  int getBuildMode() {
    System.out.println("label");
    return BUILDMODE_LABELBASED;
  }

}

