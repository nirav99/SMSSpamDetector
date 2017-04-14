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
The class counts and the conditional probabilities are written in the output file.

It also runs the validator on the validation data set and calculates the evaluation parameters.
The conditional probabilities are availadle at data/priorcounts.json .

Results
-------

TODO.

Data set
--------

Used the spam/ham dataset from [UCI](https://archive.ics.uci.edu/ml/datasets/SMS+Spam+Collection)

