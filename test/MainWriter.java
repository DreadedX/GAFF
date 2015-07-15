import writer.Writer;

import java.io.IOException;

public class MainWriter {
	public static void main(String[] args) throws IOException {
		new writer.FileWriter("in/info.json", "info");
		new writer.FileWriter("in/entities.json", "entities");
		new writer.FileWriter("in/background.json", "background");
		new writer.FileWriter("in/tiles.json", "tiles");
		new writer.FileWriter("in/lights.json", "lights");

		Writer.write("out.gaff");
	}
}
