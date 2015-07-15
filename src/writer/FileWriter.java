package writer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPOutputStream;

public class FileWriter {
	public byte[] header;
	public byte[] content;

	public static byte FILE_COUNT = 0;

	public FileWriter(String fileIn, String name) throws IOException {
		File file = new File(fileIn);

		FileInputStream fileStream = new FileInputStream(file);

		byte[] fileContentTmp = new byte[(int)file.length()];

		fileStream.read(fileContentTmp, 0, fileContentTmp.length);

//		Compression
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream((fileContentTmp.length));
			GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);
			zipStream.write(fileContentTmp);
			zipStream.close();
			byteStream.close();

		byte[] fileContent = byteStream.toByteArray();

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
