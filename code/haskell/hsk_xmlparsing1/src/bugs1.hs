{-# LANGUAGE Arrows, NoMonomorphismRestriction #-}
import Text.XML.HXT.Arrow
import Text.Regex.XMLSchema.String
import Data.List

data Defect = Defect
  { s_product, s_defect_number :: String,
    l_events :: [DefectEvent] }
  deriving (Show, Eq, Ord)
  
--instance Ord Defect where
--compare :: Defect -> Defect -> Ordering
--compare x y
-- | defproj x == defproj y    =  EQ
-- | defproj x <= defproj y    =  LT
-- | otherwise =  GT


data Product = Product
  { s_name :: String }
--    l_defects2 :: [Defect],
--    l_bugRows :: [BugRow] }
  deriving (Show, Eq)

data BugRow = BugRow
  { s_date :: String,
    l_bugStates :: [Int] }
  deriving (Show, Eq)

data TTD = TTD
  { l_defects :: [Defect],
    l_products :: [Product] }
  deriving (Show, Eq)

data DefectEvent = DefectEvent
  { s_event :: String,
    i_date :: Int }
  deriving (Show, Eq, Ord)
  
defevev :: DefectEvent -> String
defevev (DefectEvent x _) = x

defproj :: Defect -> String
defproj (Defect x _ _) = x

ttdef :: TTD -> [Defect]
ttdef (TTD x _) = x

ttprod :: TTD -> [Product]
ttprod (TTD _ x) = x

parseXML file = readDocument [(a_validate,v_0)] file

atTag tag = deep (isElem >>> hasName tag)
text = getChildren >>> getText

getDate :: [String] -> Int
getDate [m,d,y] = read (y++ (if length m == 2 then m else "0"++m) ++ (if length d == 2 then d else "0"++d))

getDefectEvent = atTag "defect-event" >>>
  proc l -> do
    event1       <- text <<< atTag "event-name"  -< l
    date        <- text <<< atTag "event-date"  -< l
    returnA -< DefectEvent
                { s_event = event1,
                  i_date  = getDate . take 3 . tokenize "[0-9]*" $ date
                }

getDefectOpen = atTag "reported-by-record" >>>
  proc l -> do
    date        <- text <<< atTag "date-found"  -< l
    returnA -< DefectEvent
                { s_event = "s_open",
                  i_date  = getDate . take 3 . tokenize "[0-9]*" $ date
                }

getDefect = atTag "defect" >>>
  proc defect -> do
    s_products          <- text <<< atTag "product"             -< defect
    s_defect_numbers    <- text <<< atTag "defect-number"       -< defect
    events              <- listA getDefectEvent                 -< defect
    opens               <- listA getDefectOpen                  -< defect
    returnA -< Defect
      { s_product       = s_products,
        s_defect_number = s_defect_numbers,
        l_events        = filter (\x -> not(defevev x `isInfixOf` "Confirm/Assign System Comment Assign/Re-assign")) events ++ opens
      }

getTTD = atTag "TestTrackData" >>>
  proc l -> do
    defects               <- listA getDefect    -< l
    returnA -< TTD
      { l_defects        = filter (\x -> not((take 1 (defproj x)) `isInfixOf` "xYZ")) defects,
        l_products       = [Product {s_name = z} | z <- [head y | y <- group (sort ([defproj x | x <- defects]))]]
      }
      
printTTD :: [TTD] -> IO()
printTTD [x] = putStrLn (unlines [show(ttdef x),show (ttprod x)]) 

main = do
  xml  <-  return $ parseXML "XML_Export_small.xml"
  ttds <-  runX(xml >>> getTTD)
  printTTD ttds
