Eric Mariacher - St�fa 22 novembre 2012 (Altran-Phonak)

carte est un projet vieux de 32 ans, d'abord abord� en programmation en C
pour la g�n�ration automatique de cartes � Raleigh en 1993 (IBM).
La 1�re version de coloriage a �t� r�alis�e en Java � la Gaude en 1996 (IBM).
Cette version en Scala avec l'algorythme consid�r� comme "ultime" � �t� programm� � Lausanne en 2011 (Logitech).

l'unit� de base est le Carr� form� de 4 cantons:
 
 NO ! NE
 ---+---
 SO ! SE
 
Chaque canton est s�par� par une fronti�re:
 
La fronti�re nord s�pare NO et NE
La fronti�re  est s�pare NE et SE
etc...

Chaque canton peut appartenir � une r�gion diff�rente.
Chaque r�gion comporte au moins 4 cantons de carr�s tous diff�rents, sauf si elle se situe en bordure de carte.
Chaque carr� appartient contient des cantons appartenant � au moins 2 r�gions diff�rentes.

Mettre le slider � l'extr�me droite ou � l'extr�me gauche a des effets particuliers que je vous laisse d�couvrir.

Eric Mariacher - Renens 4 mai 2015 (Altran-Tesa)
la m�me chose SBTis�e