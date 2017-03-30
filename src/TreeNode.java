public class TreeNode {
	
	public String attributes[] = {"sepal length in cm", "sepal width in cm", "petal length in cm", "petal width in cm"};
	
	protected DataDT data;
	protected int index=0;
	protected double value=0;
	protected boolean setSplit = false;
	
	protected int[] numPerClass = new int[3];
	
	protected TreeNode left=null;
	protected TreeNode right =null;
	
	/*
	 * Constructor of treeNode, with one param train, used for saving information when node is a leaf
	 */
	public TreeNode(DataDT train) {
		data = train;
		numPerClass = data.getNumofInstancesPerClass();
	}
	
	/*
	 * Constructor of treeNode, with three params, used when node is not a leaf and need deeper split
	 */
	public TreeNode(DataDT train, int index, double value) {
		data = train;
		this.index = index;
		this.value = value;
		this.setSplit = true;
		numPerClass = data.getNumofInstancesPerClass();
	}
	
	/*
	 * set the left child for current node
	 */
	public void setLeftChild(TreeNode node){
		this.left = node;
	}
	
	/*
	 * set the right child for current node
	 */
	public void setRightChild(TreeNode node){
		this.right = node;
	}
	
	/* 
	 * return left child for current node
	 */
	public TreeNode getLeftChild(){
		return this.left;
	}
	
	/*
	 * return right child for current node
	 */
	public TreeNode getRightChild(){
		return this.right;
	}
	
	/*
	 * return the feature(attribute) name for split
	 */
	public String getSplitFeatureName(){
		return attributes[index];
	}
	
	/*
	 * return the feature index for split
	 */
	public int getSplitFeatureIndex(){
		return index;
	}
	
	/*
	 * return the split value(median) of split feature
	 */
	public double getSplitValue(){
		return value;
	}
	
	/*
	 * return the number of instances in each class
	 */
	public int[] getNumPerClass(){
		return numPerClass;
	}
	
	/*
	 * return the percentage of each class
	 */
	public double[] getPercentPerClass(){
		double[] percent = new double[numPerClass.length];
		int total = data.getNumofInstances();
		for (int i=0; i< numPerClass.length; i++){
			percent[i] = (double)numPerClass[i] / (double)total;
		}
		return percent;
	}
	
	/*
	 * return the majority of classes of all instances
	 */
	public int getClassifiedIndex(){
		int max=0;
		int index=0;
		for (int i=0; i< numPerClass.length; i++){
			if (numPerClass[i] >= max){
				max = numPerClass[i];
				index=i;
			}
		}
		return index;
	}
}
