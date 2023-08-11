
#Outils
def h2d_half(s:str):
    """
    len(s)==1
    Calculer la valeur décimale d'un demi-octet
    """
    assert len(s)==1
    return int(s,16)

def h2d_byte(s:str):
    """
    len(s)==2
    Calculer la valeur décimale d'un octet
    """
    assert len(s)==2
    return int(s,16)

def discharge(s:str,n:int):
    """
    décharger n octets.
    return: ([liste des octets],str reste)
    """
    if n==0:
        return ([],s)
    if ' ' not in s:
        assert n==1
        return ([s],"")
    l,sr=discharge(s[3:],n-1)
    return ([s[0:2]]+l,sr)

def merge_dict(a:dict,b:dict):
    a.update(b)
    return a

# Debut
def decode(s:str):
    """
    Décoder la trame, retournant un dictionnaire
    """
    return eth_preamble(s)

def decode_no_CRC(s:str):
    return eth_dest_addr(s)

def decode_simplified(s:str):
    d=decode_no_CRC(s)
    ds=dict()
    for key in ["Type","IP protocol","IP Source address","IP Destination address","TCP Source port","TCP Destination port","UDP Source port","UDP Destination port","TCP Sequence number","TCP Acknowledgement number","TCP Flags","TCP Window","HTTP Type","HTTP Method","HTTP URL","HTTP Status","HTTP Version","HTTP Message","Wrong data"]:
        if key in d.keys():
            ds.update({key:d[key]})
    return ds

# ethernet
def eth_preamble(s:str):
    """
    preamble : 8 octets inutiles
    """
    l,sr=discharge(s,8)
    return eth_dest_addr(sr)

def eth_dest_addr(s:str):
    """
    Destination address : 6 octets
    L'adresse physique (MAC) du destinataire de la trame
    """
    l,sr=discharge(s,6)
    addr=""
    for byte in l:
        addr+=byte+":"
    return merge_dict({"Eth Destination address":addr[0:-1]},eth_src_addr(sr))

def eth_src_addr(s:str):
    """
    Source address : 6 octets
    L'adresse physique (MAC) du source de la trame
    """
    l,sr=discharge(s,6)
    addr=""
    for byte in l:
        addr+=byte+":"
    return merge_dict({"Eth Source address":addr[0:-1]},eth_type(sr))

def eth_type(s:str):
    """
    Type : 2 octets
    Protocole de niveau supérieur encapsulé dans le champ Data de la trame.
    """
    l,sr=discharge(s,2)
    type0=l[0]+l[1]
    if type0=="0800":
        try:
            d=ip_version_IHL(sr)
        except:
            d={"Wrong data":1}
        return merge_dict({"Type":"IP"},d)
    if type0=="0806":
        try:
            d=arp_hardware(sr)
        except:
            d={"Wrong data":1}
        return merge_dict({"Type":"ARP"},d)
    return {"Type":type0}

# ip
def ip_version_IHL(s:str):
    """
    Version : 4 bits
    L'identification de la version courante du protocole
    IHL : 4 bits
    IP Header Length, la longueur de l'en-tête IP exprimée en mots de 32 bits
    !!! Dans le dictionnaire nous le représentons en octets
    """
    l,sr=discharge(s,1)
    version=h2d_half(l[0][0])
    ihl=h2d_half(l[0][1])*4
    return merge_dict({"Version":version,"IHL":ihl},ip_TOS(sr,ihl-1))

def ip_TOS(s:str,IHL:int):
    """
    TOS : 1 octet
    Type of service, le type de service à appliquer au paquet en fonction de certains paramètres comme le délai de transit, la sécurité. Il est peu utilisé et sa valeur est généralement égale à 0.
    """
    l,sr=discharge(s,1)
    tos=l[0]
    return merge_dict({"TOS":tos},ip_totalL(sr,IHL-1))

def ip_totalL(s:str,IHL:int):
    """
    Total length : 2 octets
    La longueur totale du datagramme, exprimée en octets. En pratique, il est rare qu'un datagramme IP fasse plus de 1500 octets
    """
    l,sr=discharge(s,2)
    totalL=h2d_byte(l[0])*256+h2d_byte(l[1])
    return merge_dict({"Total length":totalL},ip_identification(sr,IHL-2,totalL-4))

