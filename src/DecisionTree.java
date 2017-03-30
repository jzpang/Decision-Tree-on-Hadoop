import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


public class DecisionTree {

	//hard-coded information
	String attributes[] = {"sepal length in cm", "sepal width in cm", "petal length in cm", "petal width in cm"};
	String classes[] = {"Iris-setosa", "Iris-versicolor", "Iris-virginica"};
	Map<String, Integer> classes_index = new HashMap<String, Integer>();
	

	TreeNode root;
	
	/*
	 * Constructor of decision tree
	 */
	public DecisionTree(DataDT train){
		//initialize a hashmap for classes, easy to get index of different class.
		classes_index=init_classesMap(classes);
		//build decision tree, return the root of tree.
		root = buildTree(train);
		
	}
	
	/*
	 * Build decision tree.
	 */
	public TreeNode buildTree(DataDT train) {
		TreeNode node = null;
		//If reached leaf, stop growing tree
		if (isEnd(train)) {
			if (train.getNumofInstances() == 0){
				return node;
			}
			else {
				node = new TreeNode(train);
				return node;
			}
		}
		
		//growing the tree
		else {
			//find the best feature to split, then split train data
			double[] splitIndexandValue = findSplitFeature(train);
			int featureIndex= (int) splitIndexandValue[0];
			double featureSplitValue = splitIndexandValue[1];
			
			// cannot find any feature with info gain >0, stop growing
			if(featureIndex ==-1){
				node = new TreeNode(train);
				return node;
			}
			
			DataDT[] data = new DataDT[2];
			data = splitData(train, featureIndex, featureSplitValue);
			
			
			//add information on current node
			node = new TreeNode(train, featureIndex, featureSplitValue);
			
			//use splited data, build two trees as two children of current node
			TreeNode left = buildTree(data[0]);
			TreeNode right = buildTree(data[1]);
			node.setLeftChild(left);
			node.setRightChild(right);
			
		}
		return node;
	}
	
	/*
	 * split train data based on best feature and its median value
	 */
	private DataDT[] splitData(DataDT train, int featureIndex, double featureSplitValue) {
		DataDT[] myData = new DataDT[2];
		DataDT data1 = new DataDT();
		DataDT data2 = new DataDT(); 
		for (int index=0; index< train.getNumofInstances(); index++){
			if (train.getOneInstance(index).getValueOfOneCol(featureIndex) <= featureSplitValue) {
				data1.add(train.getOneInstance(index));
			} else {
				data2.add(train.getOneInstance(index));
			}
		}
		myData[0] = data1;
		myData[1] = data2;
		return myData;
	}

	/*
	 * find the feature with max info gain as best split feature, use its median as best split value
	 * return the best feature's index and median value
	 */
	private double[] findSplitFeature(DataDT train) {
		List<Double> gainList = new ArrayList<Double>();
		List<Double> med = new ArrayList<Double>();
		List<String> labels = train.getLabels();
		int[] numPerClass = train.getNumofInstancesPerClass();
		//calculate info gain and median for each feature
		for (int index = 0; index < train.getNumofFeatures(); index++) {
			List<Double> col= train.getOneCol(index);
			List<Double> col_copy= new ArrayList<Double>(col);
			
			double median = findMiddlePoint(col_copy);
			List<int[]> mat = new ArrayList<int[]>();
			mat = calcuMatrix(col, labels, median);
			double gain = infoGain(mat, numPerClass);
			gainList.add(gain);
			med.add(median);
		}

		int bestFeatureIndex = 0;
		double bestSplitValue =0d;
		
		double totalGain = gainList.stream().mapToDouble(f -> f.doubleValue()).sum();
		
		if (totalGain ==0){
			//cannot find any feature to split data
			bestFeatureIndex = -1;
		}
		else{
			//find the feature with max info gain
			bestFeatureIndex = gainList.indexOf( Collections.max(gainList));
			bestSplitValue = med.get(bestFeatureIndex);
		}
		double[] indexAndValue = new double[2];
		indexAndValue[0] = (double) bestFeatureIndex;
		indexAndValue[1] = bestSplitValue;
		return indexAndValue;
	}

