package reader;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class FileReader {
	private String fileName    = "";

	private String fileIn;
	private int fileContentSize;
	private int fileContentOffset;

	public FileReader(int fileOffset, String fileIn, byte[] gaff) {
		this.fileIn = fileIn;
		int fileNameSize = (int) Reader.gUI(gaff[fileOffset]);
		for (int i = 0; i < fileNameSize; i++) {
			fileName += (char) gaff[fileOffset + 1 + i];
		}

		int fileContentSizeOffset = fileOffset + fileNameSize + 1;
		fileContentSize = (int) ((Reader.gUI(gaff[fileContentSizeOffset]) << 24) + (Reader.gUI(gaff[fileContentSizeOffset + 1]) << 16) + (Reader.gUI(
				gaff[fileContentSizeOffset + 2]) << 8) + Reader.gUI(gaff[fileContentSizeOffset + 3]));

		fileContentOffset = (int) ((Reader.gUI(gaff[fileContentSizeOffset + 4]) << 24) + (Reader.gUI(gaff[fileContentSizeOffset + 5]) << 16) + (Reader.gUI(
				gaff[fileContentSizeOffset + 6]) << 8) + Reader.gUI(gaff[fileContentSizeOffset + 7]));

	}

	public String read() throws IOException {
		File file = new File(fileIn);
		FileInputStream fileStream = new FileInputStream(file);
		byte[] gaff = new byte[(int)file.length()];
		byte[] fileContentZip = new byte[fileContentSize];
		fileStream.read(gaff, 0, gaff.length);

		System.arraycopy(gaff, fileContentOffset, fileContentZip, 0, fileContentSize);

		//		Decompression
		ByteArrayInputStream byteStream = new ByteArrayInputStream(fileContentZip);
		GZIPInputStream zipStream = new GZIPInputStream(byteStream);
		InputStreamReader reader = new InputStreamReader(zipStream);
		BufferedReader in = new BufferedReader(reader);

		String fileContent = "";
		String tmp;
		while ((tmp = in.readLine()) != null) {
			fileContent += tmp;
		}

		return fileContent;
	}

	public String getFileName() {
		return fileName;
	}
}
