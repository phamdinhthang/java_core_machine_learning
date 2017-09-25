import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.neighbors import KNeighborsClassifier
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.metrics import confusion_matrix
from sklearn.manifold import TSNE
from sklearn.metrics import classification_report
from sklearn.cluster import KMeans

#Import and inspection
file_path = '/Users/phamdinhthang/Desktop/iris.csv'
iris = pd.read_csv(file_path,header=0,sep=',')
iris = iris.sample(frac=1).reset_index(drop=True)
print(iris.head())
print(iris.info())
print(iris.describe())

#Visualisation exploratory data analysis
sns.set()
plt.title('Sepal length and Width histogram')
plt.hist(iris['sepal_length'], bins = 30, alpha=0.5, label='sepal_length')
plt.hist(iris['sepal_width'], bins = 30, alpha=0.5, label='sepal_width')
plt.legend(loc='upper right')
plt.xlabel('(cm)')
plt.ylabel('Observations')
plt.show()

plt.title('Petal length and Width histogram')
plt.hist(iris['petal_length'], bins = 30, alpha=0.5, label='petal_length')
plt.hist(iris['petal_width'], bins = 30, alpha=0.5, label='petal_width')
plt.legend(loc='upper right')
plt.xlabel('(cm)')
plt.ylabel('Observations')
plt.show()

plt.title('Boxplot by features name')
iris.boxplot(['sepal_length', 'sepal_width', 'petal_length','petal_width'])
plt.xlabel('Features')
plt.ylabel('Dimension (cm)')
plt.show()

plt.title('Boxplot by species name')
sns.boxplot(x='species',y='petal_length',data=iris)
plt.xlabel('Species')
plt.ylabel('Petal length (cm)')
plt.show()

#2D Scatter and linear model fitting
iris_features = iris.drop('species',axis=1).values
iris_species = iris['species'].values
sepal_length_arr = iris_features[:,0].reshape(-1,1)
petal_length_arr = iris_features[:,2].reshape(-1,1)
prediction_space = np.linspace(min(iris['sepal_length']), max(iris['sepal_length'])).reshape(-1,1)

reg = LinearRegression()
reg.fit(sepal_length_arr,petal_length_arr)
petal_length_predict = reg.predict(prediction_space)

plt.title('Sepal length vs Petal length')
plt.xlabel('Sepal length (cm)')
plt.ylabel('Petal length (cm)')
plt.scatter(iris['sepal_length'],iris['petal_length'])
plt.plot(prediction_space, petal_length_predict, color='black', linewidth=2)
plt.show()

#Multivariable visualisation with dimensional reduction
tsne = TSNE(learning_rate=100)
iris_transformed = tsne.fit_transform(iris_features)
xs = iris_transformed[:,0]
ys = iris_transformed[:,1]
iris_color = iris['species']
iris_color.replace('setosa',0,inplace=True)
iris_color.replace('virginica',1,inplace=True)
iris_color.replace('versicolor',2,inplace=True)
plt.scatter(xs,ys,c=iris_color.values)
plt.title('4 features reduce to 2 features visualisation')
plt.xlabel('Feature 1')
plt.ylabel('Feature 2')
plt.show()

#Classification with kNN
iris = pd.read_csv(file_path)
iris = iris.sample(frac=1).reset_index(drop=True)
features = iris.drop('species',axis=1).values
labels = iris['species'].values
X_train,X_test,y_train,y_test = train_test_split(features,labels,test_size = 0.3, random_state=42,stratify=labels)
knn_model = KNeighborsClassifier(n_neighbors=5)
knn_model.fit(X_train,y_train)

predicted = knn_model.predict(X_test)
correct = y_test == predicted
compare = pd.DataFrame({"label":y_test,"predicted":predicted,"correct":correct})
print('Accuracy = ',correct.sum()/(0.3*features.shape[0]))
print(compare.head(20))
print(confusion_matrix(y_test, predicted))
print(classification_report(y_test, predicted))


#Clustering with kMeans
kmeans = KMeans(n_clusters=3)
kmeans.fit(features)
groups = kmeans.predict(features)
compare = pd.DataFrame({'predicted':groups,'species':labels})
##compare.sort_values(['species'],ascending=[True],inplace=True)
print(compare.head(20))
crosstabular = pd.crosstab(compare['predicted'],compare['species'])
print(crosstabular)

#Classification with Deep learning
##path = '/Users/phamdinhthang/Dropbox/Python_DataScience_Tracks/Summary/dataset/iris.csv'
##iris = pd.read_csv(path)
##iris_features = iris.drop('species',axis=1).values
##iris_target = iris['species']
##iris_target.replace('setosa',0,inplace=True)
##iris_target.replace('versicolor',1,inplace=True)
##iris_target.replace('virginica',2,inplace=True)
##n_features = iris_features.shape[1]
##target = to_categorical(iris_target.values)
##
##model = Sequential()
##model.add(Dense(100,activation='relu',input_shape=(n_features,)))
##model.add(Dense(100,activation='relu'))
##model.add(Dense(3,activation='softmax'))
##model.compile(optimizer='sgd',loss='categorical_crossentropy',metrics = ['accuracy'])
##model.fit(iris_features,target)
##iris_pred = model.predict(iris_features)
