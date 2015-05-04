Eric Mariacher - Stäfa 22 novembre 2012 (Altran-Phonak)

carte est un projet vieux de 32 ans, d'abord abordé en programmation en C
pour la génération automatique de cartes à Raleigh en 1993 (IBM).
La 1ère version de coloriage a été réalisée en Java à la Gaude en 1996 (IBM).
Cette version en Scala avec l'algorythme considéré comme "ultime" à été programmé à Lausanne en 2011 (Logitech).

l'unité de base est le Carré formé de 4 cantons:
 
 NO ! NE
 ---+---
 SO ! SE
 
Chaque canton est séparé par une frontière:
 
La frontière nord sépare NO et NE
La frontière  est sépare NE et SE
etc...

Chaque canton peut appartenir à une région différente.
Chaque région comporte au moins 4 cantons de carrés tous différents, sauf si elle se situe en bordure de carte.
Chaque carré appartient contient des cantons appartenant à au moins 2 régions différentes.

Mettre le slider à l'extrème droite ou à l'extrème gauche a des effets particuliers que je vous laisse découvrir.

Eric Mariacher - Renens 4 mai 2015 (Altran-Tesa)
la mème chose SBTisée