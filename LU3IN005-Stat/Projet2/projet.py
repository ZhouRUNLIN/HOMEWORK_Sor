import numpy as np
import pandas as pd
import utils
import math
import matplotlib.pyplot as plt
from scipy.stats import chi2_contingency

#Nous mettons quelques fonctions au début du fichier et le code du classificateur à la fin.

#pour Question 1
def getPrior(data):
    N = 0
    N1 = 0
    for i in data['target']:        #parcours les fichers de .csv pour compter les valeurs
        N += 1
        if i == 1:
            N1 += 1

    estimation = N1/N           #la valeur de p
    variance = math.sqrt(estimation*(1 - estimation)/N) 
    
    #l'intervale de confiance à 95% est [-1.96, 1.96]
    min5pourcent = estimation - 1.96*variance
    max5pourcent = estimation + 1.96*variance

    return{"estimation": estimation,"min5pourcent": min5pourcent, "max5pourcent": max5pourcent}

#pour Question 3a
def P2D_l(df,attr):
    """
    Calcule dans le dataframe(df) la probabilité P(attr|target) sous la forme:
        un dictionnaire asssociant à la valeur 't';
        un dictionnaire associant à la valeur 'a', la probabilité de P(attr=a|target=t) 
    """
    train0 = df.copy()
    dict1 = dict() #la création d'une dictionnaire vide pour sotcker la fréquence des clés dans le dataframe lorsque target=1
    trainP = train0.groupby("target").get_group(1)
    for line in trainP[attr]:
        if line not in dict1:
            dict1[line] = 1
        else:
            dict1[line] += 1
    for key in dict1:
        dict1[key] = (0.0 + dict1[key]) / trainP[attr].count()
    
    dict0 = dict() #la création d'une dictionnaire vide pour sotcker la fréquence des clés dans le dataframe lorsque target=0
    trainNP = train0.groupby("target").get_group(0)
    for line in trainNP[attr]:
        if line not in dict0:
            dict0[line] = 1
        else:
            dict0[line] += 1
    
    for key in dict0:
        dict0[key] = (0.0+dict0[key])/trainNP[attr].count() #calculer la probabilité conditionnelle
    return {1: dict1, 0: dict0}
    
#pour Question 3a
def P2D_p(df,attr):
    """
    Calcule dans le dataframe(df) la probabilité P(target|attr) sous la forme:
        un dictionnaire asssociant à la valeur 't';
        un dictionnaire associant à la valeur 'a', la probabilité de P(target=t|attr=a) 
    """
    train0 = df.copy()
    
    dict0 = dict() #la création d'une dictionnaire vide pour sotcker la fréquence des clés dans le dataframe lorsque target=0
    for line in train0[attr]:
        if line not in dict0:
            dict0[line] = 1
        else:
            dict0[line] += 1
    
    dict1 = dict() #la création d'une dictionnaire vide pour sotcker la fréquence des clés dans le dataframe lorsque target=1
    trainP = train0.groupby("target").get_group(1)
    for key in dict0:
        dict1[key] = 0
    for line in trainP[attr]:
        dict1[line] += 1
    
    dictP2Dp = dict()
    for key in dict0:
        dictP2Dp[key] = {1: (0.0+dict1[key]) / dict0[key],0:1.0 - (0.0+dict1[key]) / dict0[key]} #calculer la probabilité conditionelle
    
    return dictP2Dp

#pour Question 4.1
def nbParams(df,lAttr=None):
    """
        retourne la taille mémoire d'une table (df)
    (en supposant qu'un float est représenté sur 8octets)
    """
    if lAttr == None:
        lAttr = list(df.columns)
        
    nb=8    
    for attr in lAttr:
        nb *= df.groupby(attr).size().count()
    return nb

#pour Question 4.2
def nbParamsIndep(df,lAttr=None):
    """
        retourne la taille mémoire d'une table (df)
    en supposant qu'un float est représenté sur 8octets 
    et en supposant l'indépendance des variables
    """
    if lAttr == None:
        lAttr = list(df.columns)
    nb=0
    for attr in lAttr:
        nb += df.groupby(attr).size().count()
    return nb*8

