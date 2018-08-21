#writing csv file
import csv
import json
import xml.etree.ElementTree as ET

nodes = list()
composers = list()
edges = list()
#read csv file to get the nodes and all composer id
with open('composer_trim.csv', 'rb') as csvfile:
    spamreader = csv.reader(csvfile)
    for row in spamreader:
        node={'id': row[0], 'label': row[1]}
        nodes.append(node)
        composers.append(row[0])
nodes.pop(0)

# read xml to get the full edge list:
tree = ET.parse('composers.xml')
root = tree.getroot()
#print the child
for child in root:

    # if it's the edges child
    if str(child.tag)=="edges":
        #print("Edges list:")
        for node in child:
            edges.append(node.attrib)
# clean the edges that doesn't link the trimmed nodes
trim_edges=list()
for edge in edges:
    if ((edge['source'] in composers) and (edge['target'] in composers)):
        trim_edges.append(edge)
# add strength in trim_edges
for edge in trim_edges:
    edge['strength']=0.7
# change the name field to a field called label
for node in nodes:
    node['group']=0
    node['level']=1




# writing json file
with open('nodes.json', 'w') as f:
    json.dump(nodes, f)
with open('links.json', 'w') as f:
    json.dump(trim_edges, f)

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






