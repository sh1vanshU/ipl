Restful services

1. required team data (this will return team data (teamName, noOfMatches, win Or Loss, List of last N Matchs by team))
Get    /team/{teamName}
2. required Match data
Get     /team/<teamName>/{matches}?<year=2020>


Transient Annotation is used for Don't look for this field in the database, this is only for operation purpose.

default keyword will help you to create method in interface, Now in java has the ability to do this method creation o
 any thing in interface