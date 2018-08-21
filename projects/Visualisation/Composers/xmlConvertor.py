import xml.etree.ElementTree as ET
import json

# initiate a list to store nodes
nodes=list()
# initiate a list to store edges
edges=list()

# Reading the file from the disk:
tree = ET.parse('composers.xml')
root = tree.getroot()
#print the child
for child in root:
    # if it's the nodes child
    if str(child.tag)=="nodes":
        #print("Nodes list:")
        for node in child:
            nodes.append(node.attrib)

    # if it's the edges child
    if str(child.tag)=="edges":
        #print("Edges list:")
        for node in child:
            edges.append(node.attrib)

# add strength in edges
for edge in edges:
    edge['strength']=0.7
# change the name field to a field called label
for node in nodes:
    node['label']=node.pop('name')
    node['group']=0
    node['level']=1



# writing json file
with open('nodes.json', 'w') as f:
    json.dump(nodes, f)
with open('links.json', 'w') as f:
    json.dump(edges, f)

#writing csv file
import csv
with open('nodes.csv', 'w') as csvfile:
    fieldnames = ['Id', 'Label']
    writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
    writer.writeheader()
    #write on the csv file
    for node in nodes:
        try:
            writer.writerow({'Id': node['id'], 'Label': node['label']})
        except UnicodeEncodeError:
            writer.writerow({'Id': node['id'], 'Label': node['label'].encode('utf-8')})


with open('edges.csv', 'w') as csvfile:
    fieldnames = ['Source', 'Target']
    writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
    writer.writeheader()
    #write on the csv file
    for edge in edges:
        writer.writerow({'Source': edge['source'], 'Target': edge['target']})





