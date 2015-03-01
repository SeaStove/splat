

package atc;

import java.lang.Object;
import java.util.*;

/**
 * "Controller" implementation using the same control method as the Classical ATC.
 */
public class ATCInputhandler_impl extends Object implements ATCInputhandler
{
  protected ATC atc_obj = null;
  protected ATCInputhandler_impl() { super(); }
  public ATCInputhandler_impl( ATC a )
  { 
    super();
    atc_obj = a;
  }

  protected LinkedList cmd_str = new LinkedList();
  protected String full_cmd_str = new String("");
  protected int plane_id;
  protected Command cmd = null;
  
  public int x;
  public int y;

  public void processKey( char c )
  {
    if( c == '\n' ) return;
    if( c == '\b' ) // backspace
    {
      if( ! cmd_str.isEmpty() )
        cmd_str.removeLast();
    }
    else
      cmd_str.add( new Character(c) );
    if( parse(false) )
      atc_obj.getData().setCommandString( full_cmd_str );
    else
    {
      cmd_str.removeLast();
      atc_obj.getData().setCommandString( full_cmd_str );
    }
  }

  public boolean processCommand( )
  {
    if( parse(true) )
    {
      if( cmd != null )
        atc_obj.getData().setCommand( plane_id, cmd );

      atc_obj.getData().setCommandString( "" );

      reset();
      return true;
    }
    else
    {
      atc_obj.getData().setCommandString( "" );
      reset();
      return false;
    }
  }

  protected boolean parse( boolean full_flag )
  {
    if(ATC.debug_flag) //DEBUG
    {
      System.out.println( "Parsing: " + cmd_str.toString() + full_flag );
    }

    int parse_state = 1;
    ListIterator it = cmd_str.listIterator();
    char c;
    Plane p = null;
    StaticObj objs[] = null, obj_to = null, obj_at = null;

    
    full_cmd_str = new String("");
    int coordcount = 0;
    while( it.hasNext() && parse_state != 99 && coordcount < 2){
    	c = ((Character)it.next()).charValue();  
	    //begin
	    if(coordcount == 0 && !Character.isLetter(c)){
	    	full_cmd_str += c;
	    	full_cmd_str += ", ";
	    	x = Character.getNumericValue(c);
	    	coordcount++;
	    }
    	if( coordcount == 1 && Character.isLetter(c) ){
    		full_cmd_str += c;
	    	y = Character.getNumericValue(c)-9;
    		coordcount++;
    	}
    } //end while

    if( full_flag )
    {
    	Zap quickZap = new Zap(x,y);
    	System.out.println("creating zap..."+x+", "+y);
    	atc_obj.getData().setCommand(quickZap);
      if( parse_state != 99 && parse_state != 5 && parse_state != 4 )
        return false;
      else
        return true;
    }
    return true;
  }

  public boolean processActionCommand( String command )
  {
    if( "New".equals( command ) )
    {
      reset();
      atc_obj.getData().start();
    }
    else if( "Exit".equals( command ) )
    {
      atc_obj.stopATC();
    }
    else if( "Pause".equals(command)){					// Added- pause functionality
    	//atc_obj.pauseATC();
    }
    return true;
  }

  public void reset()
  {
    cmd = null;
    cmd_str = new LinkedList();
  }
};
