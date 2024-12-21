# README

## Travail demandé : Étude expérimentale 1

### Description du projet

Ce projet vise à développer un outil de simulation de systèmes distribués expérimentaux pour étudier et analyser l'impact des paramètres sur les performances.

---

### Instructions de lancement

1. **Lancer le programme**  
   Pour exécuter le programme, accédez au chemin suivant et lancez :  
   `ara/projet/ee1/SimulationLauncher.java`

2. **Modifier les paramètres d'exécution**  
   Si vous devez modifier les paramètres d'exécution, éditez le fichier de configuration situé à la racine du projet :  
   **`naimi_trehel.cfg`**

   Voici une explication des principaux paramètres :

    - **`network.size`**
        - Spécifie la taille du réseau (le nombre de nœuds).

    - **`protocol.transport.mindelay`**
        - Définit la valeur maximale de γ.

    - **`protocol.transport.maxdelay`**
        - Définit la valeur minimale de γ.
        - Remarque : la valeur moyenne de γ est calculée comme suit :  
          **γ = (mindelay + maxdelay) / 2**

    - **`protocol.mutex.timeCS`**
        - Représente le paramètre α (temps passé dans la section critique).

    - **`protocol.mutex.timeBetweenCS`**
        - Représente le paramètre β (temps entre deux sections critiques).

---

