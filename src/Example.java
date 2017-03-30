import java.util.ArrayList;
import java.util.List;
/*
 * save an instance of dataset
 */
public class Example {

	protected List<Double> instance = new ArrayList<Double>();
	protected String label;
	
	public Example(){
	}
	
	/*
	 * Constructor, save a new instance
	 */
	public Example(String[] s){
		for (int i = 0; i < s.length-1; i++){
			instance.add(Double.parseDouble(s[i]));
		}
		label = s[s.length-1];
	}
	
	/*
	 * return the label of this instance
	 */
	protected String getLabel(){
		return label;
	}
	
	/*
	 * return the value with specific index of this instance
	 */
	protected double getValueOfOneCol(int index){
		return instance.get(index);
	}
	
	/*
	 * return all feature values of this instance
	 */
	protected List<Double> getInstance(){
		return instance;
	}
	
	
	
}
