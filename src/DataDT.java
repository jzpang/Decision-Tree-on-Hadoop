import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class DataDT {
	public List<Example> data = new ArrayList<Example>();
	public String label;

	/*
	 * add a new instance.
	 */
	public void add(Example neweg) {
		data.add(neweg);
	}
	/*
	 * return the label column (class) of all instances.
	 */
	public List<String> getLabels() {
		List<String> labels  = new ArrayList<String>();
		for (Example e : data) {
			labels.add(e.getLabel());
		}
		return labels;	
	}
	
	/*
	 * return an instance.
	 */
	public Example getOneInstance(int index){
		return data.get(index);
	}
	
	/*
	 * return a specific column of all instances.
	 */
	public List<Double> getOneCol(int index) {
		List<Double> oneCol = new ArrayList<Double>();
		for (Example e : data) {
			oneCol.add( e.getValueOfOneCol(index));	
		}
		return oneCol;
	}
	
	/*
	 * return the number of instances in data
	 */
	public int getNumofInstances() {
		return data.size();
	}
	
	/*
	 * return the number of attributes(features) of data
	 */
	public int getNumofFeatures() {
		int size = 0;
		size = data.get(1).getInstance().size();
		return size;
	}
	
	/*
	 * return the number of unique classes of all instances
	 */
	public int getNumofUniqLabels() {
		List<String> labels = this.getLabels();
		return new HashSet<>(labels).size();
	}

	List<String> classes= Arrays.asList("Iris-setosa", "Iris-versicolor", "Iris-virginica");
	/*
	 * return the number of instances of each class in data, following the order hard-coded in classes.
	 */
	public int[] getNumofInstancesPerClass() {
		int[] numOfClasses = new int[classes.size()] ;
		List<String> labels = this.getLabels();
		for (String l: labels) {
			int classNum = classes.indexOf(l);
			numOfClasses[classNum]++;
		}
		return numOfClasses;
	}
	
}