	/*
	 * return the matrix used for calculating info gain given values of a feature
	 */
	private List<int[]> calcuMatrix(List<Double> col, List<String> labels, double median) {
		//return a 2*3 matrix, 2 rows for less or bigger than median, 3 columns for different classes

		
		List<int[]> list = new ArrayList<int[]>();
		int[] leftMedian = new int[classes.length];
		int[] rightMedian = new int[classes.length];
		for (int i=0; i<col.size(); i++){
			String label=labels.get(i);
			int index = classes_index.get(label);
			
			if (col.get(i) <= median) {
				leftMedian[index]++;
			} else{
				rightMedian[index]++;
			}
		}
		list.add(leftMedian);
		list.add(rightMedian);
		return list;
	}
	
	/*
	 * calculate info gain
	 */
	private double infoGain(List<int[]> mat, int[] numPerClass) {
		double entropy1= entropy(numPerClass);
		double leftsum =  IntStream.of(mat.get(0)).sum();
		double rightsum =  IntStream.of(mat.get(1)).sum();
		double total = leftsum + rightsum;
		double entropy2 = 0d;
		if (total != 0d){
			 entropy2 = (leftsum/total)* entropy(mat.get(0)) + (rightsum/total)* entropy(mat.get(1));
		}

		double info = entropy1 - entropy2;
		return info;
	}

	/*
	 * calculate entropy
	 */
	private double entropy(int[] num){
		double sum = IntStream.of(num).sum();
		double entropy = 0d;
		if (sum == 0d){
			return entropy;
		}
		for(int i : num) {
			entropy = entropy+(-(i/sum)*log2(i/sum));
		}
		return entropy;
	}

	private double log2(double d) {
		return (d==0) ? 0 : Math.log(d)/Math.log(2);
	}


	/*
	 * find median value of a feature values
	 */
	private double findMiddlePoint(List<Double> col) {
		Collections.sort(col);
		int middle = col.size()/2;
		if (col.size()%2 ==1){
			return col.get(middle);
		} else{
			return (col.get(middle)+col.get(middle-1))/2;
		}
		
	}

	/*
	 * return the root node of the decision tree.
	 */
	public TreeNode getRoot(){
		return root;
	}
	
	
	/*
	 * write the decision tree to tree.txt
	 */
	public PrintWriter writeTree;
	public void printTree(TreeNode node){
		try {
			writeTree = new PrintWriter("tree.txt");
			writeTree.println(treeStructure(node));
			writeTree.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	List<String> flag = new ArrayList<String>();
	StringBuilder tree = new StringBuilder();
	public String treeStructure(TreeNode node){
		
		//if node is not empty, print the data information it contained
		if (node !=null){
			tree.append(Arrays.toString(node.getNumPerClass()) );
			tree.append("\t");
			DecimalFormat df = new DecimalFormat("#.00"); 
			for (double d: node.getPercentPerClass()){
				tree.append(df.format(d));
				tree.append(" ");
			}
			tree.append("\n");
		}
		
		//if node is a leaf, stop
		if (node.getLeftChild()==null && node.getRightChild()==null){
			return "";
		} 
		//if node is not a leaf, print the information contains in both children
		else{
			//flag & listString are helper indicators to show the structure of tree
			flag.add("|");
			String listString = String.join("", flag);
			if (node.getLeftChild() != null){
				tree.append(listString + node.getSplitFeatureName() + " <= " + node.getSplitValue() +": ");
				treeStructure(node.getLeftChild());
			}
			if (node.getRightChild() != null){
				tree.append(listString + node.getSplitFeatureName() + " > " + node.getSplitValue() +": ");
				treeStructure(node.getRightChild());
				flag.remove(flag.size()-1);
			}
		}
		return tree.toString();
	}
	
	/*
	 * initialization of classes array to a hashmap, 
	 */
	private Map<String, Integer> init_classesMap(String[] s) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i=0; i<s.length; i++){
			//(key, value) = (class, index)
			map.put(s[i], i);
		}
		return map;
	}

