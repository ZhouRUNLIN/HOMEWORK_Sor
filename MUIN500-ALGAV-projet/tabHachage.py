# Auteur : ZHOU runlin
# Num Etu : 28717281

# Code du Exec 4
import math

def padding(message: bytes) -> bytes:
    # ajouter le bit "1" au message
    ascii_list = list(map(lambda x: x, message))
    msg_length = len(ascii_list) * 8    # taille de message avant padding
    ascii_list.append(128)

    # ajouter le bit "0" jusqu'à ce que la taille du message en bits soit égale à 448
    while (len(ascii_list) * 8 + 64) % 512 != 0:
        ascii_list.append(0)

    # ajouter la taille du message initial codée en 64-bit little-endian au message
    for i in range(8):
        ascii_list.append((msg_length >> (8 * i)) & 0xff)
    return ascii_list

# definir les fonctions utilser dans le boucle principale
F = lambda x, y, z: ((x & y) | ((~x) & z))      # F(B, C, D) = (B ET C) OU (non B ET D)
G = lambda x, y, z: ((x & z) | (y & (~z)))      # G(B, C, D) = (B ET D) OU (C ET non D)
H = lambda x, y, z: (x ^ y ^ z)                 # H(B, C, D) = B XOR C XOR D
I = lambda x, y, z: (y ^ (x | (~z)))            # I(B, C, D) = C XOR (B OU non D)

# Left Rotation
# Il se peut qu'il y ait plus de 32 bits après le décalage vers la gauche, 
# donc, il faut effectuer une opération ET avec 0xffffffff pour s'assurer que le résultat est bien de 32 bits.
L = lambda x, n: ((x << n) | (x >> (32 - n))) & 0xffffffff

# Convertir quatre nombres non signés de 8 bits en un nombre non signé de 32 bits
# exemple : 0x12,0x34,0x56,0x78 -> 0x78563412
W = lambda i4, i3, i2, i1: (i1 << 24) | (i2 << 16) | (i3 << 8) | i4

# Échanger les bits de poids fort et de poids faible d'un nombre non signé de 32 bits
# exemple : 0x12345678 -> 0x78563412 
reverse = lambda x: (x << 24) & 0xff000000 | (x << 8) & 0x00ff0000 | (x >> 8) & 0x0000ff00 | (x >> 24) & 0x000000ff

def Md5(message: bytes) -> bytes:
    # definir les constances, (en little-endian)
    # Définir r comme suit :
    R = (7, 12, 17, 22) * 4 + (5, 9, 14, 20) * 4 + \
        (4, 11, 16, 23) * 4 + (6, 10, 15, 21) * 4
    # MD5 utilise des sinus d'entiers pour ses constantes
    # k[i] := floor(abs(sin(i + 1)) × 2^32)
    K = [math.floor(abs(math.sin(i + 1)) * 2**32) & 0xFFFFFFFF for i in range(64)]
    
    # Préparation des variables :
    h0 = 0x67452301
    h1 = 0xefcdab89
    h2 = 0x98badcfe
    h3 = 0x10325476

    ascii_list = padding(message)
    # Découpage en blocs de 512 bits
    # la taille de chaque bloc : 512bits=16*32bits=64*8bits
    for i in range(len(ascii_list) // 64):
        # initialiser les valeurs de hachage
        a, b, c, d = h0, h1, h2, h3

        # Boucle principale !!!
        for j in range(64):
            if 0 <= j <= 15:
                f = F(b, c, d) & 0xffffffff
                g = j
            elif 16 <= j <= 31:
                f = G(b, c, d) & 0xffffffff
                g = ((5 * j) + 1) % 16
            elif 32 <= j <= 47:
                f = H(b, c, d) & 0xffffffff
                g = ((3 * j) + 5) % 16
            else:
                f = I(b, c, d) & 0xffffffff
                g = (7 * j) % 16

            aa, dd, cc = d, c, b
            # fait des swaps
            s = i * 64 + g * 4
            w = W(ascii_list[s], ascii_list[s + 1], ascii_list[s + 2], ascii_list[s + 3])
            bb = (L((a + f + K[j] + w) & 0xffffffff, R[j]) + b) & 0xffffffff    # l'appel du rotation Left
            a, b, c, d = aa, bb, cc, dd
        
        # ajouter le résultat au bloc précédent 
        h0 = (h0 + a) & 0xffffffff
        h1 = (h1 + b) & 0xffffffff
        h2 = (h2 + c) & 0xffffffff
        h3 = (h3 + d) & 0xffffffff
    
    h0, h1, h2, h3 = reverse(h0), reverse(h1), reverse(h2), reverse(h3)
    # concaténer
    digest = (h0 << 96) | (h1 << 64) | (h2 << 32) | h3
    
    return hex(digest)[2:].rjust(32, '0')

if __name__ == '__main__':
    # le string à tester
    test = "The quick brown fox jumps over the lazy dog" # = 9e107d9d372bb6826bd81d3542a419d6
    print("The original word : " + test)
    print("The result of our func : " + Md5(b"The quick brown fox jumps over the lazy dog"))

    # verifier le résultat
    import hashlib
    t = hashlib.md5()
    t.update(b"The quick brown fox jumps over the lazy dog")
    print("The result of md5 in lib : " + t.hexdigest())
