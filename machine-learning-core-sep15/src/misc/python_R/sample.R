library(ggplot2)
library(class)
library(caret)
library(dplyr)
library(mlbench)

#Import and inspection
iris <- read.csv("/Users/phamdinhthang/Desktop/iris.csv", stringsAsFactors = FALSE)
rows <- sample(nrow(iris)) #create random row index
iris <- iris[rows,]
head(iris)
str(iris)

#Visualisation exploratory data analysis
ggplot(data = iris) + geom_histogram(aes(x = sepal_length), alpha=0.3, fill ="red",binwidth=0.2,position="dodge") + geom_histogram(aes(x = petal_length),alpha=0.3,, fill ="green",binwidth=0.2,position="dodge")

ggplot(data=iris, aes(x=species, y= petal_length)) + geom_boxplot()
ggplot(data=iris, aes(x=species, y= sepal_length)) + geom_boxplot()

#2D Scatter and linear model fitting
model <- lm(formula = petal_length ~ cbind(sepal_length,sepal_width), data = iris)
predicted_petal_length <- predict(model, newdata =  iris)
ggplot(iris, aes(x=sepal_length, y= petal_length)) + geom_point(shape=1,colour = 'black',size=3) + geom_point(aes(y= predicted_petal_length, colour = 'red',size=5))  + geom_smooth(method=lm)

#Multivariable visualisation with dimensional reduction
pca_model <- prcomp(iris[,1:4], scale = FALSE, center = TRUE)
summary(pca_model)
df <- as.data.frame(pca_model$x[,1:2])
df$label <- iris$species
ggplot(data = df,aes(x=PC1,y=PC2,color = label)) + geom_point()


#Classification with kNN
split <- round(nrow(iris)*0.7)
train_features = iris[1:split,][,1:4]
train_label = iris[1:split,][,5]
test_features = iris[(split+1):nrow(iris),][,1:4]
test_label = iris[(split+1):nrow(iris),][,5]
predicted_label<-knn(train= train_features, test= test_features,cl= train_label,k=5)
result = data.frame(test_label, predicted_label)
head(result,n=20)
confusionMatrix(test_label, predicted_label)

#Clustering with kMeans
model <- kmeans(iris[,1:4],centers=3,nstart=20)
iris$predicted_group = model$cluster
iris = arrange(iris, species)
head(iris,n=60)