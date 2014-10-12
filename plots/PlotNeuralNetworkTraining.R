setwd("/home/bthj/ITU/ModernAI/eclipseProjects/MAIG-Autumn-2014_MsPacMan/plots")

# neuralNetworkTrainingOutput <- read.table("../data/feedforwardBackpropagationTraining__hiddenNeuronsLessThanTwiceInputSize.tab")
# neuralNetworkTrainingOutput <- read.table("../data/feedforwardBackpropagationTraining__learningRateFixedAt0dot1.tab")
# neuralNetworkTrainingOutput <- read.table("../data/feedforwardBackpropagationTraining__learningRateDownEvery100thEpoch.tab")
# neuralNetworkTrainingOutput <- read.table("../data/feedforwardBackpropagationTraining__learningRateFixedAt0dot1__alternativeInputSet.tab")
# neuralNetworkTrainingOutput <- read.table("../data/feedforwardBackpropagationTraining__learningRateDownEvery100thEpoch__alternativeInputSet.tab")
# neuralNetworkTrainingOutput <- read.table("../data/feedforwardBackpropagationTraining__learningRateDownEvery100thEpoch__alternativeInputSet__hiddenNeuronsLessThanTwiceInputSize.tab")
neuralNetworkTrainingOutput <- read.table("../data/feedforwardBackpropagationTraining__hiddenNeuronsLessThanTwiceInputSize__alternativeInputSet.tab")

epoch = neuralNetworkTrainingOutput$V1
errorRate = neuralNetworkTrainingOutput$V2
learningRate = neuralNetworkTrainingOutput$V3

# plot( epoch, errorRate, type='l', main="ANN error rate during training epochs", sub="Input set 1.  Fixed learning rate at 0.1.  Hidden neurons = input neurons * 1.7" )
# plot( epoch, errorRate, type='l', main="ANN error rate during training epochs", sub="Input set 1.  Fixed learning rate at 0.1.  Hidden neurons = input neurons * (2/3) + output neurons" )
# plot( epoch, errorRate, type='l', main="ANN error rate during training epochs", sub="Input set 1.  Learning rate 1/t (every 100th epoch).  Hidden neurons = input neurons * (2/3) + output neurons" )
# plot( epoch, errorRate, type='l', main="ANN error rate during training epochs", sub="Input set 2.  Fixed learning rate at 0.1.  Hidden neurons = input neurons * (2/3) + output neurons" )
# plot( epoch, errorRate, type='l', main="ANN error rate during training epochs", sub="Input set 2.  Learning rate 1/t (every 100th epoch).  Hidden neurons = input neurons * (2/3) + output neurons" )
# plot( epoch, errorRate, type='l', main="ANN error rate during training epochs", sub="Input set 2.  Learning rate 1/t (every 100th epoch).  Hidden neurons = input neurons * 1.7" )
plot( epoch, errorRate, type='l', main="ANN error rate during training epochs", sub="Input set 2.  Fixed learning rate at 0.1.  Hidden neurons = input neurons * 1.7" )

# plot( epoch, errorRate, type="l", col="red" )
# lines( epoch, learningRate, col="green")
#plot( epoch, learningRate, type="l", col="red" )
