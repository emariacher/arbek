import Data.List
import Data.Function
import Control.Monad

le = [
 ("Eric", "France"),("Martine", "France"),
 ("John", "Great-Britain"),("Martha", "Great-Britain"),("Carine", "France"),
 ("Gerd", "Deutschland"),("Giuseppe", "Italia"),("Martha", "Deutschland")
 ]
 
dla xs = "European people: " ++ show [ fst x ++ " from " ++ snd x | x <- xs]
lc xs = [ head y | y <- group(sort([ snd x | x <- xs]))]
dlc = "European countries: " ++ show(lc le)
 
lpc4 xps = groupBy (\x y -> fst x == fst y) (sort([ (snd xp, fst xp) | xp <-xps ]))
lpc5 xps = groupBy (\x y -> snd x == snd y) (sortBy (compare  `on` snd)([ xp | xp <-xps ]))
lpc8 xps = unlines [ "People from " ++ show (fst (head xp)) ++ ":" ++ (show [snd x | x<-xp ]) | xp <-xps ]
lpc9 xps = unlines [ "People from " ++ show (snd (head xp)) ++ ":" ++ (show [fst x | x<-xp ]) | xp <-xps ]
dlac = [ dla le, dlc, lpc8(lpc4 le) ]
dlad = [ dla le, dlc, fait8b, fait9 ]

zorg = putStrLn (unlines(dlad))
             
fait4 r = do { m <- lpc4 r; m }
fait4b r = do { m <- groupBy (\x y -> fst x == fst y) (sort([ (snd xp, fst xp) | xp <-r ])); m }

fait5 y = do { u <- [[ (snd xp, fst xp) | xp <-y ]]; u }
fait5b y = do { u <- [sort [ (snd xp, fst xp) | xp <-y ]]; u }
fait5c y = do { u <- [groupBy (\x y -> fst x == fst y) (sort [ (snd xp, fst xp) | xp <-y ])]; u }

fait8 y = do { m <- [lpc4 y]; lpc8 m }
fait8b = do { m <- [lpc4 le]; lpc8 m }

fait9 = do { m <- [lpc5 le]; lpc9 m }

zurg = do
        c <- getChar  
        when (c /= ' ') $ do  
                putChar c  
                zurg  
main = do
        putStrLn "type ' zorg'" 
        zurg 
              
                