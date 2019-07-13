{-# LANGUAGE UnicodeSyntax  #-}
module Main where

import Lib (parseFile,translate,pass0,baseLookUp,process,pass1)
import System.Environment

main :: IO ()
main = do
   file <- (!! 0) <$> getArgs -- get args
   raw <- parseFile file -- parse file
   let name = reverse (drop 4 ( reverse file)) -- get name
   let sym  = pass0 raw -- construct sym table wo/ vars
   sym1 ← pass1 raw sym -- update sym table for variables
   let clean = process raw -- removes everything but A and C ins
   x <-  map (\λ -> λ++"\n") <$> mapM (\λ → translate λ sym1) clean -- translates and new lines
   writeFile (name ++ ".hack") (concat x) -- write file
