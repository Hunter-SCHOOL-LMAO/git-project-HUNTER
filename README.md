Method Summary:
---------------------------------------------------------------------------------------------------------------------
initRepo: Uses the inbuilt java File class to check if the basic repo files/folders exist, and if not, it creates them
---------------------------------------------------------------------------------------------------------------------
hashFile: Given a byte [], turn the contents into a 40 hexidecimal SHA-1 character using MessageDigest and BigInteger
---------------------------------------------------------------------------------------------------------------------
makeBlob: Given a byte [], writes the [] to a file in the objects folder. USE HASHFILE WHEN CALLING TO GENERATE A NAME
---------------------------------------------------------------------------------------------------------------------
updateIndex: Given a file's path, chech if it already exists in the index file. If it doesn't or if the hash is different, add/append it, and if the boolean is checked, make a blob a well
---------------------------------------------------------------------------------------------------------------------
fileTree: Starting point for fileTreeRecursive
---------------------------------------------------------------------------------------------------------------------
fileTreeRecursive: Given a starting path (As a String for some godawful reason) use StringBuilder to read the files in the directory, and add the fileName and it to a tree file in the Objects folder. If it encounters another directory, recurse and make a new tree file for the new directory
---------------------------------------------------------------------------------------------------------------------
initWorkingList (Deprecated): Reads the index and creates a ArrayList of the contents, sorts that, then turns it into a linkedList and calls recursiveColapse in order to generate a Tree
---------------------------------------------------------------------------------------------------------------------
recursiveCollapse (Deprecated): An ABSOLUTE PAIN IN MY ASS that IF IT *FUCKING WORKS* will generate tree files and eventually a root tree file for all files in the index
---------------------------------------------------------------------------------------------------------------------
initIndexTree: borrowed code from Shimon that uses an ingenious method of HashSets and the String.split function to generate a list of directories and entries
---------------------------------------------------------------------------------------------------------------------
initIndexTreehelper: borrowed code from Shimon that generates tree files for a directory given a list of all files in it
---------------------------------------------------------------------------------------------------------------------
Tester functions:
Creates a repository
Verifies instalation
Removes the created repo
Makes a blob
Compares the blob name to a Sha-1 hash of the file
Removes all files and directories in the Objects folder
Updates the index and creates more blobs x3
Outputs index contents, and then objects folder contents
Clears everything but the original files
---------------------------------------------------------------------------------------------------------------------
WorkingListObject (Deprecated): an empty carrier class intended for data normalization. It carries the object type, hash value, and path entirely for your convience
---------------------------------------------------------------------------------------------------------------------
slugcat gaming
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠉⠀⢻⣿⣿⣿⣿⣿⣿⡿⠁⠀⠙⢿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠏⠀⠀⠀⣾⣿⣿⣿⣿⣿⣿⠇⠀⠀⠀⠈⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⠀⠀⠀⠿⠛⠛⠛⠛⠿⣿⠀⠀⠀⠀⢀⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⣀⣤⡀⠀⠀⠀⠀⣀⡀⠀⠀⠀⢀⠀⢸⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⣴⣿⣿⠇⠀⠀⢠⣾⣿⣿⣆⠀⠀⠀⠀⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢡⣿⣿⣿⠀⠀⢀⣿⣿⣿⣿⣿⠀⠀⠀⢸⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠁⢸⣿⡿⠃⠀⠀⢸⣿⣿⣿⣿⠏⠀⠀⠀⡘⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡀⠀⠈⠀⠈⠭⠀⠈⠛⠿⠛⠋⠀⠀⠀⠀⠠⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⠈⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡏⠀⢣⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠀⠀⠈⢃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣿⠀⠀


AJ's Edits:

updateIndex:
In updateIndex, moved line = lineReader.readLine() to end of while loop

updateIndex now writes to index file and doesn't clear the existing content

Fixed bug in initIndexTree where final root directory and subtrees were being created in file named git/objectshash, not git/objects/hash, may need to make similar fixes elsewhere but we'll see


initIndexTree/initIndexTreeHelper:
Small change so files listed in the index file are created in objects folder if they weren't there before or were updated



GitTester updates:
Made initIndexTree run to see if it works

Just added a few more files and directories to test if the tree hashing functionality works and all seems good


commit:
Made new commit() function which takes in an author string and message string. It uses initIndexTree to get the hash of the current root tree based on index file, checks HEAD file to get previous commit hash, if there is one, and determines the current timestamp using LocalTime and LocalDate classes. Updates HEAD file to reflect latest commit hash.