#pour Question 5.3.1
def drawNaiveBayes(df,attr):
    """
        retourne le graphe du model Naive Bayes a partir 
    d'un dataframe et du nom de la colonne qui est la classe
    """
    str0 = ""
    for attr0 in list(df.columns):
        if attr0 != attr:
            str0 += attr + "->" + attr0 + ";"
    str0 = str0[0:-1]
    return utils.drawGraph(str0)

#pour Question 5.3.2
def nbParamsNaiveBayes(df,attr,lAttr=None):
    """
        retourne la taille mémoire d'une table (df)
    en supposant qu'un float est représenté sur 8octets 
    et en utilisant l'hypothèse du Naive Bayes.
    """
    nb=0
    if lAttr == None:
        lAttr = list(df.columns)

    for attr0 in lAttr:
        nb += df.groupby(attr0).size().count()

    nb = nb*df.groupby(attr).size().count()*8
    if nb == 0:
        nb = df.groupby(attr).size().count()*8
    else:
        nb -= df.groupby(attr).size().count()*8

    return nb

#pour Question 6 : feature selection dans le cadre du classifier naive bayes
def isIndepFromTarget(df, attr, x):
    table_sp = pd.crosstab(df[attr], df['target'])
    observed = chi2_contingency(np.array(table_sp))
    chi2, p, dof, ex = chi2_contingency(table_sp, correction=False)
    if p > x:
        return True
    return False

#pour Question 7.2
def mapClassifiers(dic,df):
    """
    Entree : 
        un dictionnaire 'dic' en format de  {nom:instance de classifier} 
        et d'un dataframe df
    Sotie :
        Représente graphiquement ces classifiers dans l'espace (precision, rappel)
    """
    lP=[]           #liste de coordonnées horizontales
    lR=[]           #liste de coordonnées verticales
    for key in dic:
        c=dic[key]
        if key in ["6","7"]:
            c.draw()
        p=c.statsOnDF(df)["Précision"]      #stocker precision dans x
        r=c.statsOnDF(df)["rappel"]         #stocker rappel dans y
        lP.append(p)
        lR.append(r)
        plt.text(p,r,key)
    plt.scatter(lP,lR)
    plt.show()
        
        

#pour Question 2
class APrioriClassifier(utils.AbstractClassifier):
    def __init__(self):
        pass

    #pour Question 2a
    def estimClass(self, attrs):
        """
        toujours retourne 1
            Raison : selon le résultat de question 1, la probabilité a priori de la classe 1 est plus huat que 70%,
            donc on peut penser que la majorité de gens est positive (égale à 1)
        """
        return 1

    #pour Question 2b
    def statsOnDF(self, df):
        """
        à partir d'un pandas.dataframe, calcule les taux d'erreurs de classification et rend un dictionnaire.

        Entrée 'df': un dataframe à tester
        Sortie : un dictionnaire incluant la valeur de VP, FP, VN, FN, précision et rappel
        """
        
        #Initialisation des variables
        VP=0
        VN=0
        FP=0
        FN=0
        
        for i in range(df.count()["target"]):
            #calculer la valeur des variables
            dictI = utils.getNthDict(df,i)
            tar = dictI["target"]
            pre = self.estimClass(dictI)
            if tar==1 and pre==1:
                VP += 1
            if tar==0 and pre==0:
                VN += 1
            if tar==0 and pre==1:
                FP += 1
            if tar==1 and pre==0:
                FN += 1
                
        return {"VP":VP,"VN":VN,"FP":FP,"FN":FN,"Précision":VP/(VP+FP),"rappel":VP/(VP+FN)}

