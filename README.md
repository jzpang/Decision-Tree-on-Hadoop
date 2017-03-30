# Decision-Tree-on-Hadoop

Author: Jingzhi Pang
=======Function============
This project is to build a fully functional decision tree based on the ID3 algorithm. The tree should grow to maximum size without prunning, and evaluated with 10-fold cross-validation. The split strategy for continuous attributes is based on median point.

=======Running Code========

1. navigate to src folder
2. run the following command:
	javac DtDriver.java 
	java DtDriver train.csv test.csv 

Note: train.csv and test.csv file are in data/ folder, otherwise you can change them to the path of your train and test file.


Code can also be imported into eclipse

The code will load the train and test data set into a data structure.
The trained data will feed into the DecisionTree constructor to build the decision tree.
The decision tree will be printed to “tree.txt” file with DecisionTree.printTree() method. 
The training data is also needed into CrossValidation class for 10-fold cross-validation. Average accuracy and confusion matrix will be printed to “cross_validation.txt”.
The testing data will feed into the DecisionTree.predict() method and then print accuracy and confusion matrix for the prediction to a file “accuracy.txt”

=======Source Files=========

1. DtDriver.java
	— The main class for running decision tree project. The main task is to build up a decision tree based train data set, do 10-fold cross validation, and predict on test dataset. It saves the train and test data to data structure.
2. DecisionTree
	- Class builds the decision tree and do prediction for test dataset. It contains methods for decision tree building, printing, and predicting, as well as necessary calculation methods such as info gain.
3. TreeNode.java
	- Data Structure holds a node in decision tree, containing data split information, including split feature index, value, left child and right child. 
4. DataDT
	- Data matrix for holding instances of dataset, with basic data structure Example.
5. Example.java
	- Data structure holds a instance of dataset.
6. CrossValidation.java
	- Class performed k-fold cross-validation on train data set. 
