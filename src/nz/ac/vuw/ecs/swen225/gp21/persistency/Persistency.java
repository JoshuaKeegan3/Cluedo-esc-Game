package nz.ac.vuw.ecs.swen225.gp21.persistency;

import nz.ac.vuw.ecs.swen225.gp21.domain.*;
import nz.ac.vuw.ecs.swen225.gp21.domain.tiles.*;

import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/***
 *
 * @author Addison
 *
 * ID list:
 * Free Tile = 0
 * Wall = 1
 * Blue Key = 2
 * Blue Lock = 22
 * Green Key = 3
 * Green Lock = 33
 * Red Key = 4
 * Red Lock = 44
 * Question Tile = 97
 * Treasure Chip = 98
 * Chip Lock = 99
 * Exit = 100
 */

public class Persistency {
	String name; //More like a level ID level 1 or level 2
	String title; //Actual name of the level
	int height = 0; //Dimensions of the board
	int width = 0;
	int timeLimit = 0; //How much time the player has 
	int chipsLeft = 0; //How many chips the player needs to unlock the door
	String infoString; //When player steps on information tile display this message
	Domain domain;
	int x = 0; //x and y values for chap and other entities
	int y = 0;
	Tile[] tiles = new Tile[0];
	
	//Chap information
	Chap chap;
	int chapX = 0;
	int chapY = 0;
	List<TileColor> keysHeld = new ArrayList<TileColor>();
	int treasuresCollected = 0;
	
	//Instantiate the other entities (bugs)
	Enemy e1;
	String e1Path;
	Enemy e2;
	String e2Path;
	Enemy e3;
	String e3Path;
	List<Enemy> enemies = new ArrayList<>();