#pour Question 3b
class ML2DClassifier(APrioriClassifier):
    def __init__(self,df,attr):
        self.df=df
        self.attr=attr
        self.dictAttr=P2D_l(df,attr)

    def estimClass(self, attrs):
        if self.dictAttr[1][attrs[self.attr]] > self.dictAttr[0][attrs[self.attr]]:
            return 1
        return 0

#pour Question 3c
class MAP2DClassifier(APrioriClassifier):
    def __init__(self,df,attr):
        self.df=df
        self.attr=attr
        self.dictAttr=P2D_p(df,attr)

    def estimClass(self, attrs):
        if self.dictAttr[attrs[self.attr]][1] > self.dictAttr[attrs[self.attr]][0]:
            return 1
        return 0

#pour Question 5.4
class MLNaiveBayesClassifier(APrioriClassifier):
    """
        p(a1, ... , an | t) = p(a1|t)* ... *p(an|t)
    """
    def __init__(self, df):
        self.data = df
        self.d = {}
        for i in df.keys():
            self.d[i] = P2D_l(self.data, i)

    def estimProbas(self, df):
        """
            retourne un dic en format de {0: p1, 1: p2}
                avec p1 est la probabilite de p(a1, ... , an|t) lorsque t = 0 et p2 est la probabilite de p(a1, ... , an|t) lorsque t = 1
        """

        prob = {0: 1, 1: 1}         #Initialisation de dic
        for i in {'age','sex','cp','trestbps','chol','fbs','restecg','thalach','exang','oldpeak','slope','ca','thal'}:
            prob_0 = prob[0]
            prob_1 = prob[1] 
            
            #si p(ai | t) exist, calculer le produit; sinon, on mettre a p(a1, ... , an|t) à 0
            if df[i] in self.d[i][1].keys():
                prob_1 *= self.d[i][1][df[i]]
            else:
                prob_1 = 0

            if df[i] in self.d[i][0].keys():
                prob_0 *= self.d[i][0][df[i]]
            else:
                prob_0 = 0

            prob = {0: prob_0, 1: prob_1}

        return prob
    
    def estimClass(self, attrs):
        d = self.estimProbas(attrs)

        if d[1] > d[0]:
            return 1
        else:
            return 0

class MAPNaiveBayesClassifier(APrioriClassifier):
    """
        p(t|a1, ..., an) = [p(t) * p(a1, ..., an|t)] / p(a1, ..., an)
                         = p(t) * Res_de_MLNaiveBayesClassifier / (p(a1) * ... *p(an))
    """
    def __init__(self,df):
        self.data = df
        self.d = {}
        for i in df.keys():
            self.d[i] = P2D_l(self.data, i)

    def probAi(self, attr_i):
        """
        retourne un dic de ai en format de {key1: p1, key2: p2, ..., keyn: pn}
            avec p1+...+pn = 1 et key1, ..., keyn sont les valeurs differents de attributs
        Par exemple : 
            attribut 'age' : {}
        """
        somme = 0
        liste = {}
        for i in self.data[attr_i]:
            somme += 1
            #si il y a une nouvelle valeur, on cree un autre key, sinon la valeur de key correspondant doit se augmenter a 1
            if i not in liste:
                liste[i] = 1
            else:
                liste[i] += 1
        
        #divise par N qui est la taille de echantillon (train)
        for j in liste: 
            res = liste[j] / somme
            liste[j] = res
        
        return liste
    
    def listeProbAi(self):
        """
        retourne un dic de tous les attributs 
            en format de {attr1 : res probAi(attr1), ..., attrn: res probAi(attrn)}
        """
        liste = {}
        for i in self.data:
            liste[i] = self.probAi(i)
        return liste
    
    def estimProbas(self, df):
        """
            retourne un dic en format de {0: p1, 1: p2}
                avec p1 est la probabilite de p(t|a1, ... , an) lorsque t = 0 et p2 est la probabilite de p(t|a1, ... , an) lorsque t = 1
        """
        #Initialisation
        prob_0 = 1 
        prob_1 = 1
        liste = self.listeProbAi()
        prob_T1 = liste['target'][1]
        prob_T0 = liste['target'][0]
        
        for i in {'age','sex','cp','trestbps','chol','fbs','restecg','thalach','exang','oldpeak','slope','ca','thal'}:           
            #si p(ai | t) exist, calculer le produit; sinon, on mettre a p(a1, ... , an|t) à 0
            if df[i] in self.d[i][1].keys():
                prob_1 *= self.d[i][1][df[i]]
            else:
                prob_1 = 0

            if df[i] in self.d[i][0].keys():
                prob_0 *= self.d[i][0][df[i]]
            else:
                prob_0 = 0
        
        if prob_0 == 0:
            return {0: 0.0, 1: 1.0}
        if prob_1 == 0:
            return {0: 1.0, 1: 0.0}
        else:
            prob_1 = 1/(1 + (prob_0*prob_T0)/(prob_1*prob_T1))
            prob_0 = 1-prob_1

            return {0: prob_0, 1: prob_1}

    def estimClass(self, attrs):
        d = self.estimProbas(attrs)

        if d[1] > d[0]:
            return 1
        else:
            return 0

