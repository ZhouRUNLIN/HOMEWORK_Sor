# TME5 : lab project for PSCR

This small ray tracer, is an example used in "Programmation Système Répartie et Concurrente" Master 1 course @ Sorbonne Université.
The goal is parallelize this application using a hand built thread pool.

[Homepage](https://pages.lip6.fr/Yann.Thierry-Mieg/PR)

Parallel ray tracing.

This project is compatible with [autoconf/automake](https://www.lrde.epita.fr/~adl/autotools.html), after cloning, use the mantra 
```
autoreconf -vfi
./configure 
make
```
in the root folder to build the executable "src/tme5".

Look at the contents of "configure.ac", "Makefile.am" and "src/Makefile.am" which are the input to autotools, and are relatively simple and easy to read.

Running this executable will generate a file "toto.ppm" in the current folder. Open this file with Gimp, or some other tool that can open this [very simple image format](https://en.wikipedia.org/wiki/Netpbm_format)

Configuration files are also provided as a Eclipse CDT project, and as Visual Studio 2017+ solution/project configurations.

(c) Sorbonne Université 2018-2019
