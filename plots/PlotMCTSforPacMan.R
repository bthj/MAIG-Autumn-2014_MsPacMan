setwd("/home/bthj/ITU/ModernAI/eclipseProjects/MAIG-Autumn-2014_MsPacMan/plots")

# MCTSscore <- read.table("../data/MCTSforPacMan__DefaultPolicy__iterationLimitTrials.tab")
MCTSscore <- read.table("../data/MCTSforPacMan__GeneticControllerSimulation__iterationLimitTrials.tab")

playoutIterationLimit = MCTSscore$V1
score = MCTSscore$V2

# plot( playoutIterationLimit, score, type='l', main="MCTS PacMan score VS simulation iteration limits", sub="PacMan performs random available moves during playouts.  Zero limit = unlimited" )
plot( playoutIterationLimit, score, type='l', main="MCTS PacMan score VS simulation iteration limits", sub="PacMan uses a genetically evolved controller for moves during playouts.  Zero limit = unlimited" )
