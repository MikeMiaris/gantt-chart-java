package test;

import org.junit.*;
import dom2app.SimpleTableModel;
import static org.junit.Assert.assertEquals;
import backend.MainController;

public class MainControllerTest {
	private MainControllerTest MainControllerTest;
	
    @Before
    public void setUp() throws Exception {
        MainControllerTest = new MainControllerTest();
    }
    
    
    @Test 
    //T1_VO
    public void testLoad() {
    	MainController test = new MainController();
    	String expected = ("100 Prepare Fry 0 1 12 60 "
    					+"101 Turn on burner (low) 100 1 1 10 "
    					+"102 Break eggs and pour into fry 100 2 4 10 "
    					+"103 Steer mixture to avoid sticking 100 5 10 10 "
    					+"105 Salt, pepper 100 5 5 10 "
    					+"104 Throw yellow cheese into fry 100 6 12 10 "
    					+"106 Turn burner off 100 12 12 10 "
    					+"200 Prepare the bread 0 10 12 20 "
    					+"201 Heat bread in toaster 200 10 12 10 "
    					+"202 Little bit of salt, galric spice to bread 200 12 12 10 "
    					+"300 Serve eggs 0 13 20 30 "
    					+"301 Put bread in plate 300 13 13 10 "
    					+"302 Put eggs on bread 300 14 14 10 "
    					+"303 Wash fry 300 15 20 10 ");
    	String result = "";
    	
    	
    	
    	
    	SimpleTableModel temp = test.load("./src/main/resources/input/EggsScrambled.tsv", "\t");
    	
    	for(int i = 0; i < temp.getData().size(); i++) {
    		for(int j = 0; j < temp.getData().get(i).length; j++) {
    			result += temp.getData().get(i)[j]+" ";
    		}
    	}
    	assertEquals("Eggsscrambled should be the same as Eggs after being loaded", expected, result );
    }
    
     
    @Test
    //T2_V0
    public void testGetTopLevelTasks() {
     	String expected = ("100 Prepare Fry 0 1 12 60 200 Prepare the bread 0 10 12 20 300 Serve eggs 0 13 20 30 ");
     	String result = "";
     	
     	MainController test = new MainController();
     	test.load("./src/main/resources/input/EggsScrambled.tsv", "\t");
     	
     	SimpleTableModel doo = test.getTopLevelTasks();
     	
     	for(int i = 0; i < doo.getData().size(); i++) {
     		for(int j = 0; j < doo.getData().get(i).length; j++) {
     			result += doo.getData().get(i)[j]+" ";
     		}
     	}
     	assertEquals(expected.length(),result.length());
     	assertEquals("The manually created SimpleTableModel 'result' should be the same as the expected", expected, result);
    }
     
     
    @Test
    //T3_V0_01
    public void testGetTaskByPrefix() {
     	
     	String expected = ("302 Put eggs on bread 300 14 14 10 301 Put bread in plate 300 13 13 10 ");
     	String result = "";
     	
     	
     	MainController test = new MainController();
     	test.load("./src/main/resources/input/EggsScrambled.tsv", "\t");
     	
     	SimpleTableModel doo = test.getTasksByPrefix("put");
     	
     	for(int i = 0; i < doo.getData().size(); i++) {
     		for(int j = 0; j < doo.getData().get(i).length; j++) {
     			result += doo.getData().get(i)[j]+" ";
     		}
     	}
     	assertEquals(expected.length(),result.length());
     	assertEquals("The manually created string 'result' should be the same as the expected", expected, result);
    }
    
    
    @Test
    //T3_V0_02
    public void testGetTaskByPrefix2() {
     	String expected = ("No tasks found with that Prefix. - - - - - ");
     	String result = "";
     	
     	MainController test = new MainController();
     	test.load("./src/main/resources/input/EggsScrambled.tsv", "\t");
     	
     	SimpleTableModel doo = test.getTasksByPrefix("cook");
     	
    	for(int i = 0; i < doo.getData().get(0).length; i++) {
    		result += doo.getData().get(0)[i]+" ";
    	}
    	
     	assertEquals(expected.length(),result.length());
     	assertEquals("The manually created string 'result' should be the same as the expected", expected, result);
     	
    }
    
    
    @Test
    //T4_V0_01
    public void testGetTaskById() {
    	String expected = ("302 Put eggs on bread 300 14 14 10 ");
    	String result = "";
    	
    	MainController test = new MainController();
    	test.load("./src/main/resources/input/EggsScrambled.tsv", "\t");
    	
    	SimpleTableModel doo = test.getTaskById(302);
    	
    	for(int i = 0; i < doo.getData().get(0).length; i++) {
    		result += doo.getData().get(0)[i]+" ";
    	}
    	assertEquals(expected.length(),result.length());
    	assertEquals("The manually created SimpleTableModel 'result' should be the same as the expected",expected , result );
    }
    
    
    @Test
    //T4_V0_02
    public void testGetTaskById2(){
    	String expected = ("No task found with that ID. - - - - - ");
    	String result = "";
    	
    	MainController test = new MainController();
    	test.load("./src/main/resources/input/EggsScrambled.tsv", "\t");
    	
    	SimpleTableModel doo = test.getTaskById(120);
    	
    	for(int i = 0; i < doo.getData().get(0).length; i++) {
    		result += doo.getData().get(0)[i]+" ";
    	}
    	
    	assertEquals(expected.length(),result.length());
    	assertEquals("The manually created SimpleTableModel 'result' should be the same as the expected",expected , result );
    }
}
