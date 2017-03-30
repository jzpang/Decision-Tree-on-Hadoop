import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrossValidation {

	String classes[] = {"Iris-setosa", "Iris-versicolor", "Iris-virginica"};
	
	DataDT data = new DataDT();
	int k_folds = 0;
	
	
	public CrossValidation(){
	}
	/*
	 * Constructor of cross-validation, with params mydata and k_folds.
	 */
	public CrossValidation(DataDT mydata, int k){
		data = mydata;
		k_folds = k;
	}
	
	/*
	 * perform k-folds cross-validation
	 */
	public void analysis(){
		//shuffle data
		DataDT shuffledData = shuffle(data);
		
		List<Double> accuList = new ArrayList<Double>();
		List<int[][]> confMatList = new ArrayList<int[][]>();
		
		//k-fold cross-validation
		int oneFoldSize = shuffledData.getNumofInstances() / k_folds;
		for(int count = 0; count <k_folds; count++){
			//for every fold, split data and build decision tree based on training set
			DataDT[] train_test= new DataDT[2];
			train_test = splitData(shuffledData, k_folds, count, oneFoldSize);
			DecisionTree dt = new DecisionTree(train_test[0]);
				
			//calculate the accuracy and confusion matrix and save them in two arraylists
			List<ArrayList<Integer>> pred_true = new ArrayList<ArrayList<Integer>>();
			pred_true = dt.getPredAndTrueLabels(train_test[1]);
			int[][] confMat = new int[classes.length][classes.length];
			confMat = dt.calConfusion(pred_true.get(0), pred_true.get(1));
			double accu = dt.calAccuracy(confMat, train_test[1].getNumofInstances());
			
			accuList.add(accu);
			confMatList.add(confMat);
		}
		//do error analysis based on accuracy list and confusion matrix list of k folds
		errorAnalysis(accuList, confMatList);
		
	}

	/*
	 * calculate the average accuracy and average confusion matrix(showed in ratio)
	 */
	private void errorAnalysis(List<Double> accuList, List<int[][]> confMatList) {
		//calculate average accuracy
		Double avgAccu = accuList.stream().mapToDouble(val -> val).average().getAsDouble();
		int[][] sum= new int[classes.length][classes.length];
		for (int i=0; i< confMatList.size(); i++){
			int[][] temp = confMatList.get(i);
			for (int m=0; m< temp.length; m++){
				for (int n=0; n<temp[m].length; n++){
					sum[m][n] += temp[m][n];
				}
			}
		}
		//calculate average confusion matrix(ratio)
		double[][] avgConf = new double[classes.length][classes.length];
		double[] sumTrueLabel = new double[classes.length];
		
		for (int m=0; m< sum.length; m++){
			for (int n=0; n<sum[m].length; n++){
				avgConf[m][n] = (double) sum[m][n] / confMatList.size();
				sumTrueLabel[n] += avgConf[m][n];
			}
		}
		
		for (int m=0; m< sum.length; m++){
			for (int n=0; n<sum[m].length; n++){
				avgConf[m][n] = avgConf[m][n] /sumTrueLabel[n];
			}
		}
		writeAccu(avgAccu, avgConf);
		
	}

	
	/*
	 * write the accuracy and confusion matrix to cross_validation.txt
	 */
	private void writeAccu(double accu, double[][] conf) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("cross_validation.txt");
			writer.println("Accuracy: " + accu);
			
			writer.println("\n\tConfusion Matrix");
			writer.println("\t\tTrue Label");
			writer.println("Prediction\t"+ String.join("\t", classes));
			
			DecimalFormat df = new DecimalFormat("#.00"); 
			for(int i=0; i<conf.length; i++){
				StringBuilder s = new StringBuilder();
				s.append(classes[i]);
				for (int j=0; j<conf[i].length; j++){
					s.append("\t");
					s.append(df.format(conf[i][j]));
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
	 * split whole train dataset to training set and validation set
	 */
	private DataDT[] splitData(DataDT data, int k_folds, int count, int oneFoldSize) {
		int start = count * oneFoldSize;
		int end = (count+1) * oneFoldSize;
		DataDT test = new DataDT();
		DataDT train = new DataDT();
		DataDT[] train_test = new DataDT[2];
		if (count == k_folds - 1){
			end = data.getNumofInstances();
		}
		for (int i=0; i<data.getNumofInstances(); i++){
			if ( i>= start && i < end){
				test.add(data.getOneInstance(i));
			} else {
				train.add(data.getOneInstance(i));
			}
		}
		train_test[0] = train;
		train_test[1]= test;
		
		return train_test;
	}

	/*
	 * shuffle the training dataset
	 */
	private DataDT shuffle(DataDT data) {
		DataDT newData = new DataDT(); 
		int max = data.getNumofInstances();
		List<Integer> index = new ArrayList<Integer>();
		
		for(int i=0; i<max; i++){
			index.add(i);
		}
		Collections.shuffle(index);
		
		for(int i=0; i<index.size(); i++){
			newData.add(data.getOneInstance(index.get(i)));
		}
		return newData;
	}
	
}
