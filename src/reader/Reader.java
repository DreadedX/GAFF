package reader;

import java.io.*;

public class Reader {
	FileReader[] files;

	public Reader(String fileIn) throws IOException {
		File file = new File(fileIn);
		FileInputStream fileStream = new FileInputStream(file);

		byte[] gaff = new byte[(int)file.length()];

		fileStream.read(gaff, 0, gaff.length);

//		HEADER
		String magic = "" + (char) gaff[0] + (char) gaff[1] + (char) gaff[2] + (char) gaff[3];
		if (!magic.equals("GAFF")) {
			System.out.println("This file is not a gaff file!");
			return;
		}

		int version = (int) gaff[4];
		if (version != 1) {
			System.out.println("This is an unknown version of gaff");
			return;
		}

		int fileLength = gaff.length-1;
		String magicEnd = "" + (char) gaff[fileLength-3] + (char) gaff[fileLength-2] + (char) gaff[fileLength-1] + (char) gaff[fileLength];
		if (!magicEnd.equals("GEOF")) {
			System.out.println("This file is corrupt!");
			return;
		}

		int filesOffset = (int) ((gUI(gaff[5]) << 24) + (gUI(gaff[6]) << 16) + (gUI(gaff[7]) << 8) + gUI(gaff[8]));
		int filesCount = (int) gUI((gaff[9]));

//		HEADER END

		int offset = 0;
		files = new FileReader[filesCount];
		for (int i = 0; i < filesCount; i++) {
			files[i] = new FileReader(filesOffset + offset, fileIn, gaff);
			offset += 1 + (int) gUI(gaff[filesOffset + offset]) + 8;
		}


//		for (FileReader f : files) {
//			System.out.println(f.getFileName());
//			System.out.println(f.read());
//		}
	}

	public String read(String name) throws IOException {
		for (FileReader f : files) {
			if (f.getFileName().equals(name)) {
				return f.read();
			}
		}
		return null;
	}

//	getUnsignedInt
	public static long gUI(int x) {
		if (x < 0) {
			return x + 256;
		} else {
			return x;
		}
	}
}
