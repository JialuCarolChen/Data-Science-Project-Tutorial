from geopy.geocoders import Nominatim

from Panama import g1Convertor

filePath = "panama.txt"
edges = g1Convertor.get_edgelist(filePath)
print(edges)
# initialize a list to store
nodes = list()
# get all the node in edges
for country in edges:
    snode = country[0]
    nodes.append(snode)
    tnode = country[1]
    nodes.append(tnode)
# keep the unique node
nodes = list(set(nodes))
print("Number of Countries/Nodes: " + str(len(nodes)))

#get country cor and the converted nodes
countrycor = list()
converted_nodes = list()
for node in nodes:
    try:
        geolocator = Nominatim(timeout=100000)
        location = geolocator.geocode(node)
        lat = location.latitude
        long = location.longitude
        coucor = [node, lat, long]
        print(coucor)
        countrycor.append(coucor)
        converted_nodes.append(node)
    except (IOError, AttributeError):
        print("Error")

# print the conver
unconverted_nodes = list(set(nodes)-set(converted_nodes))
# print all the nodes that hasn't been converted
for node in unconverted_nodes:
    print(node)

import csv
with open('nodelist.csv', 'w') as csvfile:
    fieldnames = ['Id', 'Label', 'Latitude', 'Longitude']
    writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
    writer.writeheader()
    #write on the csv file
    for line in countrycor:
        writer.writerow({'Id': line[0], 'Label': line[0], 'Latitude': line[1], 'Longitude': line[2]})








