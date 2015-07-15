package writer;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Writer {
	private static final char[] MAGIC     = new char[] { 'G', 'A', 'F', 'F' };
	private static final char[] MAGIC_END = new char[] { 'G', 'E', 'O', 'F' };
	private static final byte   VERSION   = 1;

	private static final int HEADER_SIZE = 10;
	public static        int LOCATION    = HEADER_SIZE;

	public static List<Runnable> header = new ArrayList<>();
	public static List<Runnable> content = new ArrayList<>();
	public static ByteArrayOutputStream filesHeaderStream = new ByteArrayOutputStream();
	public static ByteArrayOutputStream filesContentStream = new ByteArrayOutputStream();

	public static void write(String fileOut) throws IOException {
//		Header
		header.forEach(Runnable::run);

//		Content
		content.forEach(Runnable::run);

//		To byte array
		byte[] filesHeader = filesHeaderStream.toByteArray();
		byte[] filesContent = filesContentStream.toByteArray();


//		HEADER
		byte[] magicValue = ByteBuffer.allocate(4).putInt(((int) MAGIC[0] << 24) + ((int) MAGIC[1] << 16) + ((int) MAGIC[2] << 8) + ((int) MAGIC[3])).array();
		byte[] version = ByteBuffer.allocate(1).put(VERSION).array();
		byte[] filesOffset = ByteBuffer.allocate(4).putInt(HEADER_SIZE).array();
		byte[] filesCount = ByteBuffer.allocate(1).put(writer.FileWriter.FILE_COUNT).array();

//		EOF
		byte[] magicEndValue = ByteBuffer.allocate(4).putInt(((int) MAGIC_END[0] << 24) + ((int) MAGIC_END[1] << 16) + ((int) MAGIC_END[2] << 8) + ((int) MAGIC_END[3])).array();

////////////////////////////////////////////////////////////////////////////

//		Writing to final byte array
		ByteArrayOutputStream finalFile = new ByteArrayOutputStream();
//		HEADER
		finalFile.write(magicValue);
		finalFile.write(version);
//		Offset data
		finalFile.write(filesOffset);
		finalFile.write(filesCount);
//		File headers
		finalFile.write(filesHeader);

//		FILES
//		Content
		finalFile.write(filesContent);

//		EOF
		finalFile.write(magicEndValue);

////////////////////////////////////////////////////////////////////////////

//		Writing final byte array to file
		FileOutputStream fos = new FileOutputStream(fileOut);
		fos.write(finalFile.toByteArray());
		fos.close();
	}
}
