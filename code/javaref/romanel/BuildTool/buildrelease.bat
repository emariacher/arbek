sscm lsttdb -zch01s20:4900
sscm label DefHidpp.h -bRR_Quad -ccLabel_bulgroz -lbulgroz -o -pRR_Quad/Source_Code -v1 -y%1:%2 -zch01s20:4900

sscm label DefJB6.h -bRR_Quad -ccLabel_bulgroz -lbulgroz -o -pRR_Quad/Source_Code -v1 -y%1:%2 -zch01s20:4900

sscm label DefTypes.h -bRR_Quad -ccLabel_bulgroz -lbulgroz -o -pRR_Quad/Source_Code -v1 -y%1:%2 -zch01s20:4900

sscm add releasenote_bulgroz.xml -bRR_Quad -ccReleaseNote_bulgroz -g- -pRR_Quad/Documentation -u -zch01s20:4900 -y%1:%2
sscm checkout releasenote_bulgroz.xml -bRR_Quad -ccReleaseNote_bulgroz -f -pRR_Quad/Documentation -wreplace -zch01s20:4900 -y%1:%2
sscm checkin releasenote_bulgroz.xml -bRR_Quad -ccReleaseNote_bulgroz -f -g- -lbulgroz -o -pRR_Quad/Documentation -sCDBZU -a2 -u -zch01s20:4900 -i%1:%2 -y%1:%2