#pour Question 6    
class ReducedMLNaiveBayesClassifier(APrioriClassifier):
    def __init__(self, df, param, attr='target'):
        self.df=df
        self.attr=attr
        self.dictAttr=P2D_p(df,attr)
        self.param = param
        self.reduce_node = []

    def fit(self):
        for y_label in self.label:
            feature_dict = {}
            for col in range(self.col):
                sub_feature_dict = {}
                for feature in set(self.df.iloc[:, col]):
                    filter_temp = self.df[self.df.iloc[:, -1] == y_label]
                    proba = (sum(filter_temp.iloc[:,col] == feature) + self._lambda) /\
                        (len(filter_temp)+len(set(self.df.iloc[:,col])))
                    sub_feature_dict[feature] = proba
                feature_dict[self.x_df.columns[col]] = sub_feature_dict
            self.label_proba[y_label] = feature_dict
        
        for label_value in self.label:
            self.label_side[label_value] = (sum(self.y_df == label_value) + self._lambda)/\
                                        (self.row + len(self.label) * self._lambda)
    def reduce(self, df):
        res = 1
        for x in df:
            res *= x
        return res

    def estimProbas(self, attrs):
        for rn in self.reduce_node:
            del attrs[rn]

        max_proba = 0
        res_probe_dict = {}
        x_list = list(attrs.values())[:-1]
        for label_key, label_value in self.label_proba.items():
            feature_prob = []
            count = 0
            for feature_key, feature_value in label_value.items():
                for prob_key, prob_value in feature_value.items():
                    if prob_key == x_list[count]:
                        feature_prob.append(prob_value)
                count += 1
            
            posterior_prob = self.reduce(feature_prob)
            res_probe_dict[label_key] = posterior_prob
            if posterior_prob > max_proba:
                max_proba = posterior_prob
                res_y = label_key

        return res_probe_dict

    def estimClass(self, attrs):
        for rn in self.reduce_node:
            del attrs[rn]

        max_proba = 0
        res_y = 0
        x_list = list(attrs.values())[:-1]
        for label_key, label_value in self.label_proba.items():
            feature_prob = []
            count = 0
            for feature_key, feature_value in label_value.items():
                for prob_key, prob_value in feature_value.items():
                    if prob_key == x_list[count]:
                        feature_prob.append(prob_value)
                count += 1
            
            posterior_prob = self.reduce(feature_prob)
            if posterior_prob > max_proba:
                max_proba = posterior_prob
                res_y = label_key
        return res_y
    
    def draw(self):
        """
            retourne le graphe du model Naive Bayes a partir 
        d'un dataframe et du nom de la colonne qui est la classe
        """
        str0=""
        for attr0 in list(self.df.columns):
            if isIndepFromTarget(self.df, attr0, self.param):
                self.reduce_node.append(attr0)
                continue
            if attr0!=self.attr:
                str0+=self.attr+"->"+attr0+";"
        str0=str0[0:-1]
        
        self.df = self.df.drop(columns=self.reduce_node)
        self.x_df = self.df.iloc[:, 0:-1]
        self.y_df = self.df.iloc[:, -1]
        self.label = set(self.y_df)
        self.col = len(self.x_df.columns)
        self.row = len(self.x_df)
        self.label_proba = {}
        self.label_side = {}
        self._lambda = 0.01
        self.fit()
        return utils.drawGraph(str0)

