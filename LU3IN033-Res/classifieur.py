import os
from analyse import *
import linecache


#obtenir l'addresse ip 
def get_line_context(file_path, line_number):
    return linecache.getline(file_path, line_number).strip()

addIP=get_line_context("information.txt", 2)
print("address IP local :" + str(addIP))

def show_highest_protocol(dic):
    bool=1
    for i in ["HTTP Type"]: #,"HTTP Method","HTTP Status","HTTP Version"
        if i not in dic.keys():
            bool=0
    if bool==1:
        return "HTTP"
    else:
        bool=1
        for i in ["TCP Source port","TCP Destination port", "TCP Sequence number","TCP Acknowledgement number"]:
            if i not in dic.keys():
                bool=0
        if bool==1:
            return "TCP"
        else:
            return "IP"

path = "trame"
files= os.listdir(path) 
num = len(files)-1
destinationListe = []              #Le classificateur va classer en fonction de l'adresse cible.
for i in range(num): 
    if os.path.exists("trame/trame{:d}.txt".format(i)):                
        f=open('trame/trame{:d}.txt'.format(i),'rb')
        str=f.read().decode(errors='replace').upper()
        dic=decode_simplified(str)

        bool=1
        for data in ["Type", "IP protocol", "TCP Source port","TCP Destination port", "TCP Sequence number","TCP Acknowledgement number"]:
            if data not in dic.keys():
                bool=0
            
        if bool==1:
            if dic['Type']=='IP' and dic['IP protocol']=='TCP':
                if dic['IP Destination address'] != addIP and dic['IP Destination address'] not in destinationListe:  
                    destinationListe.append(dic['IP Destination address'])
                if dic['IP Source address'] != addIP and dic['IP Source address'] not in destinationListe:
                    destinationListe.append(dic['IP Source address'])
            else:
                os.remove("trame/trame{:d}.txt".format(i))
        else:
            os.remove("trame/trame{:d}.txt".format(i))

print("liste de hôte capturé :")
print(destinationListe)


dicVide={}
for i in range(len(destinationListe)):
    listeFile=[]
    for j in range(num):                 
        f=open('trame/trame{:d}.txt'.format(j),'rb')
        str=f.read().decode(errors='replace').upper()
        dic=decode_simplified(str)
        if dic['IP Destination address']==destinationListe[i] or dic['IP Source address']==destinationListe[i]:
            listeFile.append('trame/trame{:d}.txt'.format(j))
    dicVide[destinationListe[i]]=listeFile