def ip_identification(s:str,IHL:int,totalL:int):
    """
    Identification : 2 octets
    Sert en cas de fragmentation/réassemblage du datagramme. Ce champ permet alors à l'entité réceptrice de reconnaître les fragments issus d'un même datagramme initial et qui doivent donc faire l'objet d'un réassemblage.
    """
    l,sr=discharge(s,2)
    idf=l[0]+l[1]
    return merge_dict({"Identification":"0x"+idf},ip_flags_fo(sr,IHL-2,totalL-2))

def ip_flags_fo(s:str,IHL:int,totalL:int):
    """
    flags : 3 bits
        bit réservé : mis à 0
        DF (Don't Fragment) : mis à 1 par l'émetteur pour interdire la fragmentation
        MF (More Fragment) : mis à 1 pour signifier que le fragment courant est suivi d'un autre fragment
    fragment offset : 13 bits
    Donner la position relative du fragment dans le datagramme initial, le déplacement étant donné en unités de 64 bits
    """
    l,sr=discharge(s,2)
    num=int(l[0]+l[1],16)
    numBin="{:016b}".format(num)
    assert numBin[0]=='0'
    if numBin[1]=='1':
        f="DF"
    elif numBin[2]=='1':
        f="MF"
    else:
        f="None"
    numBin=numBin[3:]
    fo=int(numBin,2)
    return merge_dict({"flags":f,"Fragment offset":fo},ip_TTL(sr,IHL-2,totalL-2))

def ip_TTL(s:str,IHL:int,totalL:int):
    """
    Identification : 1 octet
    Time To Live, donner une indication de la limite supérieure du temps de vie d'un datagramme
    """
    l,sr=discharge(s,1)
    ttl=h2d_byte(l[0])
    return merge_dict({"TTL":ttl},ip_protocol(sr,IHL-1,totalL-1))

def ip_protocol(s:str,IHL:int,totalL:int):
    """
    Identification : 1 octet
    Le protocole (de niveau supérieur) utilisé pour le champ de données du datagramme.
    """
    l,sr=discharge(s,1)
    protocol=h2d_byte(l[0])
    if protocol in (1,6,17):
        prot_Name={1:"ICMP",6:"TCP",17:"UDP"}[protocol]
    else:
        prot_Name=str(protocol)
    return merge_dict({"IP protocol":prot_Name},ip_header_checksum(sr,IHL-1,totalL-1,prot_Name))

def ip_header_checksum(s:str,IHL:int,totalL:int,protocol:str):
    """
    Header checksum : 2 octets
    Une zone de contrôle d'erreur portant uniquement sur l'en-tête du datagramme
    """
    l,sr=discharge(s,2)
    cs=l[0]+l[1]
    return merge_dict({"IP Header checksum":"0x"+cs},ip_src_addr(sr,IHL-2,totalL-2,protocol))

def ip_src_addr(s:str,IHL:int,totalL:int,protocol:str):
    """
    Source address : 4 octets
    L'adresse IP de la source du datagramme
    """
    l,sr=discharge(s,4)
    ip=""
    for i in range(4):
        ip+=str(h2d_byte(l[i]))+"."
    ip=ip[0:-1]
    return merge_dict({"IP Source address":ip},ip_dest_addr(sr,IHL-4,totalL-4,protocol))

def ip_dest_addr(s:str,IHL:int,totalL:int,protocol:str):
    """
    Source address : 4 octets
    L'adresse IP de la destination du datagramme
    """
    l,sr=discharge(s,4)
    ip=""
    for i in range(4):
        ip+=str(h2d_byte(l[i]))+"."
    ip=ip[0:-1]
    if IHL-4==0:
        return merge_dict({"IP Destination address":ip},ip_to_protocol(sr,protocol))
    return merge_dict({"IP Destination address":ip},ip_option(sr,protocol,IHL-4))

# IP options
def ip_option(s:str,protocol:str,IHL:int):
    """
    Type : 1 octet
    Length : 1 octet
    Data : Length-2 octets
    """
    l,sr=discharge(s,1)
    if l[0]=="00":
        return ip_option_padding(sr,protocol,IHL-1)
    dType={1:"NOP",7:"RR",68:"TS",131:"LSR",137:"SSR"}
    if h2d_byte(l[0]) in dType:
        oType=dType[h2d_byte(l[0])]
    else:
        oType=str(h2d_byte(l[0]))
    l,sr1=discharge(sr,1)
    oLen=h2d_byte(l[0])
    l,sr2=discharge(sr1,oLen-2)
    oData=""
    for octet in l:
        oData+=octet+" "
    oData=oData[0:-1]
    return merge_dict({"IP Option "+oType:oData},ip_option(sr2,protocol,IHL-oLen))

