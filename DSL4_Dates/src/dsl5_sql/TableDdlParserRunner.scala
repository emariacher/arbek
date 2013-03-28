package dsl5_sql

import scala.util.parsing.combinator.JavaTokenParsers

class TableDdlParser extends JavaTokenParsers {
  
  def tables: Parser[Map[String, Any]] = rep(table) ^^ { Map() ++ _ }
  
  def table: Parser[(String,Any)] = 
    ("TABLE" ~ tableName ~ columns 
       ^^ { case "TABLE" ~ tableName ~ tableContents => (tableName,tableContents) })
  
  def tableName: Parser[String] = ident ^^ { case ident => ident }
  
  def columns: Parser[Map[String, Any]] = "("~> repsep(column, ",") <~")" ^^ { Map() ++ _ }
  
  def column: Parser[(String,Any)] = 
    columnName ~ dataType ^^ { case columnName ~ dataType => (columnName,dataType) }
    
  def columnName: Parser[String] = ident ^^ { case ident => ident }
  
  def dataType: Parser[Any] = "VARCHAR" | "INTEGER"
  
}

object TableDdlParserRunner extends TableDdlParser {
  
  def main(args: Array[String]) {
    val input = 
      """TABLE person (first_name VARCHAR, last_name VARCHAR, age INTEGER)
         TABLE place (city VARCHAR, state VARCHAR)"""
    println(parseAll(tables,input))
  }
 
}