	public Persistency() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Reads the xml file using StaX parser with Iterator API
	 * Loads all information needed to pass to domain
	 * @param path
	 */
	public void loadLevel(String path)  throws FileNotFoundException{
		int counter = 0; //Counter specifies location in the array{
		//Create the StaX parser
		try {
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
			while (reader.hasNext()) {
				//Finds the next tag
				XMLEvent event = reader.nextEvent();
				if(event.isStartElement()) { //This finds the opening tag for the next element
					StartElement startElement = event.asStartElement();
					//System.out.println(startElement.getName().getLocalPart());
					switch(startElement.getName().getLocalPart()) {
						case "name":
							event = reader.nextEvent();
							if(event.isCharacters()) {
								name = event.asCharacters().getData();
							}
							break;
							
						case "title":
							event = reader.nextEvent();
							if(event.isCharacters()) {
								title = event.asCharacters().getData();
							}
							break;
							
						case "height":
							event = reader.nextEvent();
							if(event.isCharacters()) {
								String heightString = event.asCharacters().getData();
								height = Integer.valueOf(heightString);
							}
							break;
							
						case "width":
							event = reader.nextEvent();
							if(event.isCharacters()) {
								String widthString = event.asCharacters().getData();
								width = Integer.valueOf(widthString);
							}
							break;
							
						case "timeLimit":
							event = reader.nextEvent();
							if(event.isCharacters()) {
								String timeLimitString = event.asCharacters().getData();
								timeLimit = Integer.valueOf(timeLimitString);
							}
							break;
							
						case "chipsLeft":
							event = reader.nextEvent();
							if(event.isCharacters()) {
								String chipsLeftString = event.asCharacters().getData();
								chipsLeft = Integer.parseInt(chipsLeftString);
							}
							break;
							
						case "treasuresCollected":
							event = reader.nextEvent();
							if(event.isCharacters()) {
								String treasureString = event.asCharacters().getData();
								treasuresCollected = Integer.parseInt(treasureString);
							}
							break;
						
						case "information1":
							event = reader.nextEvent();
							if(event.isCharacters()) {
								infoString = event.asCharacters().getData();
							}
							break;
						
						case "background": //This is where all the tiles get placed
							event = reader.nextEvent();
							//Define the tile array here once background is read
							tiles = new Tile[width*height];
							break;
							
						case "tile":
							event = reader.nextEvent();
							if(event.isCharacters()) {
								String tileIDString = event.asCharacters().getData();
								int tileID = Integer.parseInt(tileIDString);
								switch(tileID) { //Generate certain tiles and add them to the list 
									case 0: //Free tile
										tiles[counter] = new EmptyTile();
										break;
									case 1: //Wall
										tiles[counter] = new Wall();
										break;
									case 2: //Blue Key
										tiles[counter] = new Key(TileColor.BLUE);
										break;
									case 22: //Blue Lock
										tiles[counter] = new Door(TileColor.BLUE);
										break;
									case 3: //Green Key
										tiles[counter] = new Key(TileColor.GREEN);
										break;
									case 33: //Green Lock
										tiles[counter] = new Door(TileColor.GREEN);
										break;
									case 4: //Red Key
										tiles[counter] = new Key(TileColor.RED);
										break;
									case 44: //Red Lock
										tiles[counter] = new Door(TileColor.RED);
										break;
									case 97: //Question Tile
										tiles[counter] = new Info(infoString);
										break;
									case 98: //Treasure
										tiles[counter] = new Treasure();
										break;
									case 99: //Chip Lock
										tiles[counter] = new Lock();
										break;
									case 100: //Exit
										tiles[counter] = new Exit();
										break;
								}
								counter++;
								break;
							}	
							
						case "chap": //Can create entities inside switch statements since characters are read after all tiles have been added
							event = reader.nextEvent();
							//System.out.println(startElement.getName().getLocalPart());
							if(startElement.getName().getLocalPart().equals("chap")) { //Make sure it's reading the chap tag
								while(reader.hasNext()) {
									event = reader.nextEvent(); //This moves the reader to the x tag
									if(event.isStartElement()) { //This reads the x tag as a start tag
										startElement = event.asStartElement();
										if(startElement.getName().getLocalPart().equals("x")) {
											event = reader.nextEvent();
											if(event.isCharacters()) {
												String xString = event.asCharacters().getData();
												chapX = Integer.parseInt(xString);
											}
										}
										if(startElement.getName().getLocalPart().equals("y")){
											event = reader.nextEvent();
											if(event.isCharacters()) {
												String yString = event.asCharacters().getData();
												chapY = Integer.parseInt(yString);
											}
											break;
										}
									}
								}
							}
							break;
							
						case "bug1": //Can create entities inside switch statements since characters are read after all tiles have been added
							event = reader.nextEvent();
							//System.out.println(startElement.getName().getLocalPart());
							if(startElement.getName().getLocalPart().equals("bug1")) { //Make sure it's reading the chap tag
								while(reader.hasNext()) {
									event = reader.nextEvent(); //This moves the reader to the x tag
									if(event.isStartElement()) { //This reads the x tag as a start tag
										startElement = event.asStartElement();
										if(startElement.getName().getLocalPart().equals("x")) {
											event = reader.nextEvent();
											if(event.isCharacters()) {
												String xString = event.asCharacters().getData();
												x = Integer.parseInt(xString);
											}
										}
										if(startElement.getName().getLocalPart().equals("y")){
											event = reader.nextEvent();
											if(event.isCharacters()) {
												String yString = event.asCharacters().getData();
												y = Integer.parseInt(yString);
											}
										}
										if(startElement.getName().getLocalPart().equals("route")) {
											event = reader.nextEvent();
											if(event.isCharacters()) {
												e1Path = event.asCharacters().getData();
												//Create chap here after getting the x and y value
												e1 = new Enemy(x, y, new EmptyTile(), e1Path);
												enemies.add(e1);
												break;
											}
										}
									}
								}
							}
							
						case "bug2":
							event = reader.nextEvent();
							if(startElement.getName().getLocalPart().equals("bug2")) { //Make sure it's reading the chap tag
								while(reader.hasNext()) {
									event = reader.nextEvent(); //This moves the reader to the x tag
									if(event.isStartElement()) { //This reads the x tag as a start tag
										startElement = event.asStartElement();
										if(startElement.getName().getLocalPart().equals("x")) {
											event = reader.nextEvent();
											if(event.isCharacters()) {
												String xString = event.asCharacters().getData();
												x = Integer.parseInt(xString);
											}
										}
										if(startElement.getName().getLocalPart().equals("y")){
											event = reader.nextEvent();
											if(event.isCharacters()) {
												String yString = event.asCharacters().getData();
												y = Integer.parseInt(yString);
											}
										}
										if(startElement.getName().getLocalPart().equals("route")) {
											event = reader.nextEvent();
											if(event.isCharacters()) {
												e2Path = event.asCharacters().getData();
												//Create chap here after getting the x and y value
												e2 = new Enemy(x, y, new EmptyTile(), e2Path);
												enemies.add(e2);
												break;
											}
										}
									}
								}
							}
							
						case "bug3":
							event = reader.nextEvent();
							//System.out.println(startElement.getName().getLocalPart());
							if(startElement.getName().getLocalPart().equals("bug3")) { //Make sure it's reading the chap tag
								while(reader.hasNext()) {
									event = reader.nextEvent(); //This moves the reader to the x tag
									if(event.isStartElement()) { //This reads the x tag as a start tag
										startElement = event.asStartElement();
										if(startElement.getName().getLocalPart().equals("x")) {
											event = reader.nextEvent();
											if(event.isCharacters()) {
												String xString = event.asCharacters().getData();
												x = Integer.parseInt(xString);
											}
										}
										if(startElement.getName().getLocalPart().equals("y")){
											event = reader.nextEvent();
											if(event.isCharacters()) {
												String yString = event.asCharacters().getData();
												y = Integer.parseInt(yString);
											}
										}
										if(startElement.getName().getLocalPart().equals("route")) {
											event = reader.nextEvent();
											if(event.isCharacters()) {
												e3Path = event.asCharacters().getData();
												//Create chap here after getting the x and y value
												e3 = new Enemy(x, y, new EmptyTile(), e3Path);
												enemies.add(e3);
												break;
											}
										}
									}
								}
							}
					}
					
					
				}
				//If the end event is level then end
				if(event.isEndElement()) {
					EndElement end = event.asEndElement();
					if(end.getName().getLocalPart().equals("level")) {
						//Create chap here after getting the x and y value
						chap = new Chap(keysHeld, chapX, chapY, tiles[chapY * width + chapX], treasuresCollected);
						domain = new Domain(width, height, chipsLeft, tiles, chap, enemies);
						System.out.println(domain.getBoardString());
					}
				}
			}
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * https://mkyong.com/java/how-to-write-xml-file-in-java-stax-writer/
	 * Used tutorial from above to help with formatting xml file saving
	 * @param string
	 * @return
	 * @throws TransformerException
	 */
	private String indentXML(String string) throws TransformerException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer t = transformerFactory.newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.STANDALONE, "yes");
		
		StreamSource source = new StreamSource(new StringReader(string));
		StringWriter out = new StringWriter();
		t.transform(source, new StreamResult(out));
		
		return out.toString();
	}
	