def ip_option_padding(s:str,protocol:str,IHL:int):
    """
    Padding : 0-3 octets
    Permet d'aligner l'en-tête sur 32 bits
    """
    l,sr=discharge(s,IHL)
    return ip_to_protocol(sr,protocol)

def ip_to_protocol(s:str,protocol:str):
    if protocol=="ICMP":
        try:
            d=icmp_start(s)
        except:
            d={"Wrong data":1}
        return d
    if protocol=="UDP":
        try:
            d=udp_src_port(s)
        except:
            d={"Wrong data":1}
        return d
    if protocol=="TCP":
        try:
            d=tcp_src_port(s)
        except:
            d={"Wrong data":1}
        return d
    return dict()

# ARP
def arp_hardware(s:str):
    """
    Type : 2 octets
    Le type d'interface pour laquelle l'émetteur cherche une réponse
    """
    l,sr=discharge(s,2)
    hw=l[0]+l[1]
    return merge_dict({"Hardware":"0x"+hw},arp_protocol(sr))

def arp_protocol(s:str):
    """
    Type : 2 octets
    Le type d'interface pour laquelle l'émetteur cherche une réponse
    """
    l,sr=discharge(s,2)
    protocol=l[0]+l[1]
    return merge_dict({"Protocol":"0x"+protocol},arp_Hlen(sr))

def arp_Hlen(s:str):
    """
    Type : 1 octet
    La taille de l'adresse physique (Ethernet) en octets
    """
    l,sr=discharge(s,1)
    hlen=h2d_byte(l[0])
    return merge_dict({"Hlen":hlen},arp_Plen(sr))

def arp_Plen(s:str):
    """
    Type : 1 octet
    La taille de l'adresse au niveau protocolaire (IP)
    """
    l,sr=discharge(s,1)
    plen=h2d_byte(l[0])
    return merge_dict({"Plen":plen},arp_operation(sr))

def arp_operation(s:str):
    """
    Type : 2 octets
    Le type d'opération à effectuer par le récepteur
    """
    l,sr=discharge(s,2)
    op=l[0]+l[1]
    return merge_dict({"Operation":"0x"+op},arp_sender_HA(sr))

def arp_sender_HA(s:str):
    """
    Type : 6 octets
    L'adresse physique (Ethernet) de l'émetteur
    """
    l,sr=discharge(s,6)
    addr=""
    for byte in l:
        addr+=byte+":"
    return merge_dict({"Sender HA":addr[0:-1]},arp_sender_IA(sr))

def arp_sender_IA(s:str):
    """
    Type : 4 octets
    L'adresse de niveau protocolaire (IP) demandé de l'émetteur 
    """
    l,sr=discharge(s,4)
    ip=""
    for i in range(4):
        ip+=str(h2d_byte(l[i]))+"."
    ip=ip[0:-1]
    return merge_dict({"Sender IA":ip},arp_target_HA(sr))

def arp_target_HA(s:str):
    """
    Type : 6 octets
    L'adresse physique (Ethernet) du récepteur
    """
    l,sr=discharge(s,6)
    addr=""
    for byte in l:
        addr+=byte+":"
    return merge_dict({"Target HA":addr[0:-1]},arp_target_IA(sr))

def arp_target_IA(s:str):
    """
    Type : 4 octets
    L'adresse de niveau protocolaire (IP) demandé du récepteur 
    """
    l,sr=discharge(s,4)
    ip=""
    for i in range(4):
        ip+=str(h2d_byte(l[i]))+"."
    ip=ip[0:-1]
    return {"Target IA":ip}

#ICMP
def icmp_start(s:str):
    """
    Type : 2 octets
    Est 0x0800 ou 0x0000
    """
    l,sr=discharge(s,2)
    assert l[0] in ["08","00"]
    assert l[1] == "00"
    return icmp_checksum(sr)

