REM Eric Mariacher 14Jan05
REM bat file for making a Seapine SCM backup of the latest version of all branches
cd D:\temp
sscm lsmainline -y%1:%2 -z172.17.16.20:4900 > mainlinelist.txt
REM sscm get / -bRE_KBD_BT -dD:\temp\RE_KBD_BT -f -pRE_KBD_BT -r -wreplace -y%1:%2 -z172.17.16.20:4900
sscm lsbranch -ppersonal -y%1:%2 -z172.17.16.20:4900

