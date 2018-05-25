    main =
        do
            x <- getInteger
            let ans = fac x
            print ans

    fac n | n < 2 = 1
          | otherwise = n * fac (n-1)

    getInteger :: IO Integer
    getInteger = 
        do
            putStr "Enter an integer: "
            line <- getLine
            return (read line)

