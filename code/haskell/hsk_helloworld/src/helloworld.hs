import Data.List

le = [
 ("Eric", "France"),("Martine", "France"),
 ("John", "Great-Britain"),("Martha", "Great-Britain"),("Carine", "France"),
 ("Gerd", "Deutschland"),("Giuseppe", "Italia"),("Martha", "Deutschland")
 ]
 
dla xs = "European people: " ++ show [ fst x ++ " from " ++ snd x | x <- xs]
lc xs = [ head y | y <- group(sort([ snd x | x <- xs]))]
dlc xs = "European countries: " ++ show(lc xs)
 
lpc4 xps = groupBy (\x y -> fst x == fst y) (sort([ (snd xp, fst xp) | xp <-xps ]))
lpc8 xps = unlines [ "People from " ++ show (fst (head xp)) ++ ":" ++ (show [snd x | x<-xp ]) | xp <-xps ]
dlac = [ dla le, dlc le, lpc8(lpc4 le) ]

main = putStrLn (unlines(dlac))
                