print("\n")
while 1:
    print("Please select an IP address to view its traffic with this machine")
    print("In order to select an IP, please enter on the keyboard the index of the list of ip addresses mentioned above \n(the first element of the list is 1, the second is 2, the n-th is n,...)")
    print("PRESS 0 TO EXIT")
    a=int(input("waiting for your choice : "))
    if a==0:
        print("\n\n\n\nGoodbye !")
        break
    ls=[0]
    for s in range(len(destinationListe)+1):
        ls.append(s)
    if a not in ls:
        print("The number of your input is invalide ")
    else:
        print("Please choose your filter")
        print("0 : tous ; 1 : TCP ; 2 : HTTP")
        b=int(input("waiting for your choice for filter: "))
        if b not in [0, 1, 2]:
            print("The number of your input is invalide ")
        else:
            listeFile=dicVide[destinationListe[a-1]]
            contenueListe=[]
            for i in listeFile:
                line=get_line_context(i, 1).upper()
                line=decode_simplified(line)
                contenueListe.append(line)

            #entete
            print()
            print('IP address'.center(15) + 'port'.center(7) + " ".ljust(32) + " ".rjust(30)+ 'port'.center(7) + 'IP address'.center(15))

            for i in range(len(contenueListe)):
                if b==0:
                    if show_highest_protocol(contenueListe[i])=="HTTP":
                        
                        print(" ".ljust(23), end="")
                        print("Type="+ contenueListe[i]["HTTP Type"] + "  ", end="")
                        if "HTTP Method" in contenueListe[i].keys():
                            print(contenueListe[i]["HTTP Method"] + "  ", end="")
                        print(contenueListe[i]["HTTP Version"], end="")
                        print("  ", end="")
                        if "HTTP Status" in contenueListe[i].keys():
                            print(contenueListe[i]["HTTP Status"], end="")
                            if contenueListe[i]["HTTP Status"]=="200":
                                print("  OK", end="")
                            if contenueListe[i]["HTTP Status"]=="404":
                                print("  Not Found", end="")
                            if contenueListe[i]["HTTP Status"]=="100":
                                print("  Continue", end="")
                            if contenueListe[i]["HTTP Status"]=="500":
                                print("  Internal Server Error", end="")
                        print("")

                    if show_highest_protocol(contenueListe[i])=="TCP":
                        print(" ".ljust(23), end="")
                        print(contenueListe[i]['TCP Flags'], end="")
                        print("  ", end="")
                        print("Seq={:d}   ".format(contenueListe[i]["TCP Sequence number"])+ "Ack={:d}   ".format(contenueListe[i]["TCP Acknowledgement number"]) , end="")
                        print("Window={:d}".format(contenueListe[i]['TCP Window']))
                    
                    if contenueListe[i]['IP Destination address']==addIP:
                        print(contenueListe[0]['IP Source address'].center(15), end='')
                        print("{:^7d}".format(contenueListe[0]["TCP Source port"]), end='')
                        print("----------------------------------", end='')
                        print("--------------------------->", end='')
                        print("{:^7d}".format(contenueListe[0]["TCP Destination port"]), end='')
                        print(contenueListe[0]['IP Destination address'].center(15))
                    else:
                        print(contenueListe[0]['IP Source address'].center(15), end='')
                        print("{:^7d}".format(contenueListe[0]["TCP Source port"]), end='')
                        print("<---------------------------------", end='')
                        print("----------------------------", end='')
                        print("{:^7d}".format(contenueListe[0]["TCP Destination port"]), end='')
                        print(contenueListe[0]['IP Destination address'].center(15))

                if b==1:
                    if show_highest_protocol(contenueListe[i])=="TCP":
                        print(" ".ljust(23), end="")
                        print(contenueListe[i]['TCP Flags'], end="")
                        print("  ", end="")
                        print("Seq={:d}   ".format(contenueListe[i]["TCP Sequence number"])+ "Ack={:d}   ".format(contenueListe[i]["TCP Acknowledgement number"]) , end="")
                        print("Window={:d}".format(contenueListe[i]['TCP Window']))
                        if contenueListe[i]['IP Destination address']==addIP:
                            print(contenueListe[0]['IP Source address'].center(15), end='')
                            print("{:^7d}".format(contenueListe[0]["TCP Source port"]), end='')
                            print("----------------------------------", end='')
                            print("--------------------------->", end='')
                            print("{:^7d}".format(contenueListe[0]["TCP Destination port"]), end='')
                            print(contenueListe[0]['IP Destination address'].center(15))
                        else:
                            print(contenueListe[0]['IP Source address'].center(15), end='')
                            print("{:^7d}".format(contenueListe[0]["TCP Source port"]), end='')
                            print("<---------------------------------", end='')
                            print("----------------------------", end='')
                            print("{:^7d}".format(contenueListe[0]["TCP Destination port"]), end='')
                            print(contenueListe[0]['IP Destination address'].center(15))
        
                
                if b==2:
                    if show_highest_protocol(contenueListe[i])=="HTTP":
                        
                        print(" ".ljust(23), end="")
                        print("Type="+ contenueListe[i]["HTTP Type"] + "  ", end="")
                        if "HTTP Method" in contenueListe[i].keys():
                            print(contenueListe[i]["HTTP Method"] + "  ", end="")
                        print(contenueListe[i]["HTTP Version"], end="")
                        print("  ", end="")
                        if "HTTP Status" in contenueListe[i].keys():
                            print(contenueListe[i]["HTTP Status"], end="")
                            if contenueListe[i]["HTTP Status"]=="200":
                                print("  OK", end="")
                            if contenueListe[i]["HTTP Status"]=="404":
                                print("  Not Found", end="")
                            if contenueListe[i]["HTTP Status"]=="100":
                                print("  Continue", end="")
                            if contenueListe[i]["HTTP Status"]=="500":
                                print("  Internal Server Error", end="")
                        print("")

                        if contenueListe[i]['IP Destination address']==addIP:
                            print(contenueListe[0]['IP Source address'].center(15), end='')
                            print("{:^7d}".format(contenueListe[0]["TCP Source port"]), end='')
                            print("----------------------------------", end='')
                            print("--------------------------->", end='')
                            print("{:^7d}".format(contenueListe[0]["TCP Destination port"]), end='')
                            print(contenueListe[0]['IP Destination address'].center(15))
                        else:
                            print(contenueListe[0]['IP Source address'].center(15), end='')
                            print("{:^7d}".format(contenueListe[0]["TCP Source port"]), end='')
                            print("<---------------------------------", end='')
                            print("----------------------------", end='')
                            print("{:^7d}".format(contenueListe[0]["TCP Destination port"]), end='')
                            print(contenueListe[0]['IP Destination address'].center(15))
