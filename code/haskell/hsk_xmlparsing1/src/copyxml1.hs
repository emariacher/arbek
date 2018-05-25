module Main
where
 
import Text.XML.HXT.Arrow
 
import System.IO
import System.Environment
import System.Console.GetOpt
import System.Exit
 
main :: IO()
main
    = do
      argv <- getArgs
      (al, src, dst) <- cmdlineOpts argv
      [rc]  <- runX (application al src dst)
      if rc >= c_err
         then exitWith (ExitFailure (0-1))
         else exitWith ExitSuccess
 
-- | the dummy for the boring stuff of option evaluation,
-- usually done with 'System.Console.GetOpt'
 
cmdlineOpts     :: [String] -> IO (Attributes, String, String)
cmdlineOpts argv
    = return ([(a_validate, v_0)], argv!!0, argv!!1)
 
-- | the main arrow
 
application     :: Attributes -> String -> String -> IOSArrow b Int
application al src dst
    = readDocument al src
      >>>
      processChildren (processDocumentRootElement `when` isElem)  -- (1)
      >>>
      writeDocument al dst
      >>>
      getErrStatus
 
 
-- | the dummy for the real processing: the identity filter
 
processDocumentRootElement      :: IOSArrow XmlTree XmlTree
processDocumentRootElement
    = this         -- substitute this by the real application 