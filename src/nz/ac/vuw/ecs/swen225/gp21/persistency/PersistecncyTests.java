package nz.ac.vuw.ecs.swen225.gp21.persistency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.FileNotFoundException;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp21.domain.ChapOnInvalidTileException;
import nz.ac.vuw.ecs.swen225.gp21.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp21.domain.Move;

import nz.ac.vuw.ecs.swen225.gp21.domain.Domain;

/**
 * At least 80% coverage
 * @author Addison
 *
 */
public class PersistecncyTests {
	
	
	//Valid Tests
	@Test
	public void validTest1() {
	  //Tests level 1 loading
	  
	  String input =
	       "wwwwwwwwwww\n"
	      +"wwwwwewwwww\n"
	      +"wwwwwlwwwww\n"
	      +"wwwwwdwwwww\n"
	      +"wwwwi__wwww\n"
	      +"w__w_k_w__w\n"
	      +"wk_d_c_d_kw\n"
	      +"w__w___w__w\n"
	      +"wwwwwdwwwww\n"
	      +"wwwwtttwwww\n"
	      +"wwwwtttwwww\n"
	      +"wwwwtktwwww\n"
	      +"wwwwwwwwwww\n";
	  try {
      check("levels/level1.xml", input);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
		
	}
	
	@Test
	public void validTest2() {
	  //Tests level 2 loading
	  
	  String input =
	       "wwwwwwwwwwwwwwww\n"
	      +"w___b___wwwwwwww\n"
	      +"w____t__wwwwbwww\n"
	      +"w_c_____dttt_lew\n"
	      +"w____k__wwww_www\n"
	      +"w_____b_wwwwwwww\n"
	      +"wwwwwwwwwwwwwwww\n";
	  try {
      check("levels/level2.xml", input);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
	  
	}
	
	@Test
	public void validTest3() {
	  //Tests saving and reloading level 1
	  String input =
        "wwwwwwwwwww\n"
       +"wwwwwewwwww\n"
       +"wwwwwlwwwww\n"
       +"wwwwwdwwwww\n"
       +"wwwwi__wwww\n"
       +"w__w_k_w__w\n"
       +"wk_d_c_d_kw\n"
       +"w__w___w__w\n"
       +"wwwwwdwwwww\n"
       +"wwwwtttwwww\n"
       +"wwwwtttwwww\n"
       +"wwwwtktwwww\n"
       +"wwwwwwwwwww\n";
   try {
     check("levels/level1.xml", input);
   } catch (FileNotFoundException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
   }
	}
	
	@Test
  public void validTest4() {
    //Tests saving and reloading level 2
	  String input =
        "wwwwwwwwwwwwwwww\n"
       +"w___b___wwwwwwww\n"
       +"w____t__wwwwbwww\n"
       +"w_c_____dttt_lew\n"
       +"w____k__wwww_www\n"
       +"w_____b_wwwwwwww\n"
       +"wwwwwwwwwwwwwwww\n";
	  try {
	     check("levels/level2.xml", input);
	   } catch (FileNotFoundException e) {
	     // TODO Auto-generated catch block
	     e.printStackTrace();
	   }
  }
	
	@Test
	public void validTest5() throws ChapOnInvalidTileException {
	  List<Move> moves = new ArrayList<>();
	  moves.add(new Move(0, 1));
	  String input =
        "wwwwwwwwwww\n"
       +"wwwwwewwwww\n"
       +"wwwwwlwwwww\n"
       +"wwwwwdwwwww\n"
       +"wwwwi__wwww\n"
       +"w__w_k_w__w\n"
       +"wk_d___d_kw\n"
       +"w__w_c_w__w\n"
       +"wwwwwdwwwww\n"
       +"wwwwtttwwww\n"
       +"wwwwtttwwww\n"
       +"wwwwtktwwww\n"
       +"wwwwwwwwwww\n";
   try {
     check("levels/level1.xml", input, moves);
   } catch (FileNotFoundException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
   }
	}
	
	@Test
	public void validTest6() throws ChapOnInvalidTileException {
	  List<Move> moves = new ArrayList<>();
	  moves.add(new Move(1, 0));
	  String input =
		        "wwwwwwwwwwwwwwww\n"
		       +"w___b___wwwwwwww\n"
		       +"w____t__wwwwbwww\n"
		       +"w__c____dttt_lew\n"
		       +"w____k__wwww_www\n"
		       +"w_____b_wwwwwwww\n"
		       +"wwwwwwwwwwwwwwww\n";
   try {
     check("levels/level2.xml", input, moves);
   } catch (FileNotFoundException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
   }
	}
	
	//Invalid Tests
	@Test
	public void invalidTest1() {
		//Load an invalid file
		assertThrows(FileNotFoundException.class, () -> {
			check("idontexist.xml", "");
		});
	}
	
	//Helper method for checking
	public void check(String path, String input) throws FileNotFoundException{
	  Persistency p = new Persistency();
    p.loadLevel(path);
    p.prettyPrintXML();
    p.loadLevel("levels/savedLevel.xml");
    Domain d = p.getDomain();
    String expected = d.getBoardString();
    assertEquals(expected, input);
	}
	
	public void check(String path, String input, List<Move> moves) throws FileNotFoundException, ChapOnInvalidTileException{
    Persistency p = new Persistency();
    p.loadLevel(path);
    p.prettyPrintXML();
    p.loadLevel("levels/savedLevel.xml");
    Domain d = p.getDomain();
    for(Move m : moves) {
      d.move(m);
    }
    String expected = d.getBoardString();
    assertEquals(expected, input);
  }
	

}
