module Main where

firsteq:: Char->(Char,String)->Bool
firsteq a (b,c) = (a == b)


assoc:: [(Char,String)] ->Char->String
assoc a b  = snd( head( filter (firsteq b)  a ))

linditer:: [(Char,String)]->String->String
linditer a b = concat ( map (assoc a)  b )


linden:: Int->[(Char,String)]->String->String
linden 0 a b = b
linden n a b = linditer  a ( linden (n-1) a b)

lindentst = linden 15 [('A',"B"),('B',"C"),('C',"AB")] "A"

prlind = print ( "this is the result =  " ++ lindentst ++ " " ++ show( length( lindentst)  ))

main = prlind