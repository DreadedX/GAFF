import writer.Writer;

import java.io.IOException;

public class MainWriter {
	public static void main(String[] args) throws IOException {
		new writer.FileWriter("in/info.json");
		new writer.FileWriter("in/entities.json");
		new writer.FileWriter("in/background.json");
		new writer.FileWriter("in/tiles.json");
		new writer.FileWriter("in/lights.json");

		Writer.write("out.gaff");
	}
}
