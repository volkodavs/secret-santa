# Programming Challenge: Secret Santa

### Part one
Imagine that every year your extended family does a "Secret Santa" gift exchange. 
For this gift exchange, each person draws another person at random and then gets a gift for them. 
Write a program that will choose a Secret Santa for everyone given a list of all the members of your extended family. 
Obviously, a person cannot be their own Secret Santa.

### Part two
After the third year of having the Secret Santa gift exchange, 
you’ve heard complaints of having the same Secret Santa year after year. 
Modify your program so that a family member can only have the same Secret Santa once every 3 years.

### Part three
As your extended family has grown, members have gotten married and/or had children. 
Families usually get gifts for members of their immediate family, 
so it doesn’t make a lot of sense for anyone to be 
a Secret Santa for a member of their immediate family (spouse, parents, or children). 
Modify your program to take this constraint into consideration when choosing Secret Santas.

## Followup

### Q1. Describe your algorithm for selecting Secret Santas

#### Part one 

I have introduced random shift index for pair family member candidate. 
That algorithm allows building a Secret Santa pair in O(n) in runtime and O(n) for memory complexity  
<img width="547" alt="Part One Algorithm" src="https://user-images.githubusercontent.com/4140597/54382056-10ece280-4687-11e9-86c2-f7b6d134dbe7.png">

#### Part two 

I added a sliding window algorithm that store history for the last three years in a pair year/family member. 

<img width="618" alt="sliding window algorithm" src="https://user-images.githubusercontent.com/4140597/54473875-8f579a80-47d5-11e9-8b80-8c42897e5a51.png">

#### Part three

To store family relations, I used a weighted directed graph.

<img width="834" alt="weighted directed graph representation" src="https://user-images.githubusercontent.com/4140597/54473703-3b4bb680-47d3-11e9-993f-23b5bebfab23.png">
