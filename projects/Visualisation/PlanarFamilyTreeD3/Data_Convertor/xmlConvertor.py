import xml.etree.ElementTree as ET
import json
import csv
from Queue import Queue

# initiate a list to store nodes
nodes=list()
# initiate a list to store edges
edges=list()

# Reading the file from the disk:
tree = ET.parse('planar.xml')
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

child_parent_list=list()

for edge in edges:
    #replace source and target name with label in nodes
    source=edge['source']
    target=edge['target']
    for node in nodes:

        if node['id']==source:
            source_name=node['name']

        if node['id']==target:
            target_name=node['name']

    child_parent_list.append([target_name, source_name])

#get distinct label of nodes
node_names = list()
for node in nodes:
    node_names.append(node['name'])


formal_cp=list()
current_layer= Queue()
for link in child_parent_list:
    if link[1]=='planar':
        formal_cp.append(link)
        current_layer.put(link[0])
while (not current_layer.empty()):
    current_child=current_layer.get()
    #check if the current_child has child
    for link in child_parent_list:
        if link[1] == current_child:
            formal_cp.append(link)
            current_layer.put([link[0]])

for cp in formal_cp:
    print(cp)

with open('child_parent.csv', 'w') as csvfile:
    fieldnames = ['name', 'parent']
    writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
    writer.writeheader()
    #write on the csv file
    for line in formal_cp:
        writer.writerow({'name': line[0], 'parent': line[1]})

