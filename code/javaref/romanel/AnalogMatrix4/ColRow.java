
public class ColRow implements Comparable<ColRow> {
	int i_col;
	int i_row;

	public ColRow(String s_col, String s_row) {
		i_col = Integer.valueOf(s_col);
		i_row = Integer.valueOf(s_row);
	}

	public ColRow(int i_col, int i_row) {
		this.i_col = i_col;
		this.i_row = i_row;
	}

	public ColRow(Key col, Key row) {
		i_col=col.i_col;
		i_row=row.i_row;
	}

	@Override
	public int compareTo(ColRow cr) {
		return ((i_col-cr.i_col)*1000)+(i_row-cr.i_row);
	}

	public String toString() {
		return("C"+i_col+"R"+i_row);
	}
}