class ReducedMAPNaiveBayesClassifier(APrioriClassifier):
    def __init__(self, df, param, attr='target'):
        self.df=df
        self.attr=attr
        self.dictAttr=P2D_p(df,attr)
        self.param = param
        self.reduce_node = []

    def fit(self):
        for y_label in self.label:
            feature_dict = {}
            for col in range(self.col):
                sub_feature_dict = {}
                for feature in set(self.df.iloc[:, col]):
                    filter_temp = self.df[self.df.iloc[:, -1] == y_label]
                    proba = (sum(filter_temp.iloc[:,col] == feature) + self._lambda) /\
                        (len(filter_temp)+len(set(self.df.iloc[:,col])))
                    sub_feature_dict[feature] = proba
                feature_dict[self.x_df.columns[col]] = sub_feature_dict
            self.label_proba[y_label] = feature_dict
        
        for label_value in self.label:
            self.label_side[label_value] = (sum(self.y_df == label_value) + self._lambda)/\
                                        (self.row + len(self.label) * self._lambda)

    def reduce(self, df):
        res = 1
        for x in df:
            res *= x
        return res
        
    def estimProbas(self, attrs):
        for rn in self.reduce_node:
            del attrs[rn]

        max_proba = 0
        res_probe_dict = {}
        x_list = list(attrs.values())[:-1]
        for label_key, label_value in self.label_proba.items():
            feature_prob = []
            count = 0
            for feature_key, feature_value in label_value.items():
                for prob_key, prob_value in feature_value.items():
                    if prob_key == x_list[count]:
                        feature_prob.append(prob_value)
                count += 1
            
            posterior_prob = self.reduce(feature_prob)
            posterior_prob *= self.label_side[label_key]
            res_probe_dict[label_key] = posterior_prob
            if posterior_prob > max_proba:
                max_proba = posterior_prob
                res_y = label_key

        return res_probe_dict

    def estimClass(self, attrs):
        for rn in self.reduce_node:
            del attrs[rn]

        max_proba = 0
        res_y = 0
        x_list = list(attrs.values())[:-1]
        for label_key, label_value in self.label_proba.items():
            feature_prob = []
            count = 0
            for feature_key, feature_value in label_value.items():
                for prob_key, prob_value in feature_value.items():
                    if prob_key == x_list[count]:
                        feature_prob.append(prob_value)
                count += 1
            
            posterior_prob = self.reduce(feature_prob)
            posterior_prob *= self.label_side[label_key]
            if posterior_prob > max_proba:
                max_proba = posterior_prob
                res_y = label_key
        return res_y

    def draw(self):
        """
            retourne le graphe du model Naive Bayes a partir 
        d'un dataframe et du nom de la colonne qui est la classe
        """
        str0=""
        for attr0 in list(self.df.columns):
            if isIndepFromTarget(self.df, attr0, self.param):
                self.reduce_node.append(attr0)
                continue
            if attr0!=self.attr:
                str0+=self.attr+"->"+attr0+";"
        str0=str0[0:-1]
        
        self.df = self.df.drop(columns=self.reduce_node)
        self.x_df = self.df.iloc[:, 0:-1]
        self.y_df = self.df.iloc[:, -1]
        self.label = set(self.y_df)
        self.col = len(self.x_df.columns)
        self.row = len(self.x_df)
        self.label_proba = {}
        self.label_side = {}
        self._lambda = 0.01
        self.fit()
        return utils.drawGraph(str0)