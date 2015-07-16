package writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.Deflater;

public class FileWriter {
	public byte[] header;
	public byte[] content;

	public static byte FILE_COUNT = 0;

	public final static int MAX_FILE_SIZE = 10000; //Max file size of 10kB

	public FileWriter(String fileIn) throws IOException {
		File file = new File(fileIn);
		String name = file.getName().substring(0, file.getName().lastIndexOf("."));

		FileInputStream fileStream = new FileInputStream(file);

		byte[] fileContentRaw = new byte[(int)file.length()];

		fileStream.read(fileContentRaw, 0, fileContentRaw.length);

//		Deflate
		byte[] fileContentTmp = new byte[MAX_FILE_SIZE];
		Deflater compressor = new Deflater();
		compressor.setInput(fileContentRaw);
		compressor.finish();
		int compressedDataLength = compressor.deflate(fileContentTmp);
		System.out.println(compressedDataLength);
		compressor.end();

		byte[] fileContent = new byte[compressedDataLength];

		System.arraycopy(fileContentTmp, 0, fileContent, 0, compressedDataLength);
//		End Deflate

		byte fileNameSize = (byte) name.length();
		int fileContentSize = fileContent.length;

		header = ByteBuffer.allocate(1 + name.length() + 4).put(fileNameSize).put(name.getBytes()).putInt(fileContentSize).array();
		content = ByteBuffer.allocate(fileContentSize).put(fileContent).array();

		Writer.LOCATION += header.length + 4;
		FILE_COUNT += 1;

		Writer.header.add(() -> {
			try {
				Writer.filesHeaderStream.write(header);
				Writer.filesHeaderStream.write(ByteBuffer.allocate(4).putInt(Writer.LOCATION).array());
				Writer.LOCATION += content.length;
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Writer.content.add(() -> {
			try {
				Writer.filesContentStream.write(content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
