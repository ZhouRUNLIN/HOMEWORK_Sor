# PSAR

Sujet : 
L’objectif est de concevoir un gestionnaire de tas réparti entre des processus situés sur des machines différentes. Le tas est stocké dans un segment mémoire dupliqué surchaque processus. Chaque processus peut allouer une zone en lui associant un nom symbolique puis la lire et la modifier. 

Les processus peuvent faire les opérations suivantes :

• Création d’une nouvelle donnée (dmalloc) : le processus alloue dans tous les tas une donnée à la même adresse et lui associe un nom.<br>
• Demande un accès en lecture (daccess_read) : le processus demande l’accès en lecture à une donnée dont il connaît le nom. Si un accès en écriture est en cours sur un autre processus l’appel est bloqué.<br>
• Demande un accès en écriture (daccess_write) : le processus demande l’accès exclusif en écriture (et lecture) à une donnée dont il connaît le nom. Si un accès est en cours sur un autre processus l’appel est bloqué.<br>
• Manipulation de la donnée : une fois l’accès autorisé, la donnée peut être lue et modifiée directement en mémoire.<br>
• Fin de manipulation de la donnée (drelease) ; le processus signale qu’il n’utilise plus la donnée. Toutes les modifications faites sur la donnée sont alors propagées à tous les processus.<br>
• Destruction de la donnée (dfree) : le processus détruit la donnée dans tous les tas.<br>


### check update info through :
https://docs.google.com/spreadsheets/d/12pzZp1Ouw7IqKV83eWC61hRKy3UQp33Y3Q0T2vx6Lng/edit#gid=1386834576
