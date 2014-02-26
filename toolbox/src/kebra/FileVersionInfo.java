package kebra;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APIOptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class FileVersionInfo
{
	interface Version extends Library {

		Version INSTANCE = (Version) Native.loadLibrary("Version", Version.class, W32APIOptions.UNICODE_OPTIONS);

		public int GetFileVersionInfoSizeW(String lptstrFilename, int dwDummy);

		public boolean GetFileVersionInfoW(String lptstrFilename, int dwHandle,
				int dwLen, Pointer lpData);

		public int VerQueryValueW(Pointer pBlock, String lpSubBlock,
				PointerByReference lplpBuffer, IntByReference puLen);

	}

	public static class VS_FIXEDFILEINFO extends com.sun.jna.Structure {
		public int dwSignature;
		public int dwStrucVersion;
		public int dwFileVersionMS;
		public int dwFileVersionLS;
		public int dwProductVersionMS;
		public int dwProductVersionLS;
		public int dwFileFlagsMask;
		public int dwFileFlags;
		public int dwFileOS;
		public int dwFileType;
		public int dwFileSubtype;
		public int dwFileDateMS;
		public int dwFileDateLS;

		public VS_FIXEDFILEINFO(com.sun.jna.Pointer p){
			super(p);
		}

		@Override
		/*protected List getFieldOrder() {
			// TODO Auto-generated method stub
			return null;
		}*/


		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { 
					"dwSignature", "dwStrucVersion", "dwFileVersionMS", "dwFileVersionLS",
					"dwProductVersionMS","dwProductVersionLS","dwFileFlagsMask","dwFileFlags",
					"dwFileOS","dwFileType","dwFileSubtype","dwFileDateMS","dwFileDateLS" });
		}

	}
	
	public static String sresult = new String("rien pour l'instant");
	
	public static String getVersion(String filename) throws IOException {
		//String s = "C:\\Program Files (x86)\\Java\\jre7\\bin\\java.exe";
		String s = filename;
		int dwDummy = 0;
		int versionlength = Version.INSTANCE.GetFileVersionInfoSizeW(
				s, dwDummy);
		//System.out.println("versionlength: "+versionlength);

		byte[] bufferarray = new byte[versionlength];
		Pointer lpData = new Memory(bufferarray.length);   

		PointerByReference lplpBuffer = new PointerByReference();
		IntByReference puLen = new IntByReference();
		boolean FileInfoResult = Version.INSTANCE.GetFileVersionInfoW(
				s,
				0, versionlength, lpData);
		//System.out.println("FileInfoResult: "+FileInfoResult);
		int verQueryVal = Version.INSTANCE.VerQueryValueW(lpData,
				"\\", lplpBuffer,
				puLen);
		/*System.out.println("verQueryVal: "+verQueryVal);
		System.out.println("");
		for (int i = 0; i < versionlength; i++) {
			System.out.print(lpData.getChar(i));
		}
		System.out.println("\n");*/

		VS_FIXEDFILEINFO lplpBufStructure = new VS_FIXEDFILEINFO(
				lplpBuffer.getValue());
		lplpBufStructure.read();

		short[] rtnData = new short[4];
		rtnData[0] = (short) (lplpBufStructure.dwFileVersionMS >> 16);
		rtnData[1] = (short) (lplpBufStructure.dwFileVersionMS & 0xffff);
		rtnData[2] = (short) (lplpBufStructure.dwFileVersionLS >> 16);
		rtnData[3] = (short) (lplpBufStructure.dwFileVersionLS & 0xffff);

		/*System.out.print("["+s+"] version: ,");

		for (int i = 0; i < rtnData.length; i++) {
			System.out.print(rtnData[i]+",");
		}
		System.out.println("\n");*/
		sresult = new String("["+s+"] version: ,"+rtnData[0]+","+rtnData[1]+","+rtnData[2]+","+rtnData[3]+",");
		return sresult;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(getVersion(args[0]));		
	}
} 