	/*
	 * judge if tree reaches a leaf
	 */
	private boolean isEnd(DataDT data) {
		
		if(data.getNumofInstances() ==0) return true;
		if (data.getNumofUniqLabels() <= 1){
			return true;
		} else {
			return false;
		}
	}

	/*
	 * prediction for test data
	 */
	public void predict(DataDT test) {
		
		List<ArrayList<Integer>> pred_true = new ArrayList<ArrayList<Integer>>();
		
		//3*3 confusion matrix, 3 rows for prediction, 3 columns for true labels
		int[][] confusionMatrix = new int[classes.length][classes.length];
		
		//record the prediction and true labels for all test samples
		pred_true = getPredAndTrueLabels(test);
		
		//compute the confusion matrix and accuracy for test data
		confusionMatrix = calConfusion(pred_true.get(0), pred_true.get(1));
		double accu = calAccuracy(confusionMatrix, test.getNumofInstances());
		
		writeAccu(accu, confusionMatrix);
		
	}
	
	/*
	 * return the prediction and true labels for all test samples
	 */
	public List<ArrayList<Integer>> getPredAndTrueLabels(DataDT test) {
		List<Integer> predIndex = new ArrayList<Integer>();
		List<Integer> trueLabel = new ArrayList<Integer>();
		List<ArrayList<Integer>> pred_true = new ArrayList<ArrayList<Integer>>();
		for(int i=0; i<test.getNumofInstances(); i++){
			Example e = test.getOneInstance(i);
			int pred = predictExample(this.root, e);
			predIndex.add(pred);
			trueLabel.add(classes_index.get(e.getLabel()));
		}
		pred_true.add((ArrayList<Integer>) predIndex);
		pred_true.add((ArrayList<Integer>) trueLabel);
		
		return pred_true;
	}

	/*
	 * write the accuracy and confusion matrix to accuracy.txt
	 */
	private void writeAccu(double accu, int[][] confusionMatrix) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("accuracy.txt");
			writer.println("Accuracy: " + accu);
			
			writer.println("\n\tConfusion Matrix");
			writer.println("\t\tTrue Label");
			writer.println("Prediction\t"+ String.join("\t", classes));
			
			for(int i=0; i<confusionMatrix.length; i++){
				StringBuilder s = new StringBuilder();
				s.append(classes[i]);
				for (int j=0; j<confusionMatrix[i].length; j++){
					s.append("\t");
					s.append(Integer.toString(confusionMatrix[i][j]));
					s.append("\t");
				}
				writer.println(s);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	/*
	 * calculate the accuracy
	 */
	public double calAccuracy(int[][] confusionMatrix, int num) {
		double accuracy = 0d;
		double truePred = 0d;
		for(int i=0; i<confusionMatrix.length; i++){
			truePred += confusionMatrix[i][i];
		}
		accuracy =  truePred/num;
		return accuracy;
	}

	/*
	 * calculate the confusion matrix, rows for predicted labels, columns for true labels
	 */
	public int[][] calConfusion(List<Integer> predList, List<Integer> labelList) {
		int[][] mat = new int[classes.length][classes.length];
		for(int i=0; i<predList.size(); i++){
			int pred = predList.get(i);
			int label = labelList.get(i);
			if(pred == label){
				mat[label][label]++;
			} else{
				mat[pred][label]++;
			}
		}
		return mat;
	}
	
	/*
	 * predict the label for one example
	 */
	int predLabelIndex = 0;
	private int predictExample(TreeNode node, Example e) {
		
		if( node.getLeftChild()==null && node.getRightChild()==null){
			predLabelIndex = node.getClassifiedIndex();
			return predLabelIndex;
		}
		else{
			int splitIndex = node.getSplitFeatureIndex();
			double splitValue = node.getSplitValue();
			if( e.getValueOfOneCol(splitIndex) <= splitValue ){
				predictExample(node.getLeftChild(), e);
			}
			else{
				predictExample(node.getRightChild(), e);
			}
		}
		return predLabelIndex;
		
	}

	
}
