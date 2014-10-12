setwd("/home/bthj/ITU/ModernAI/eclipseProjects/MAIG-Autumn-2014_MsPacMan/plots")

# geneticAlgorithmRunOutput <- read.table("../data/geneticAlgorithmRunOutput__mutate.05.tab")
geneticAlgorithmRunOutput <- read.table("../data/geneticAlgorithmRunOutput__mutate.125.tab")

generation = geneticAlgorithmRunOutput$V1
averageFitness = geneticAlgorithmRunOutput$V2

plot( generation, averageFitness, type='l' )