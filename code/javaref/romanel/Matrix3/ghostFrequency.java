import java.util.Comparator;


public class ghostFrequency {
	String s_triplet;
	int i_occurence;
	int i_totalWordsInFrequencyFile=0;


	public ghostFrequency(String s_triplet, int i_occurence) {
		this.s_triplet = s_triplet;
		this.i_occurence = i_occurence;
	}

	public ghostFrequency(int i_totalWordsInFrequencyFile, float f_threshold) {
		this.i_totalWordsInFrequencyFile= i_totalWordsInFrequencyFile;
		i_occurence=(new Float(i_totalWordsInFrequencyFile*f_threshold)).intValue();
	}

	public static class  compare2 implements Comparator<ghostFrequency> {
		public int compare(ghostFrequency o1, ghostFrequency o2) {
			return o2.i_occurence-o1.i_occurence;
		}}

	public String toString() {
		float f_percentage=(new Float(100*i_occurence))/(new Float(i_totalWordsInFrequencyFile));
//		return new String("\n["+s_triplet+"]:"+f_percentage+"%  ");
		return new String(String.format("\n  [%1s]: %2$5.2f%% ",s_triplet,f_percentage));
	}
}
