from analyse import *
t="f8 4d 89 85 ee 46 8c fd de c5 b6 cc 08 00 45 00 00 56 01 14 40 00 37 06 1e e2 2f f6 31 e1 c0 a8 01 2d 00 50 f3 8f 0d 92 23 1c 40 e4 9a 8c 50 18 00 53 65 4a 00 00 69 6e 3a 20 2a 0d 0a 45 61 67 6c 65 49 64 3a 20 32 66 66 36 33 31 39 35 31 36 37 30 34 33 39 33 38 37 36 39 32 37 35 33 32 65 0d 0a 0d 0a"
t=t.upper()
print(decode_no_CRC(t))