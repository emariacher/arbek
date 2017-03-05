#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <stdarg.h>

#define STRINGIFY2( x) #x
#define STRINGIFY(x) STRINGIFY2(x)
#define valueInt( myvar) STRINGIFY(myvar##_), myvar
#define my_print(...) Log(__FILE__,__LINE__, __VA_ARGS__)
#define my_printInt( myvar) my_print("%s=%d",STRINGIFY(myvar##_), myvar)

void Log (char* sourcefile, int line, char * format, ...)
{
    char buffer[256];
    char *lastSlash = strrchr(sourcefile, '/');
    if(lastSlash!=NULL)   // si on a trouve le dernier slash, enlever ce qu'il y a avant
    {
        lastSlash++;
    }
    else     // sinon laisser le file path en entier
    {
        lastSlash = sourcefile;
    }
    va_list args;
    va_start (args, format);
    vsnprintf (buffer, 255, format, args);
    printf("%10s-%3d  %s\n",lastSlash,line,buffer);
    va_end (args);
}

typedef struct
{
    int PlayerHealth;
    int PlayerX, PlayerY;
    int Strength;
    int Health;
} TPlayerState;

// Save a players state to file
void SaveState(TPlayerState PlayerState)
{
    // Open the file for writing binary
    FILE *fSaveFile = fopen("SaveFile.bin", "wb");

    if (fSaveFile)
    {
        // Write the structure to the file
        fwrite(&PlayerState, sizeof(TPlayerState), 1, fSaveFile);
        fclose(fSaveFile);
    }
    else
    {
        printf("Error opening savefile!\n");
    }
}

// Loads a players state.  You'd call this with a pointer to the Player's State, like this:
// RestoreState(&PlayerState);
void RestoreState(TPlayerState *pPlayerState)
{
    // Open the file for reading binary
    FILE *fLoadFile = fopen("SaveFile.bin", "rb");

    if (fLoadFile)
    {
        // read the structure from the file
        fread(pPlayerState, sizeof(TPlayerState), 1, fLoadFile);
        fclose(fLoadFile);
    }
    else
    {
        printf("Error opening savefile!\n");
    }
}

int main()
{
    TPlayerState zob, zub;
    int vieille_valeur_de_zob_health;

    printf("Hello world!\n");
    my_print("Bonjour monde!");

    zob.Health = 1;
    zob.PlayerHealth = 2;
    zob.PlayerX = 3;
    zob.PlayerY = 4;
    zob.Strength = 5;

    my_print("Sauvons zob [%s=%d]",valueInt( zob.Health));
    SaveState(zob);

    my_printInt(zub.Health);

    zub.Health = 999;


    my_print("%s valait %d",valueInt( zub.Health));
    my_printInt(zub.Health);
    assert(zub.Health!=zob.Health); // verifions que zub.Health est different de zob.Health pour que le programme prouve bien quelquechose

    vieille_valeur_de_zob_health = zob.Health;
    zob.Health = 777;
    my_print("Changeons la valeur de %s qui vaut maintenant  %d",valueInt( zob.Health));


    my_print("Recuperons zob et affectons le a zub");
    RestoreState(&zub);
    my_print("Maintenant %s vaut %d, la meme valeur que %s qui vaut %d",
             valueInt( zub.Health),valueInt( vieille_valeur_de_zob_health));

    my_printInt(zub.Health);
    my_printInt(zob.Health);
    my_printInt(vieille_valeur_de_zob_health);
    assert(zub.Health==vieille_valeur_de_zob_health); // verifions que zub.Health abien maintenant la meme valeur que zob.Health

    assert(1==2); // verifions que la fonction assert remplit son office

    return 0;
}
