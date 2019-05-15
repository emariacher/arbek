package com.frugalmechanic.optparse

object ParseIt extends App with OptParse {
	// For testing we want OptParse to throw exceptions instead of calling System.exit
	override val optParseExitOnError = false

			/**
			 * An example boolean option.
			 * 
			 * defaults to -f and --flag as long as no other option names conflict
			 */
			val flag = BoolOpt(desc="this is a flag")

			/**
			 * An example options taking a string as an argument
			 *
			 * Defaults to -s and --str as long as no other option names conflict
			 */
			val str = StrOpt()

			/**
			 * An example Int option
			 *
			 * Defaults to -n and --number since no other option names conflict
			 */
			val number = IntOpt()

			/**
			 * An example string option that can be passed multiple times
			 *
			 * Defaults to -a and the long form is overridden to --alias
			 */
			val aliases = MultiStrOpt(long="alias")

			/**
			 * Another flag (-b or --bool) to test multiple flags (e.g. -fb)
			 */
			val bool = BoolOpt()  

			println("Hello World!")
			parse(args)

			// An implicit Opt to Boolean conversion is used here
			if(flag) println("Flag is set")

			// The same implicit is used here along with an implicit from OptVal to Option (the str.get)
			if(str) println("Str is set: "+str.get)
			
			if(aliases) println("aliases is set: "+aliases.get)

}