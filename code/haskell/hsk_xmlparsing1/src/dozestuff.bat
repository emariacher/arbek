ghc -c -O -package hxt %1.hs
ghc -o %1 %1.o
