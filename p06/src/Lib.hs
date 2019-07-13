{-# LANGUAGE UnicodeSyntax  #-}
module Lib where
import Text.Parsec
import Data.Maybe (fromJust,isJust,fromMaybe)

-- | parses label
parseLabel = do
  _ ← char '('
  s <- many (upper <|> lower <|> digit <|> oneOf "_$.")
  _ <- char ')'
  return $ Label s
  
-- | Whitespace parser
ws = many (oneOf " \t\n\r")

-- | parses A instructtion
parseA = do
  _ <- char '@'
  parsed <-  (many (digit <|> upper <|> digit <|> lower <|> oneOf "_.$"))
  return $ A parsed

-- | parse identifier and lifts from char -> string
parseNoOp = (\x -> [x]) <$> reg

-- | Parse head of c ynstruction
parseDestHead = try (  string "AMD")
  <|> try ( string "MD")
  <|> try  (string "AM")
  <|> try  (string "AD")
  <|> parseNoOp

-- | parses destination
parseDest = do
  head <- parseDestHead
  _ <- char '=' <* ws
  return $ head
  
-- | parse various jumps
jmps = try  (string "JGT")
  <|> try  (string "JEQ")
  <|> try  (string "JGE")
  <|> try  (string "JLT")
  <|> try  (string "JNE")
  <|> try  (string "JLE")
  <|> (string "JMP")
  
-- | more jumps  
parseJump = do
  _ <- char ';'
  jmps
-- | tries to parse varios bodies with ops eg A ; A+1;!A
parseBody = try parseOp
  <|> parseMiniOp
  <|> parseComp
  
-- | more comps
parseComp = try (string "0")
  <|> try (string "1")
  <|> try (string "-1")
  <|> try (string "D")
  <|> try (string "A")
  <|> try (string "!D")
  <|> try (string "!A")
  <|> try (string "-D")
  <|> try (string "-A")
  <|> try (string "!M")
  <|> try (string "-M")
  <|> parseNoOp

-- | parses c instruction
parseC = do
  head <- optionMaybe (try parseDest)
  body <- parseBody
  jmp  <- optionMaybe  (try parseJump)
  return $ Cins head body jmp

-- | parses operation part of C
parseOp = do
  dest <- reg
  z <- oneOf "+-|&" 
  dest1 <- reg <|> oneOf "01"
  return $ [dest] ++ [z] ++ [dest1]

-- | parses !0; -A
parseMiniOp = do
  op <- oneOf "-!"
  z <- reg <|> oneOf "01"
  return $ [op] ++ [z]
  
-- | parses register name combinator  
reg = oneOf "ADM"

-- | parses comment
parseComment = do
  _ <- string "//"
  a <- many $ noneOf "\n"
  return $  Comment a
  
-- | parses instruction
parseIns = parseComment <* ws
  <|> parseA <* ws
  <|> parseLabel <* ws
  <|> parseC <* ws

-- | parses multiple lines  
parseLines = many $ parseIns

-- | ASM Data type
data Asm = A String
  | Cins { head :: Maybe String,
           bod :: String,
           jmp :: Maybe String
           } 
  | Label String
  | Comment String
  | WS String -- whitespace
  deriving (Show)

-- checks strings for A and Label to Check if strings are ≡ 
instance Eq Asm where
  (Label x) == (A y)        = x == y
  (A y)     == (Label x)    = x == y
  (A y)     == (A x)        = x == y
  _         == _            = False


-- | runs parser over given file
parseFromFile p fname = do
  input <- readFile fname
  return (runParser p () fname input)

-- | parses file
parseFile ∷ FilePath → IO [Asm]
parseFile x = do
  raw <- parseFromFile parseLines x
  let e = case raw of
        (Left x) -> []
        (Right x) -> x
  return e

-- | filters only for the core instructions
process ∷ [Asm] → [Asm]
process x = do
  filter (not ∘ isNotCore) x

-- | selects non core instructions
isNotCore ∷ Asm → Bool
isNotCore v = case v of
  (Comment _) → True
  (WS _)      → True
  (Label _)   → True
  (_)         → False


-- | pass0 recursively constructs symbol tabl wo/ vars
pass0 ∷ [Asm] → [(Asm,Int)]
pass0 θ = proc 0 θ baseLookUp
  where proc counter (x:xs) symtable = if isLabel x
                               then proc (counter) xs (symtable ++ [(x,counter)])
                               else
                                 if isNotCore x
                                 then proc counter xs symtable
                                 else proc (counter+1) xs symtable
                                 where isLabel v = case v of
                                         (Label _)  -> True
                                         (_)      -> False
        proc counter (x)  symtable   =  symtable

-- | unpacks string from A data type
unpack (A δ) = δ

-- | translate number into binary based off of base divisor
toBin :: Int -> Int -> String
toBin 0 main = ""
toBin d main = if mod main d == main
  then "0" ++ toBin (div d 2) (rem main d)
  else "1" ++ toBin (div d 2) (rem main d)


-- base sym table
baseLookUp :: [(Asm,Int)]
baseLookUp = [(A "R0",0),
              (A "R1",1),
              (A "R2",2),
              (A "R3",3),
              (A "R4",4),
              (A "R5",5),
              (A "R6",6),
              (A "R7",7),
              (A "R8",8),
              (A "R9",9),
              (A "R10",10),
              (A "R11",11),
              (A "R12",12),
              (A "R13",13),
              (A "R14",14),
              (A "R15",15),
              (A "SP",0),
              (A "LCL",1),
              (A "ARG",2),
              (A "THIS",3),
              (A "THAT",4),
              (A "SCREEN",16384),
              (A "KBD",24576)]

-- | body look up tablen
bodyLookUp = [("M",  "1110000"),
              ("!M", "1110001"),
              ("-M", "1110011"),
              ("M+1","1110111"),
              ("M-1","1110010"),
              ("D+M","1000010"),
              ("D-M","1010011"),
              ("M-D","1000111"),
              ("D&M","1000000"),
              ("D|M","1010101"),
              ("0"  ,"0101010"),
              ("1"  ,"0111111"),
              ("-1" ,"0111010"),
              ("D"  ,"0001100"),
              ("A"  ,"0110000"),
              ("!D" ,"0001101"),
              ("!A" ,"0110001"),
              ("-D" ,"0001111"),
              ("-A" ,"0110011"),
              ("D+1","0011111"),
              ("A+1","0110111"),
              ("D-1","0001110"),
              ("A-1","0110010"),
              ("D+A","0000010"),
              ("D-A","0010011"),
              ("A-D","0000111"),
              ("D&A","0000000"),
              ("D|A","0010101")]
             
-- | destination lookup table 
destLookUp = [("M"  ,"001"),
              ("D"  ,"010"),
              ("MD" ,"011"),
              ("A"  ,"100"),
              ("AM" ,"101"),
              ("AD" ,"110"),
              ("AMD","111")]
             
-- | jump lookup table
jmpLookUp = [("JGT","001"),
             ("JEQ","010"),
             ("JGE","011"),
             ("JLT","100"),
             ("JNE","101"),
             ("JLE","110"),
             ("JMP","111")]
            
-- | translates a and c intructions int binary           
translate ∷ Asm → [(Asm,Int)] → IO  String
translate ins symtable = do
  case ins of
    a@(A x) -> do
      if isJust (lookup a symtable)
      then pure $  toBin (2^15) (fromJust (lookup a symtable))
      else pure $ toBin (2^15) (read x :: Int)
    (Cins des bod jmp)-> do
      let comp = if isJust (lookup bod bodyLookUp) then fromJust (lookup bod bodyLookUp) else "FAIL" -- looks up instruction in relevant table
      let dest = if isJust des  then fromJust (lookup (fromJust des) destLookUp) else "000"
      let jump  = if isJust jmp  then fromJust (lookup (fromJust jmp) jmpLookUp) else "000"
      pure $ "111" ++ comp ++ dest ++ jump

-- | creates variable references
pass1 ∷ [Asm] → [(Asm, Int)] → IO [(Asm,Int)]
pass1 x sym  = proc x sym 16
  where proc (x:xs) sym base = if (not ∘ isA) x --is it in a instruction
          then proc xs sym base
          else if isJust (lookup x sym)-- is it already in the symbol table
                  then (proc xs sym base)
                  else if (isJust ∘ (maybeRead ∷ String → Maybe Int) ∘ unpack) x -- can it be read as int?
                          then proc xs sym base
                          else (proc xs (sym ++ [(x,base)]) (base+1))
        proc [] sym _ = pure sym
        isA x = case x of
          (A _) → True
          (_)   → False
          
-- | unicode infix 
(∘) = (.)

-- | returns maybe monad of a depending if a is readable to supplied type
maybeRead :: Read a ⇒ String → Maybe a
maybeRead s = case reads s of
  [(x,"")] -> Just x
  _        -> Nothing
