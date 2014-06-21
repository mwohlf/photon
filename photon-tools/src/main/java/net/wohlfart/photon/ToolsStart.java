package net.wohlfart.photon;
import java.awt.EventQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ToolsStart {
	private static final Logger LOGGER = LoggerFactory.getLogger(ToolsStart.class);

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TextureTool() .initialize() .start();
			}
		});
	}

}

