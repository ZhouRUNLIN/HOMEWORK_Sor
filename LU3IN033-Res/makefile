#obtenir les donnees necessaire
install:
	chmod +x *.sh
	./getInformation.sh > information.txt

#capture : capture tous les trames passer sur en0 dans 3 secondes et stocker les ficher en format de .txt
capture:
	tcpdump -i en0 port 80 -w trame/trace.pcap & sleep 10 
	python3 transforme.py
	rm -f trame/trace.pcap

#analyser les trame et mettre les trame a l'ordre de temps
analyse:
	python3 classifieur.py 2>&1 | tee fluxRes.txt

#clean : initialiser la répertoire
clean: 
	rm -f trame/*.txt *.txt
