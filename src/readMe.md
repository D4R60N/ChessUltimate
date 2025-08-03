# move
f - forward
b - backward
l - left
r - right
+ - repeat
* - distance to end of the board

# rank/file moves
$ - starting symbol for recognition
F - file
R - rank
c - center
f - first
l - last

# units
e - enemy
a - ally
unitId - specific unit

# spaces
O - open
"?" - whatever symbol
E - enemy
A - ally
TO - to space

# logic
& - and
| - or
! - not

# game
p - player followed by player number
"-" - separator

# example
Ff+2:1.2 - Ff First file, +2 repetition with 2 spaces spacing, 1.2 from file with index 1 and 2.
Rc:5 - Rc Middle rank, 5 rank with index 5 in bout direction.
4.*:f,2:r+ - 4.5 From four to end of the board spaces forward, 2 spaces to the right with repeat no spacing.

2.3:b,2.3:r+ | 2.*:b+,2.3:l+
