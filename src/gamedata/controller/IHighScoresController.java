package gamedata.controller;

import java.io.File;
import java.io.IOException;

import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

/**
 * The purpose of this interface is to serve as the public interface with which the ISplashScreen can interact to deal with high scores functionality if the high scores button is pressed.
 * @author cmt57
 *
 */
public interface IHighScoresController {
	
	/**
	 * Will display the high scores on a HighScoreScreen
	 * @param highScores a map of user names associated with their high scores
	 * @throws TransformerException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public void viewHighScores() throws ParserConfigurationException, SAXException, IOException, TransformerException;
	
	public void saveHighScore(int score, String player) throws ParserConfigurationException, SAXException, IOException, TransformerException;
	
	/**
	 * Will clear the high scores from a highScores.xml
	 * @throws TransformerException 
	 * @throws ParserConfigurationException 
	 */
	public void clearHighScores() throws SAXException, IOException, ParserConfigurationException, TransformerException;
	

}