def icmp_checksum(s:str):
    """
    Type : 2 octets
    """
    l,sr=discharge(s,2)
    return merge_dict({"ICMP Checksum":"0x"+l[0]+l[1]},icmp_identifier(sr))

def icmp_identifier(s:str):
    """
    Type : 2 octets
    """
    l,sr=discharge(s,2)
    return merge_dict({"ICMP Identifier":"0x"+l[0]+l[1]},icmp_seq(sr))

def icmp_seq(s:str):
    """
    Type : 2 octets
    """
    l,sr=discharge(s,2)
    return merge_dict({"ICMP Sequence number":"0x"+l[0]+l[1]},icmp_opData(sr))

def icmp_opData(s:str):
    """
    Data : ? octets
    """
    return {"ICMP Optional data":"0x"+s}

# UDP
def udp_src_port(s:str):
    """
    Source Port : 2 octets
    Le port du source
    """
    l,sr=discharge(s,2)
    port=256*h2d_byte(l[0])+h2d_byte(l[1])
    return merge_dict({"UDP Source port":port},udp_dest_port(sr))

def udp_dest_port(s:str):
    """
    Destination Port : 2 octets
    Le port de la destination
    """
    l,sr=discharge(s,2)
    port=256*h2d_byte(l[0])+h2d_byte(l[1])
    return merge_dict({"UDP Destination port":port},udp_dest_port(sr))

def udp_length(s:str):
    """
    Length : 2 octets
    La longueur totale (en octets) du segment UDP
    """
    l,sr=discharge(s,2)
    lenU=256*h2d_byte(l[0])+h2d_byte(l[1])
    return merge_dict({"UDP Length":lenU},udp_checksum(sr))

def udp_checksum(s:str):
    """
    Length : 2 octets
    Champs de contrôle optionnel (mis à zéro si non utilisé) portant sur tout le segment augmenté d'un pseudo en-tête constitué d'informations de l'en-tête IP
    """
    l,sr=discharge(s,2)
    cs=l[0]+l[1]
    return merge_dict({"UDP checksum":"0x"+cs},udp_data(sr))

def udp_data(s:str):
    """
    Data : ? octets
    """
    return {"UDP Data":"0x"+s}

# TCP
def tcp_src_port(s:str):
    """
    Source Port : 2 octets
    Le port du source
    """
    l,sr=discharge(s,2)
    port=256*h2d_byte(l[0])+h2d_byte(l[1])
    return merge_dict({"TCP Source port":port},tcp_dest_port(sr,port))

def tcp_dest_port(s:str,sp:int):
    """
    Destination Port : 2 octets
    Le port de la destination
    """
    l,sr=discharge(s,2)
    port=256*h2d_byte(l[0])+h2d_byte(l[1])
    if 80 in [sp,port]:
        http=1
    else:
        http=0
    return merge_dict({"TCP Destination port":port},tcp_seq_num(sr,http))

def tcp_seq_num(s:str,http:int):
    """
    Sequence number : 4 octets
    Le numéro de séquence du premier octet de données du segment TCP ; si le drapeau SYN est à 1, ce numéro est l'ISN (Initial Sequence Number) 
    et le premier octet de données sera numéroté ISN+1
    """
    l,sr=discharge(s,4)
    num=(256**3)*h2d_byte(l[0])+(256**2)*h2d_byte(l[1])+256*h2d_byte(l[2])+h2d_byte(l[3])
    return merge_dict({"TCP Sequence number":num},tcp_ack_num(sr,http))

def tcp_ack_num(s:str,http:int):
    """
    Acknowledgement number : 4 octets
    Le numéro d'acquittement ; si le drapeau ACK est à 1, ce numéro contient la valeur du prochain numéro de séquence que l'émetteur est prêt à recevoir
    """
    l,sr=discharge(s,4)
    num=(256**3)*h2d_byte(l[0])+(256**2)*h2d_byte(l[1])+256*h2d_byte(l[2])+h2d_byte(l[3])
    return merge_dict({"TCP Acknowledgement number":num},tcp_do_op(sr,http))

