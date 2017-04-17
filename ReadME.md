SMS Spam Detector
=================

A classifier to classify SMS messages to spam / non-spam.

It is based on Bernoulli Naive Bayes classifier.

Building the project
--------------------

It is built using gradle. The command is

```cmd
gradle all
```

The jar files are created in the jar directory.

Feature Selection
-----------------

Identified relevant words (features) using mutual information analysis on the data set.

To identify relevant words use the command

```cmd
cd jar
java -jar SMSSpamFeatureFinder.jar {PathToLabledDataSet}s
```

This tool lists out 50 words or word types that have the highest mutual information in the given data.

Training The Model
------------------

To train the model, execute the command

```cmd
java -jar SMSSpamTrainer.jar {PathToLabeledDataSet} {OutputFile}
```
It splits the labeled data set in training and validation data sets. About 20% data is used as validation data set.
The class counts and the conditional probabilities are written in the output file. It uses the features identified from
mutual information analysis.

It also runs the validator on the validation data set and calculates the evaluation parameters.
The conditional probabilities are availadle at data/priorcounts.json .

To quickly run the trainer and see the evaluation results, run the command

```cmd
cd jar
java -jar SMSSpamTrainer.jar ../TrainingData/SMSSpamCollection.tsv ../data/priorcounts.json
```

Results
-------

Sample evaluation on a validation data set is

```cmd
Overall accuracy : 
Total records : 1018
Total true positives : 126
Total false positives : 1
Total false negatives : 13
Precision : 0.9921
Recall : 0.9065
fscore : 0.9474
```

These numbers differ based on different validation data sets.

Some sample messages classified as spam and non-spam are :

Sample spam messages:
```cmd
This is the 2nd time we have tried 2 contact u. U have won the £750 Pound prize. 2 claim is easy, call 087187272008 NOW1! Only 10p per minute. BT-national-rate.

u r subscribed 2 TEXTCOMP 250 wkly comp. 1st wk?s free question follows, subsequent wks charged@150p/msg.2 unsubscribe txt STOP 2 84128,custcare 08712405020
```
Sample Non-spam messages:

```cmd
Sorry, I'll call later

When you guys planning on coming over?
```

Predicting Class Of New Messages
--------------------------------

After the model is trained, SMSSPAMPredictor.jar can be used to determine if the new messages are spam or not-spam.

Say, the messages to be checked are available in a file named testdata.txt in the data directory, then one can use the
following command to identify the spam messages :

```cmd
java -jar SMSSpamPredictor.jar {modelfile} ./data/testdata.txt
```

Where the modelFile is the outputFile produced by SMSSpamTrainer.


Data set
--------

Used the spam/ham dataset from [UCI](https://archive.ics.uci.edu/ml/datasets/SMS+Spam+Collection)

