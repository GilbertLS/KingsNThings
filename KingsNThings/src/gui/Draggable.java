package gui;

import javafx.scene.input.DataFormat;

public interface Draggable {
	static DataFormat thingRackIds = new DataFormat("thingRackIds");
	static DataFormat originalTile = new DataFormat("tileReference");
}
