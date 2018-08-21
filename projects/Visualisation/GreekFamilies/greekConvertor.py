import csv

# initiate a dictionary to store all the families
# {(mother, father): [child1, child2...]}
fam = dict()
# initiate a list to store single parent families
# [father/mother, child]
single_fam = list()

#read csv file to get the nodes and all composer id
with open('greek-gods.csv', 'rb') as csvfile:
    spamreader = csv.reader(csvfile)
    for row in spamreader:
        # read the row
        row = str(row[0]).split(';')
        # if not the titles
        if (row[0]!='NAME'):
            # if it has both father and mother
            if ((row[1]!='') and (row[2]!='')):
                # get the parent of the family
                fam_parent = (row[1], row[2])
                # if they are already in families
                if (fam_parent in list(fam.keys())):
                    # add the node to the child list
                    fam[fam_parent].append(row[0])
                # otherwise
                else:
                    fam[fam_parent]=[row[0]]
            # else if it has only father or mother
            elif (row[1]=='' and row[2]!=''):
                single_fam.append([row[2], row[0]])
            elif (row[2]=='' and row[1]!=''):
                single_fam.append([row[1], row[0]])

# initiate source, target list
st = list()
# initiate index to indicates the family relationship
index=1
# store the information of families with two parents in edge list
for parents, childs in fam.items():
    # father and mother points to relationship index
    st.append([parents[0], index])
    st.append([parents[1], index])
    # relationship index points to each child
    for child in childs:
        st.append([index, child])
    # increment index
    index=index+1

# store the single families information
for pcpair in single_fam:
    st.append(pcpair)
    index=index+1
for edge in st:
    print(edge)
print("number of families: "+str(index))

# write the edge list
with open('greek_family_edges.csv', 'w') as csvfile:
    fieldnames = ['Source', 'Target']
    writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
    writer.writeheader()
    #write on the csv file
    for line in st:
        writer.writerow({'Source': line[0], 'Target': line[1]})




