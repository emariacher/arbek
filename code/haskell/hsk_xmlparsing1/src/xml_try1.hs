module Main
where

import Text.XML.HXT.Arrow
import Data.Tree.NTree.TypeDefs
import System.Exit

main    :: IO()
main
    = do
      [rc] <- runX ( readDocument [ (a_trace, "1")
                                  , (a_validate, v_0)
                                  ] "hello.xml"
                     >>>
                     buildXLSFile []
                     >>>
                     writeDocument [ (a_output_encoding, isoLatin1)
                                   ] "-"
                     >>>
                     getErrStatus
                   )
      exitWith ( if rc >= c_err
                 then ExitFailure 1
                 else ExitSuccess
               )
      
      
buildXLSFile     :: Attributes -> IOStateArrow s XmlTree XmlTree
buildXLSFile userOptions
    = perform ( traceMsg 1 ("buildXLSFile:")
                >>>
--                prepareContents userOptions encodeDocument
--                deep isXText
--                >>>
                traceMsg 1 "buildXLSFile: finished"
              )


type XmlFilter = XmlTree -> [XmlTree]



isXText                       :: XmlFilter       -- XmlTree -> [XmlTree]
isXText t@(NTree (XText _) _) =  [t]
isXText _                     =  []

      