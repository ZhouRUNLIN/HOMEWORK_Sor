#!/bin/sh

gnuplot -e "
set terminal pdfcairo;
set output '$2';
set style data histogram;
set style fill solid border -1;
set boxwidth 1 absolute;
plot '$1' using 2:xtic(1);
"
