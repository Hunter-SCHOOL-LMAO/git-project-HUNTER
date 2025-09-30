Method Summary:
initRepo: Uses the inbuilt java File class to check if the basic repo files/folders exist, and if not, it creates them
hashFile: Given a byte [], turn the contents into a 40 hexidecimal SHA-1 character using MessageDigest and BigInteger
makeBlob: Given a byte [], writes the [] to a file in the objects folder. USE HASHFILE WHEN CALLING TO GENERATE A NAME
updateIndex: given a file's path, creates a blob file if the boolean is set to true, and adds both of their name to a newline in the Index file

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