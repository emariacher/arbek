thislist0 = [6,3,4]
print(thislist0)
print(min(thislist0))
thislist = [[6,3,4],[7,3,4],[6,0,4],[1,1,1]]
print(thislist)
print(min(thislist))
print(list(map(min, thislist)))
zemin = min(list(map(min, thislist)))
print(zemin)
lignemin = list(map(min, thislist)).index(zemin)
print(lignemin)
colonnemin = thislist[lignemin].index(zemin)
print(colonnemin)
