import reader.Reader;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class MainReader {
	public static void main(String[] args) throws IOException, DataFormatException {
		Reader gaffReader = new Reader("out.gaff");

		System.out.println(gaffReader.read("background"));
		System.out.println(gaffReader.read("entities"));
		System.out.println(gaffReader.read("info"));
		System.out.println(gaffReader.read("tiles"));
	}
}
