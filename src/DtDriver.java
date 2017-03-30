import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DtDriver {
	
	public static void main(String[] args){
		DataDT[] train_test = readDataDT(args);
		DataDT train = train_test[0];
		DataDT test = train_test[1];
		//build decision tree
		DecisionTree decisionTree = new DecisionTree(train);
		//print the tree
		decisionTree.printTree(decisionTree.getRoot());
		
		//use decision tree to predict the test samples
		decisionTree.predict(test);
		
		//10-folds cross-validation
		CrossValidation cv = new CrossValidation(train, 10);
		cv.analysis();
	}
	
	/*
	 * Read train and test file, save all data to data structure DataDT.
	 */
	public static DataDT[] readDataDT(String[] s) {
		DataDT[] myData = new DataDT[2];
		DataDT trainData = new DataDT();
		DataDT testData = new DataDT();		
		try{
			BufferedReader trainFile = new BufferedReader(new FileReader(s[0]));
			BufferedReader testFile = new BufferedReader(new FileReader(s[1]));
		
			String line = "";
		
			while (( line = trainFile.readLine()) != null) {
				Example eg = new Example(line.split(","));
				trainData.add(eg);
			}
			trainFile.close();
			
			while (( line = testFile.readLine()) != null) {
				Example eg = new Example(line.split(","));
				testData.add(eg);
			}
			testFile.close();
			
		} catch (IOException e) {
				e.printStackTrace();
		}
		myData[0] = trainData;
		myData[1] = testData;
		return myData;
	}

}
