#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <stdarg.h>

#define STRINGIFY2( x) #x
#define STRINGIFY(x) STRINGIFY2(x)
#define valueInt( myvar) STRINGIFY(myvar##_), myvar
#define printInt( myvar) printf("%10s-%3d  %s=%d\n",__FILE__,__LINE__,STRINGIFY(myvar##_), myvar)
#define my_print(...) Log(__FILE__,__LINE__, __VA_ARGS__)

typedef struct
{
    int PlayerHealth;
    int PlayerX, PlayerY;
    int Strength;
    int Health;
} TPlayerState;


void Log (char* sourcefile, int line, char * format, ...)
{
    char buffer[256];
    va_list args;
    va_start (args, format);
    vsnprintf (buffer, 255, format, args);
    printf("%10s-%3d  %s\n",sourcefile,line,buffer);
    va_end (args);
}

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

    printf("Hello world!\n");
    my_print("Bonjour monde!\n");

    zob.Health = 1;
    zob.PlayerHealth = 2;
    zob.PlayerX = 3;
    zob.PlayerY = 4;
    zob.Strength = 5;

    my_print("Sauvons zob [%s=%d]\n",valueInt( zob.Health));
    SaveState(zob);
    printInt(zub.Health);

    zub.Health = 999;

    my_print("%s valait %d\n",valueInt( zub.Health));
    printInt(zub.Health);
    assert(zub.Health!=zob.Health); // verifions que zub.Health est different de zob.Health pour que le programme prouve bien quelquechose

    my_print("Recuperons zob et affectons le a zub\n");
    RestoreState(&zub);
    my_print("maintenant %s vaut %d, la meme valeur que %s qui vaut %d\n",valueInt( zub.Health),valueInt( zob.Health));

    printInt(zub.Health);
    assert(zub.Health==zob.Health); // verifions que zub.Health a bien maintenant la meme valeur que zob.Health

    assert(1==2); // verifions aussi que la fonction assert remplit son office

    return 0;
}
