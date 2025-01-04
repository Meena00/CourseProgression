import java.io.IOException;
import java.util.LinkedList;

/**
 * Display driver.
 *  
 * 
 */
class Display {
	/**
	 *  Main method display driver.
	 *  @param args 0 - filename, 1 - start node, 2 - optional "GUI"
	 */
	public static void main(String[] args) {
		String usage = "java Display filename startNodeName [GUI]";
		try {
			if(args.length == 2) {
				ThreeTenGraph<String> graph = TopologicalSort.getGraph(args[0]);
				LinkedList<String> output = TopologicalSort.topologicalSort(graph,args[1]);
				
				StringBuilder sb = new StringBuilder();
				for(String s : output)  {
					sb.append(s);
					sb.append(" -> ");
				}
				if(sb.length() > 0) sb.delete(sb.length()-4, sb.length());
				System.out.println(sb);
			}
			//use the GUI
			else if(args.length == 3 && args[2].equals("GUI")) {
				new SimGUI(TopologicalSort.getGraph(args[0]), args[1]);
			}
			else {
				System.out.println(usage);
			}
		}
		catch(IOException e) {
			System.out.println("Invalid file given: "+e.toString()+"\n" + usage);
		}
		catch(IllegalArgumentException e) {
			System.out.println("Illegal argument given: "+e.toString());
		}
	}
}