	/**
	 * https://mkyong.com/java/how-to-write-xml-file-in-java-stax-writer/
	 * Used tutorial from above to help with formatting xml file saving
	 */
	public void prettyPrintXML() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			saveLevel(out);
			
			String xml = new String(out.toByteArray(), StandardCharsets.UTF_8);
			
			String prettyPrint = indentXML(xml);
			
			Files.writeString(Paths.get("levels/savedLevel.xml"), prettyPrint, StandardCharsets.UTF_8);
		}
		catch (TransformerException | XMLStreamException | IOException e) {
            e.printStackTrace();
		}
	}
	
	public void saveLevel(OutputStream out) throws XMLStreamException{
	  boolean bugFound = false; //Boolean for formatting between lv 1 and lv 2
		int[] enemyCoords = new int[6];//Stores x and y of each enemy
		int chapX = 0;
		int chapY = 0;
		int enemyX = 0;
		int enemyY = 0;
		XMLOutputFactory output = XMLOutputFactory.newInstance();
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEventWriter writer = output.createXMLEventWriter(out);
		XMLEvent event = eventFactory.createStartDocument();
		
		//Get the board
		
		//Write to the xml file
		writer.add(event);
		
		writer.add(eventFactory.createStartElement("", "", "level"));
		
		//General information
		formatXML(writer, eventFactory, out, "name", name);
		formatXML(writer, eventFactory, out, "title", title);
		formatXML(writer, eventFactory, out, "height", String.valueOf(height));
		formatXML(writer, eventFactory, out, "width", String.valueOf(width));
		formatXML(writer, eventFactory, out, "timeLimit", String.valueOf(timeLimit));
		formatXML(writer, eventFactory, out, "chipsLeft", String.valueOf(domain.getTreasuresToCollect()));
		formatXML(writer, eventFactory, out, "treasuresCollected", String.valueOf(domain.getChap().getTreasuresCollected()));
		formatXML(writer, eventFactory, out, "information1", infoString);
		
		//Saving the tiles
		writer.add(eventFactory.createStartElement("", "", "background"));
		//Loop through all tiles
		for(int row=0; row<domain.getBoard().getHeight(); row++) {
			for(int col=0; col<domain.getBoard().getWidth(); col++) {
				Tile tile = domain.getBoard().getTile(col, row);
				switch(tile.getTextRepresentation()) {
					case '_': //Free Tile
						formatXML(writer, eventFactory, out, "tile", "0");
						break;
					case 'w': //Wall
						formatXML(writer, eventFactory, out, "tile", "1");
						break;
					case 'k': //All keys
						if(tile instanceof Key) {
							if(((Key) tile).getColor().equals(TileColor.BLUE)) {
								formatXML(writer, eventFactory, out, "tile", "2");
								break;
							}
							if(((Key) tile).getColor().equals(TileColor.GREEN)) {
								formatXML(writer, eventFactory, out, "tile", "3");
								break;
							}
							if(((Key) tile).getColor().equals(TileColor.RED)) {
								formatXML(writer, eventFactory, out, "tile", "4");
								break;
							}
						}
					case 'd': //All locked doors
						if(tile instanceof Door) {
							if(((Door) tile).getColor().equals(TileColor.BLUE)) {
								formatXML(writer, eventFactory, out, "tile", "22");
								break;
							}
							if(((Door) tile).getColor().equals(TileColor.GREEN)) {
								formatXML(writer, eventFactory, out, "tile", "33");
								break;
							}
							if(((Door) tile).getColor().equals(TileColor.RED)) {
								formatXML(writer, eventFactory, out, "tile", "44");
								break;
							}
						}
					case 'i': //information tile
						formatXML(writer, eventFactory, out, "tile", "97");
						break;
					case 't': //treasure chip 
						formatXML(writer, eventFactory, out, "tile", "98");
						break;
					case 'l': //lock tile for treasure chips
						formatXML(writer, eventFactory, out, "tile", "99");
						break;
					case 'e': //exit tile
						formatXML(writer, eventFactory, out, "tile", "100");
						break;
					case 'c': //Ecountering chap
						if(tile instanceof Chap) {
							if(((Chap)tile).isStandingOn().getTextRepresentation() == ('i')) {
								formatXML(writer, eventFactory, out, "tile", "97");
							}
							if(((Chap)tile).isStandingOn().getTextRepresentation() == ('_')) {
								formatXML(writer, eventFactory, out, "tile", "0");
							}
							chapX = ((Chap)tile).getLocation().getX();
							chapY = ((Chap)tile).getLocation().getY();
							break;
						}
					case 'b': //Encountering enemy/bug
						formatXML(writer, eventFactory, out, "tile", "0"); //Place empty tile underneath them
						if(tile instanceof Enemy) {
						  bugFound = true;
							enemyX = ((Enemy)tile).getLocation().getX();
							enemyY = ((Enemy)tile).getLocation().getY();
							if(((Enemy)tile).getPath().equals(e1Path)) { //Confirm bug1
								enemyCoords[0] = enemyX;
								enemyCoords[1] = enemyY;
							}
							if(((Enemy)tile).getPath().equals(e2Path)) { //Confirm bug2
								enemyCoords[2] = enemyX;
								enemyCoords[3] = enemyY;
							}
							if(((Enemy)tile).getPath().equals(e3Path)) { //Confirm bug3
								enemyCoords[4] = enemyX;
								enemyCoords[5] = enemyY;
							}
						}
						break;
				}
			}
		}
		writer.add(eventFactory.createEndElement("", "", "background"));
		
		//Write the info for characters
		//Write chap here

		writer.add(eventFactory.createStartElement("", "", "entities"));
		
		
		writer.add(eventFactory.createStartElement("", "", "chap"));
		formatXML(writer, eventFactory, out, "x", String.valueOf(chapX));
		formatXML(writer, eventFactory, out, "y", String.valueOf(chapY));
		writer.add(eventFactory.createEndElement("", "", "chap"));
		
		if(bugFound) {
		  writer.add(eventFactory.createStartElement("", "", "bug1"));
	    formatXML(writer, eventFactory, out, "x", String.valueOf(enemyCoords[0]));
	    formatXML(writer, eventFactory, out, "y", String.valueOf(enemyCoords[1]));
	    formatXML(writer, eventFactory, out, "route", e1Path);
	    writer.add(eventFactory.createEndElement("", "", "bug1"));
	    
	    writer.add(eventFactory.createStartElement("", "", "bug2"));
	    formatXML(writer, eventFactory, out, "x", String.valueOf(enemyCoords[2]));
	    formatXML(writer, eventFactory, out, "y", String.valueOf(enemyCoords[3]));
	    formatXML(writer, eventFactory, out, "route", e2Path);
	    writer.add(eventFactory.createEndElement("", "", "bug2"));
	    
	    writer.add(eventFactory.createStartElement("", "", "bug3"));
	    formatXML(writer, eventFactory, out, "x", String.valueOf(enemyCoords[4]));
	    formatXML(writer, eventFactory, out, "y", String.valueOf(enemyCoords[5]));
	    formatXML(writer, eventFactory, out, "route", e3Path);
	    writer.add(eventFactory.createEndElement("", "", "bug3"));
		}
		
		writer.add(eventFactory.createEndElement("", "", "entities"));
		
		
		writer.add(eventFactory.createEndElement("", "", "level"));
		
		//End the document
		writer.add(eventFactory.createEndDocument());
		writer.flush();
		writer.close();
	}
	
	public void formatXML(XMLEventWriter writer, XMLEventFactory eventFactory, OutputStream out, String tagName, String characters) throws XMLStreamException {
		writer.add(eventFactory.createStartElement("", "", tagName));
		writer.add(eventFactory.createCharacters(characters));
		writer.add(eventFactory.createEndElement("", "", tagName));
	}
	//Create method to return time limit
	public int getTime() {
		return timeLimit;
	}
	
	//Create method to return domain
	public Domain getDomain() {
		return domain;
	}
}