def tcp_do_op(s:str,http:int):
    """
    Data offset : 4 bits
    La longueur de l'en-tête TCP exprimée en mots de 32 bits ; elle indique donc où les données commencent
    Reserved : 6 bits
    Doit être mis à zéro
    URG/ACK/PSH/RST/SYN/FIN : 1 bit
    """
    l,sr=discharge(s,2)
    doTcp=h2d_half(l[0][0:1])
    ops="{:08b}".format(h2d_byte(l[1]))
    dOp={2:"URG",3:"ACK",4:"PSH",5:"RST",6:"SYN",7:"FIN"}
    lOp=[]
    for i in range(2,8):
        if ops[i]=='1':
            lOp.append(dOp[i])
    return merge_dict({"TCP Data offset":doTcp,"TCP Flags":lOp},tcp_window(sr,http,4*doTcp-20))

def tcp_window(s:str,http:int,lo:int):
    """
    Window : 2 octets
    Fenêtre d'anticipation de taille variable ; la valeur de ce champ indique au récepteur combien il peut émettre d'octets après l'octet acquitté
    """
    l,sr=discharge(s,2)
    w=256*h2d_byte(l[0])+h2d_byte(l[1])
    return merge_dict({"TCP Window":w},tcp_checksum(sr,http,lo))

def tcp_checksum(s:str,http:int,lo:int):
    """
    Checksum : 2 octets
    Champs de contrôle portant sur tout le segment augmenté d'un pseudo en-tête constitué d'informations de l'en-tête IP
    """
    l,sr=discharge(s,2)
    cs=l[0]+l[1]
    return merge_dict({"TCP checksum":"0x"+cs},tcp_up(sr,http,lo))

def tcp_up(s:str,http:int,lo:int):
    """
    Urgent pointer : 2 octets
    Pointeur indiquant l'emplacement des données urgentes ; utilisé uniquement si le drapeau URG est positionné à 1
    """
    l,sr=discharge(s,2)
    w=256*h2d_byte(l[0])+h2d_byte(l[1])
    if lo==0:
        return merge_dict({"TCP Urgent pointer":w},tcp_to_protocol(sr,http))
    return merge_dict({"TCP Urgent pointer":w},tcp_options(sr,http,lo))

def tcp_options(s:str,http:int,lo:int):
    """
    Type : 1 octet
    Length : 1 octet
    Data : Length-2 octets
    """
    l,sr=discharge(s,1)
    if l[0] in ["00","01"]:
        return tcp_option_padding(sr,http,lo-1)
    oType=str(h2d_byte(l[0]))
    l,sr1=discharge(sr,1)
    oLen=h2d_byte(l[0])
    l,sr2=discharge(sr1,oLen-2)
    oData=""
    for octet in l:
        oData+=octet+" "
    oData=oData[0:-1]
    return merge_dict({"TCP Option "+oType:oData},tcp_options(sr2,http,lo-oLen))

def tcp_option_padding(s:str,http:int,lo:int):
    """
    Padding : 0-3 octets
    Permet d'aligner l'en-tête sur 32 bits
    """
    l,sr=discharge(s,lo)
    return tcp_to_protocol(sr,http)

def tcp_to_protocol(s:str,http:int):
    if http==1:
        try:
            return http_method_version(s)
        except:
            return {"Wrong data":1}
    return {"TCP Data":s}

# http
def http_method_version(s:str):
    l,sr=discharge(s,1)
    m=""
    while l[0]!="20":
        m+=chr(h2d_byte(l[0]))
        l,sr=discharge(sr[:],1)
    if m in ["GET","HEAD","POST","PUT","DELETE","CONNECT","OPTIONS","TRACE","PATCH"]:
        return merge_dict({"HTTP Type":"Request","HTTP Method":m},http_URL_status(sr,0))
    elif "HTTP" in m:
        return merge_dict({"HTTP Type":"Response","HTTP Version":m},http_URL_status(sr,1))
    return{"TCP Data":s}

def http_URL_status(s:str,t:int):
    l,sr=discharge(s,1)
    m=""
    while l[0]!="20":
        m+=chr(h2d_byte(l[0]))
        l,sr=discharge(sr[:],1)
        
    if t==0:
        return merge_dict({"HTTP URL":m},http_version_message(sr,t))
    return merge_dict({"HTTP Status":m},http_version_message(sr,t))

def http_version_message(s:str,t:int):
    l,sr=discharge(s,1)
    m=""
    while l[0]!="0D":
        m+=chr(h2d_byte(l[0]))
        l,sr=discharge(sr[:],1)
    if t==0:
        return {"HTTP Version":m}
    return {"HTTP Message":m}